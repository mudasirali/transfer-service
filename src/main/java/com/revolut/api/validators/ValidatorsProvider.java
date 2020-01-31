package com.revolut.api.validators;

import com.google.inject.Provider;
import com.revolut.dto.TransferSearchRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class ValidatorsProvider implements Provider<Map<Class, List<Validator>>> {

    @Override
    public Map<Class, List<Validator>> get() {
        return new HashMap<Class, List<Validator>>(){{

            put(TransferSearchRequest.class,
                    newArrayList(
                            ValidatorFactory.getSearchRequestValidator()));

        }};
    }

}
