package com.etnetera.hr.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JavaScriptFrameworkWithVersionsResponse {
    private final String name;
    private final List<JavaScriptFrameworkVersionResponse> versions;
}
