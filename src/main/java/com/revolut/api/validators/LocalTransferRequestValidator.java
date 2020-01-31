package com.revolut.api.validators;

import com.revolut.dto.LocalTransferRequest;
import com.revolut.dto.TransferSearchRequest;
import com.revolut.error.InvalidRequestException;

public class LocalTransferRequestValidator implements Validator<LocalTransferRequest> {

    public static final String TRANSFER_MISSING = "transfer.missing";
    public static final String TRANSFER_SENDER_BRANCH_MISSING = "transfer.senderBranch.missing";
    public static final String TRANSFER_SENDER_ACCOUNT_MISSING = "transfer.senderAccount.missing";
    public static final String TRANSFER_RECEIVER_BRANCH_MISSING = "transfer.receiverBranch.missing";
    public static final String TRANSFER_RECEIVER_ACCOUNT_MISSING = "transfer.receiverAccount.missing";
    public static final String TRANSFER_AMOUNT_MISSING = "transfer.amount.missing";
    public static final String TRANSFER_AMOUNT_INVALID = "transfer.amount.invalid";

    protected LocalTransferRequestValidator() {

    }

    @Override
    public void validate(LocalTransferRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException(TRANSFER_MISSING, "Transfer request can not be null");
        }

        if (request.getSenderBranchCode() == null) {
            throw new InvalidRequestException(TRANSFER_SENDER_BRANCH_MISSING, "Sender branch can not be null");
        }

        if (request.getSenderAccountNumber() == null) {
            throw new InvalidRequestException(TRANSFER_SENDER_ACCOUNT_MISSING, "Sender account can not be null");
        }

        if (request.getReceiverBranchCode() == null) {
            throw new InvalidRequestException(TRANSFER_RECEIVER_BRANCH_MISSING, "Receiver branch can not be null");
        }

        if (request.getReceiverAccountNumber() == null) {
            throw new InvalidRequestException(TRANSFER_RECEIVER_ACCOUNT_MISSING, "Receiver account can not be null");
        }

        if (request.getAmount() == null) {
            throw new InvalidRequestException(TRANSFER_AMOUNT_MISSING, "Amount can not be null");
        }

        if (request.getAmount() < 0) {
            throw new InvalidRequestException(TRANSFER_AMOUNT_INVALID, "Amount can not be below zero");
        }
    }


}
