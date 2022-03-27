package com.etnetera.hr.service;

import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;

public interface JavaScriptFrameworkService {

    Iterable<JavaScriptFrameworkWithVersionsResponse> findAll();

    JavaScriptFrameworkWithVersionsResponse findByName(String name);

    JavaScriptFrameworkPlainResponse findByNameAndVersion(String name, String version);

    JavaScriptFrameworkPlainResponse create(JavaScriptFrameworkPlainRequest javaScriptFrameworkPlainRequest);

    JavaScriptFrameworkPlainResponse update(JavaScriptFrameworkPlainRequest javaScriptFrameworkPlainRequest);

    JavaScriptFrameworkPlainResponse updateDeprecationDateAndHypeLevel(
            String name,
            String version,
            JavaScriptFrameworkDataPatchRequest javaScriptFrameworkDataPatchRequest);

    void deleteByName(String name);

    void deleteByNameAndVersion(String name, String version);
}
