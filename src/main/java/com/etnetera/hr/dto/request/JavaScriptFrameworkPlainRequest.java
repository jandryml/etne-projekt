package com.etnetera.hr.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@Builder
public class JavaScriptFrameworkPlainRequest {
    @Size(max = 15)
    @NotEmpty
    private final String name;
    @NotEmpty
    @Pattern(regexp = "^\\d+.\\d+.\\d+$")
    private final String version;
    @NotNull
    private final String deprecationDate;
    @Min(value = 1)
    @Max(value = 10)
    private final int hypeLevel;
}
