package com.etnetera.hr.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JavaScriptFrameworkPlainResponse {
    private final Long id;
    private final String name;
    private final String version;
    private final String deprecationDate;
    private final int hypeLevel;
}
