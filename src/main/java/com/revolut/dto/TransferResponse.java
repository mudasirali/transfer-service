package com.revolut.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransferResponse {

    private Long id;
    private TransactionStatus status;

}
