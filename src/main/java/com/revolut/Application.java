package com.revolut;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.api.TransferController;
import com.revolut.config.GuiceModule;
import com.revolut.error.TransferException;
import spark.ResponseTransformer;

import static spark.Spark.*;

public class Application {


    public static final String API_RESPONSE_TYPE = "application/json";

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GuiceModule());
        TransferController transferController = injector.getInstance(TransferController.class);
        Gson gson = injector.getInstance(Gson.class);

        routing(transferController, gson::toJson);

        exception(TransferException.class, (e, req, res) -> {
            res.status(e.getHttpStatus());
            res.type(API_RESPONSE_TYPE);
            JsonObject errorDto = new JsonObject();
            errorDto.addProperty("message", e.getMessage());
            errorDto.addProperty("code", e.getCode());
            res.body(errorDto.toString());
        });


        after((request, response) -> {
            response.type(API_RESPONSE_TYPE);
        });
    }

    public static void routing(TransferController transferController, ResponseTransformer transformer) {


        post("/transfer", transferController::makeLocalTransfer, transformer);
        post("/transfer/search", transferController::searchTransfers, transformer);
        get("/transfer/:transferId", transferController::getTransfer, transformer);



    }




}
