package com.etnetera.hr.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class JavaScriptFrameworkVersionResponse {
    private final String version;
    private final String deprecationDate;
    private final int hypeLevel;
}
