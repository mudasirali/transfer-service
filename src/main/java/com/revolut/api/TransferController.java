package com.revolut.api;

import com.google.inject.Inject;
import com.revolut.dto.LocalTransferRequest;
import com.revolut.dto.TransferDTO;
import com.revolut.dto.TransferResponse;
import com.revolut.dto.TransferSearchRequest;
import com.revolut.service.TransferService;
import spark.Request;
import spark.Response;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;

public class TransferController extends BaseController {


    public static final String TRANSFER_ID = "transferId";


    @Inject
    private TransferService transferService;


    public TransferResponse makeLocalTransfer(Request req, Response res) {
        LocalTransferRequest request = buildRequest(req.body(), LocalTransferRequest.class);

        TransferResponse transferResponse = transferService.makeLocalTransfer(request);

        res.status(HTTP_CREATED);
        return transferResponse;
    }

    public List<TransferDTO> searchTransfers(Request req, Response res) {
        TransferSearchRequest request = buildRequest(req.body(), TransferSearchRequest.class);

        List<TransferDTO> searchResult = transferService.searchTransfers(request);
        return searchResult;
    }

    public TransferDTO getTransfer(Request req, Response res) {
        Long transferId = id(req.params(TRANSFER_ID));
        TransferDTO transfer = transferService.getTransfer(transferId);
        return transfer;
    }

}
