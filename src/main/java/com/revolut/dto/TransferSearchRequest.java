package com.revolut.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferSearchRequest {


    private LocalDateTime start;

    private LocalDateTime end;

    //For very basic navigation/scrolling
    private int pageNo = 1;



}
