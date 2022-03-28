package com.etnetera.hr.service.impl;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.dto.mapper.JavaScriptFrameworkMapper;
import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import com.etnetera.hr.exception.ResourceAlreadyExistsException;
import com.etnetera.hr.exception.ResourceNotFoundException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JavaScriptFrameworkServiceImpl implements JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository javaScriptFrameworkRepository;

    private final JavaScriptFrameworkVersionRepository javaScriptFrameworkVersionRepository;

    private final JavaScriptFrameworkMapper mapper;

    @Override
    public List<JavaScriptFrameworkWithVersionsResponse> findAll() {
        return mapper.toJavaScriptFrameworkWithVersionsResponse(javaScriptFrameworkRepository.findAll());
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse findByName(String name) {
        return mapper.toJavaScriptFrameworkWithVersionsResponse(
                javaScriptFrameworkRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
    }

    @Override
    public JavaScriptFrameworkPlainResponse findByNameAndVersion(String name, String version) {
        return mapper.toJavaScriptFrameworkPlainResponse(
                javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(name, version)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found")));
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse create(JavaScriptFrameworkPlainRequest plainRequest) {
        javaScriptFrameworkVersionRepository
                .findByFrameworkNameAndVersion(plainRequest.getName(), plainRequest.getVersion())
                .ifPresent(it -> {
                    throw new ResourceAlreadyExistsException("JavaScript framework exists");
                });

        // issue with returning iterable
        var framework = javaScriptFrameworkRepository.findByName(plainRequest.getName())
                .orElse(new JavaScriptFramework().setName(plainRequest.getName()));

        var frameworkVersion = mapper.toJavaScriptFrameworkVersion(plainRequest).setFramework(framework);

        javaScriptFrameworkVersionRepository.save(frameworkVersion);

        framework.getVersions().add(frameworkVersion);

        return mapper.toJavaScriptFrameworkWithVersionsResponse(frameworkVersion.getFramework());
    }

    @Override
    //TODO remove
    public JavaScriptFrameworkWithVersionsResponse update(JavaScriptFrameworkPlainRequest plainRequest) {
        var javaScriptFrameworkVersion = javaScriptFrameworkVersionRepository
                .findByFrameworkNameAndVersion(plainRequest.getName(), plainRequest.getVersion())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("JavaScript framework with name %s not found", plainRequest.getName())));

        javaScriptFrameworkVersionRepository.save(
                updateJavaScriptFrameworkVersion(
                        javaScriptFrameworkVersion, plainRequest.getDeprecationDate(), plainRequest.getHypeLevel()
                ));

        return mapper.toJavaScriptFrameworkWithVersionsResponse(javaScriptFrameworkVersion.getFramework());
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse updateDeprecationDateAndHypeLevel(
            String name, String version, JavaScriptFrameworkDataPatchRequest dataPatchRequest) {
        var fetchedValue = javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(name, version)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        javaScriptFrameworkVersionRepository.save(
                updateJavaScriptFrameworkVersion(
                        fetchedValue, dataPatchRequest.getDeprecationDate(), dataPatchRequest.getHypeLevel()
                ));

        return mapper.toJavaScriptFrameworkWithVersionsResponse(fetchedValue.getFramework());
    }

    @Override
    public void deleteByName(String name) {
        javaScriptFrameworkRepository.deleteById(findByName(name).getId());
    }

    @Override
    public void deleteByNameAndVersion(String name, String version) {
        javaScriptFrameworkRepository.deleteById(findByNameAndVersion(name, version).getId());
    }

    private JavaScriptFrameworkVersion updateJavaScriptFrameworkVersion(JavaScriptFrameworkVersion frameworkVersion, String deprecationDate, int hypeLevel) {
        frameworkVersion.setDeprecationDate(LocalDate.parse(deprecationDate));
        frameworkVersion.setHypeLevel(hypeLevel);
        return frameworkVersion;
    }
}
