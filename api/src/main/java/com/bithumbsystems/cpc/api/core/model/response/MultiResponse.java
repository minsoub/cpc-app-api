package com.bithumbsystems.cpc.api.core.model.response;

import com.bithumbsystems.cpc.api.core.model.enums.ReturnCode;
import lombok.Getter;

import java.util.List;

@Getter
public class MultiResponse<T> {
    private final ReturnCode result;
    private final List<T> data;

    MultiResponse(List<T> data) {
        this.result = ReturnCode.SUCCESS;
        this.data = data;
    }
}
