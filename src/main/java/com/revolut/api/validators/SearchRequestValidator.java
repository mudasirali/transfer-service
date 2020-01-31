package com.revolut.api.validators;

import com.revolut.dto.TransferSearchRequest;
import com.revolut.error.InvalidRequestException;

public class SearchRequestValidator implements Validator<TransferSearchRequest> {

    public static final String SEARCH_MISSING = "search.missing";
    public static final String SEARCH_END_MISSING = "search.end.missing";
    public static final String SEARCH_START_MISSING = "search.start.missing";

    protected SearchRequestValidator() {

    }

    @Override
    public void validate(TransferSearchRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException(SEARCH_MISSING, "Search request can not be null");
        }

        if (request.getEnd() == null) {
            throw new InvalidRequestException(SEARCH_END_MISSING, "End date can not be null");
        }

        if (request.getStart() == null) {
            throw new InvalidRequestException(SEARCH_START_MISSING, "End date can not be null");
        }
    }


}
