package com.revolut.api.validator;

import com.revolut.dto.LocalTransferRequest;
import com.revolut.error.InvalidRequestException;

import static com.revolut.error.TransferExceptionFactory.getInvalidRequestException;

public class LocalTransferRequestValidator implements Validator<LocalTransferRequest> {

    private static final String TRANSFER_MISSING = "transfer.input.missing";
    private static final String TRANSFER_SENDER_BRANCH_MISSING = "transfer.input.senderBranch.missing";
    private static final String TRANSFER_SENDER_ACCOUNT_MISSING = "transfer.input.senderAccount.missing";
    private static final String TRANSFER_RECEIVER_BRANCH_MISSING = "transfer.input.receiverBranch.missing";
    private static final String TRANSFER_RECEIVER_ACCOUNT_MISSING = "transfer.input.receiverAccount.missing";
    private static final String TRANSFER_AMOUNT_MISSING = "transfer.input.amount.missing";
    private static final String TRANSFER_AMOUNT_INVALID = "transfer.input.amount.invalid";

    protected LocalTransferRequestValidator() {

    }

    @Override
    public void validate(LocalTransferRequest request) throws InvalidRequestException {

        if (request == null) {
            throw getInvalidRequestException(TRANSFER_MISSING, "Transfer request can not be null");
        }

        if (request.getSenderBranchCode() == null) {
            throw getInvalidRequestException(TRANSFER_SENDER_BRANCH_MISSING, "Sender branch can not be null");
        }

        if (request.getSenderAccountNumber() == null) {
            throw getInvalidRequestException(TRANSFER_SENDER_ACCOUNT_MISSING, "Sender account can not be null");
        }

        if (request.getReceiverBranchCode() == null) {
            throw getInvalidRequestException(TRANSFER_RECEIVER_BRANCH_MISSING, "Receiver branch can not be null");
        }

        if (request.getReceiverAccountNumber() == null) {
            throw getInvalidRequestException(TRANSFER_RECEIVER_ACCOUNT_MISSING, "Receiver account can not be null");
        }

        if (request.getAmount() == null) {
            throw getInvalidRequestException(TRANSFER_AMOUNT_MISSING, "Amount can not be null");
        }

        if (request.getAmount() < 0) {
            throw getInvalidRequestException(TRANSFER_AMOUNT_INVALID, "Amount can not be below zero");
        }

        if (request.getReceiverBranchCode() == request.getSenderBranchCode() &&
            request.getReceiverAccountNumber() == request.getSenderAccountNumber()) {
            throw getInvalidRequestException("transfer.input.sameAccount", "Sender and receiver accounts can not be same");
        }
    }



}
