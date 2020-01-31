package com.revolut.repository;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.domain.tables.Accounts;
import com.revolut.domain.tables.Transfers;
import com.revolut.domain.tables.records.AccountsRecord;
import com.revolut.dto.*;
import com.revolut.error.TooMuchAccountActivityException;
import com.revolut.error.TransferExceptionFactory;
import com.revolut.util.RetryUtil;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static com.revolut.domain.Tables.ACCOUNTS;
import static com.revolut.domain.Tables.TRANSFERS;
import static com.revolut.error.TransferExceptionFactory.*;
import static org.jooq.impl.DSL.concat;


@Singleton
public class TransferRepository {

    private static final int PAGE_SIZE = 20;
    private static final Settings WITH_LOCK = new Settings().withExecuteWithOptimisticLocking(true).withRenderOutputForSQLServerReturningClause(true);
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TransferRepository.class);

    public static final String SENDER_MISSING = "sender.missing";
    public static final String RECEIVER_MISSING = "receiver.missing";
    public static final String TRANSFER_MISSING = "transfer.missing";

    @Inject
    private DataSource dataSource;

    @Inject
    TransferDTOMapper transferDTOMapper;


    public TransferDTO getTransfer(Long id) {

        Accounts sender = ACCOUNTS.as("sender");
        Accounts receiver = ACCOUNTS.as("receiver");
        Transfers transfer = TRANSFERS.as("transfer");

        return selectTransfers(transfer, sender, receiver)
                .where(transfer.ID.eq(id))
                .fetchOptional().map(transferDTOMapper::map)
                .orElseThrow( () -> getTransferNotFoundException(TRANSFER_MISSING, String.format("Transfer %s not found", id)));

    }

    public List<TransferDTO> searchTransfers(TransferSearchRequest request) {

        Accounts sender = ACCOUNTS.as("sender");
        Accounts receiver = ACCOUNTS.as("receiver");
        Transfers transfer = TRANSFERS.as("transfer");

        Condition condition = transfer.CREATED_ON.ge(request.getStart()).and(transfer.CREATED_ON.le(request.getEnd()));
        return selectTransfers(transfer, sender, receiver)
                .where(condition)
                .orderBy(transfer.CREATED_ON.desc())
                .limit((request.getPageNo() - 1) * PAGE_SIZE, PAGE_SIZE)
                .fetch(transferDTOMapper);
    }

    public TransferResponse createTransfer(LocalTransferRequest localTransferRequest) {

        log.debug("Executing local transfer: {}", localTransferRequest);

        Long transferId = RetryUtil.<LocalTransferRequest, Long>defaultExecutor()
                .retryOn(DataAccessException.class)
                .backOfferiod(500)
                .wrapErrorWith(TransferExceptionFactory::getTooMuchAccountActivityException)
                .execute(() -> makeTransfer(localTransferRequest));

        log.debug("Saved transfer: {}", transferId);

        return TransferResponse.builder()
                .id(transferId)
                .status(TransactionStatus.OK)
                .build();

    }

    private Long makeTransfer(LocalTransferRequest localTransferRequest) {
        Accounts senderAlias = ACCOUNTS.as("sender");
        Accounts receiverAlias = ACCOUNTS.as("receiver");
        Condition senderCondition = senderAlias.BRANCH_ID.eq(localTransferRequest.getSenderBranchCode())
                .and(senderAlias.ACCOUNT_NUMBER.eq(localTransferRequest.getSenderAccountNumber()));

        Condition receiverCondition = receiverAlias.BRANCH_ID.eq(localTransferRequest.getReceiverBranchCode())
                .and(receiverAlias.ACCOUNT_NUMBER.eq(localTransferRequest.getReceiverAccountNumber()));

        Long transferId = DSL.using(dataSource, SQLDialect.H2, WITH_LOCK)
                .transactionResult(config -> {
                    DSLContext ctx = DSL.using(config);

                    AccountsRecord sender = ctx.selectFrom(senderAlias)
                            .where(senderCondition)
                            .fetchOptional()
                            .orElseThrow(() -> accountNotFoundException(SENDER_MISSING,
                                    localTransferRequest.getSenderBranchCode(),
                                    localTransferRequest.getSenderAccountNumber()));

                    if (sender.getAccountBalance() < localTransferRequest.getAmount()) {
                        throw getInsufficientFundsException("sender.funds.insufficient", String.format("Account %s%s has insufficient funds",
                                localTransferRequest.getSenderBranchCode(),
                                localTransferRequest.getSenderAccountNumber()));
                    }

                    sender.setAccountBalance(sender.getAccountBalance() - localTransferRequest.getAmount());
                    sender.store();

                    AccountsRecord receiver = ctx.selectFrom(receiverAlias)
                            .where(receiverCondition)
                            .fetchOptional()
                            .orElseThrow(() -> accountNotFoundException(RECEIVER_MISSING,
                                    localTransferRequest.getReceiverBranchCode(),
                                    localTransferRequest.getReceiverAccountNumber()));

                    receiver.setAccountBalance(receiver.getAccountBalance() + localTransferRequest.getAmount());
                    receiver.store();

                    Long id = ctx.insertInto(TRANSFERS)
                            .columns(TRANSFERS.SENDER_ID, TRANSFERS.RECEIVER_ID, TRANSFERS.AMOUNT, TRANSFERS.STATUS)
                            .values(sender.getId(), receiver.getId(), localTransferRequest.getAmount(), TransactionStatus.OK.getCode())
                            .returning(TRANSFERS.ID)
                            .fetchOne().getValue(TRANSFERS.ID);

                    return id;
                });


        return transferId;
    }


    private SelectOnConditionStep<Record7<Long, Long, Integer, LocalDateTime, String, String, String>> selectTransfers(Transfers transfer, Accounts sender, Accounts receiver) {


        return DSL.using(dataSource, SQLDialect.H2)
                .select(transfer.ID,
                        transfer.AMOUNT,
                        transfer.STATUS,
                        transfer.CREATED_ON,
                        concat(sender.BRANCH_ID, sender.ACCOUNT_NUMBER).as("senderAccount"),
                        concat(receiver.BRANCH_ID, receiver.ACCOUNT_NUMBER).as("receiverAccount"),
                        receiver.TITLE.as("receiverName"))
                .from(transfer)
                .join(sender).on(transfer.SENDER_ID.eq(sender.ID))
                .join(receiver).on(transfer.RECEIVER_ID.eq(receiver.ID));
    }

}
