package com.revolut.repository;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.domain.tables.Customers;
import com.revolut.domain.tables.Transfers;
import com.revolut.dto.LocalTransferRequest;
import com.revolut.dto.TransferDTO;
import com.revolut.dto.TransferResponse;
import com.revolut.dto.TransferSearchRequest;
import org.jooq.Condition;
import org.jooq.Record7;
import org.jooq.SQLDialect;
import org.jooq.SelectOnConditionStep;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static com.revolut.domain.Tables.CUSTOMERS;
import static com.revolut.domain.Tables.TRANSFERS;
import static org.jooq.impl.DSL.concat;


@Singleton
public class TransferRepository {

    public static final int PAGE_SIZE = 20;
    public static final Settings SETTINGS_WITH_LOCK = new Settings().withExecuteWithOptimisticLocking(true);

    @Inject
    private DataSource dataSource;

    @Inject
    TransferDTOMapper transferDTOMapper;


    public TransferDTO getTransfer(Long id) {

        Customers sender = CUSTOMERS.as("sender");
        Customers receiver = CUSTOMERS.as("receiver");
        Transfers transfer = TRANSFERS.as("transfer");

        return selectTransfers(transfer, sender, receiver)
                .where(transfer.ID.eq(id))
                .fetchOne(transferDTOMapper);

    }

    public List<TransferDTO> searchTransfers(TransferSearchRequest request) {

        Customers sender = CUSTOMERS.as("sender");
        Customers receiver = CUSTOMERS.as("receiver");
        Transfers transfer = TRANSFERS.as("transfer");

        Condition condition = transfer.CREATED_ON.ge(request.getStart()).and(transfer.CREATED_ON.le(request.getEnd()));
        return selectTransfers(transfer, sender, receiver)
                .where(condition)
                .orderBy(transfer.ID.desc())
                .limit((request.getPageNo() - 1) * PAGE_SIZE, PAGE_SIZE)
                .fetch(transferDTOMapper);
    }

    public TransferResponse createTransfer(LocalTransferRequest localTransferRequest) {

        DSL.using(dataSource, SQLDialect.H2)
                .transaction( config -> {

                } );

        return null;
    }

    private SelectOnConditionStep<Record7<Long, Long, Byte, LocalDateTime, String, String, String>> selectTransfers(Transfers transfer, Customers sender, Customers receiver) {


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
