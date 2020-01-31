package com.revolut.api;

import com.google.inject.Inject;
import com.revolut.dto.LocalTransferRequest;
import com.revolut.dto.TransferDTO;
import com.revolut.dto.TransferResponse;
import com.revolut.dto.TransferSearchRequest;
import com.revolut.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;

@Slf4j
public class TransferController extends BaseController {


    @Inject
    private TransferService transferService;



    public TransferResponse makeLocalTransfer(Request req, Response res) {
        log.debug("executing transfer: {}", req.body());
        LocalTransferRequest request = buildRequest(req.body(), LocalTransferRequest.class);

        TransferResponse transferResponse = transferService.makeLocalTransfer(request);
        log.trace("transfer completed: {}", transferResponse);
        res.status(HTTP_CREATED);
        return transferResponse;
    }

    public List<TransferDTO> searchTransfers(Request req, Response res) {
        log.debug("searching transfers: {}", req.body());
        TransferSearchRequest request = buildRequest(req.body(), TransferSearchRequest.class);

        List<TransferDTO> searchResult = transferService.searchTransfers(request);
        log.trace("search completed: {}", searchResult);
        return searchResult;
    }

    public TransferDTO getTransfer(Request req, Response res) {
        Long transferId = id(req.params("transferId"));
        log.debug("getting transfer {}", transferId);
        TransferDTO transfer = transferService.getTransfer(transferId);

        log.trace("transfer fetched", transfer);
        return transfer;
    }

}
