package com.revolut.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.dto.*;
import com.revolut.repository.TransferRepository;

import java.util.List;
import java.util.UUID;

@Singleton
public class TransferService {

    @Inject
    private TransferRepository transferRepository;

    public TransferResponse makeLocalTransfer(LocalTransferRequest request) {
        return transferRepository.createTransfer(request);
    }

    public List<TransferDTO> searchTransfers(TransferSearchRequest request) {
        return transferRepository.searchTransfers(request);
    }

    public TransferDTO getTransfer(Long id) {
        return transferRepository.getTransfer(id);
    }
}
