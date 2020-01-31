package com.revolut.api.validators;

import com.revolut.dto.TransferSearchRequest;

public class ValidatorFactory {

    public static Validator<TransferSearchRequest> getSearchRequestValidator() {
        return new SearchRequestValidator();
    }

}
