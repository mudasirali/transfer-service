package com.revolut.dto;

import lombok.Data;

@Data
public class LocalTransferRequest {

    private Integer senderBranchCode;
    private Long senderAccountNumber;
    private Integer receiverBranchCode;
    private Long receiverAccountNumber;
    private Long amount;

}
