package com.etnetera.hr.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class JavaScriptFrameworkDataPatchRequest {
    @NotNull
    private final String deprecationDate;
    @Min(value = 1)
    @Max(value = 10)
    private final int hypeLevel;
}
