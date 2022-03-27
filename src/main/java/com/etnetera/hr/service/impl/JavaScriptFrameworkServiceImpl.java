package com.etnetera.hr.service.impl;

import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JavaScriptFrameworkServiceImpl implements JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository javaScriptFrameworkRepository;

    private final JavaScriptFrameworkVersionRepository javaScriptFrameworkVersionRepository;

    @Override
    public Iterable<JavaScriptFrameworkWithVersionsResponse> findAll() {
        return null;
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse findByName(String name) {
        return null;
    }

    @Override
    public JavaScriptFrameworkPlainResponse findByNameAndVersion(String name, String version) {
        return null;
    }

    @Override
    public JavaScriptFrameworkPlainResponse create(JavaScriptFrameworkPlainRequest javaScriptFrameworkPlainRequest) {
        return null;
    }

    @Override
    public JavaScriptFrameworkPlainResponse update(JavaScriptFrameworkPlainRequest javaScriptFrameworkPlainRequest) {
        return null;
    }

    @Override
    public JavaScriptFrameworkPlainResponse updateDeprecationDateAndHypeLevel(String name, String version, JavaScriptFrameworkDataPatchRequest javaScriptFrameworkDataPatchRequest) {

        return null;
    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void deleteByNameAndVersion(String name, String version) {

    }
}
