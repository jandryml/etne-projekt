package com.etnetera.hr.service.impl;

import com.etnetera.hr.data.JavaScriptFramework;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JavaScriptFrameworkServiceImpl implements JavaScriptFrameworkService {

    private final JavaScriptFrameworkRepository javaScriptFrameworkRepository;

    private final JavaScriptFrameworkVersionRepository javaScriptFrameworkVersionRepository;

    private final JavaScriptFrameworkMapper mapper;

    @Override
    public List<JavaScriptFrameworkWithVersionsResponse> findAll() {
        return javaScriptFrameworkRepository.findAll().stream()
                .map(mapper::toJavaScriptFrameworkWithVersionsResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse findByName(String name) {
        return javaScriptFrameworkRepository.findByName(name)
                .map(mapper::toJavaScriptFrameworkWithVersionsResponse)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("JavaScript framework with name %s not found", name)));
    }

    @Override
    public JavaScriptFrameworkPlainResponse findByNameAndVersion(String name, String version) {
        return javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(name, version)
                .map(mapper::toJavaScriptFrameworkPlainResponse)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("JavaScript framework with name %s and version %s not found", name, version)));
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse create(JavaScriptFrameworkPlainRequest plainRequest) {
        // verify that framework with same name and version doesn't exist
        javaScriptFrameworkVersionRepository
                .findByFrameworkNameAndVersion(plainRequest.getName(), plainRequest.getVersion())
                .ifPresent(it -> {
                    throw new ResourceAlreadyExistsException(String.format("JavaScript framework with name %s and version %s not found", plainRequest.getName(), plainRequest.getVersion()));
                });

        // try to find framework by name to prevent framework names duplications
        var framework = javaScriptFrameworkRepository.findByName(plainRequest.getName())
                // if not found, create new
                .orElse(new JavaScriptFramework().setName(plainRequest.getName()));

        // add new version of framework
        framework.getVersions().add(mapper.toJavaScriptFrameworkVersion(plainRequest).setFramework(framework));
        javaScriptFrameworkRepository.save(framework);

        return mapper.toJavaScriptFrameworkWithVersionsResponse(framework);
    }

    @Override
    public JavaScriptFrameworkWithVersionsResponse updateDeprecationDateAndHypeLevel(String name, String version,
                                                                                     JavaScriptFrameworkDataPatchRequest dataPatchRequest) {
        var fetchedValue = javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(name, version)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("JavaScript framework with name %s and version %s not found", name, version)));

        javaScriptFrameworkVersionRepository.save(
                fetchedValue
                        .setDeprecationDate(LocalDate.parse(dataPatchRequest.getDeprecationDate()))
                        .setHypeLevel(dataPatchRequest.getHypeLevel())
        );

        return mapper.toJavaScriptFrameworkWithVersionsResponse(fetchedValue.getFramework());
    }

    @Override
    public void deleteByName(String name) {
        javaScriptFrameworkRepository.deleteById(findByName(name).getId());
    }

    @Override
    public void deleteByNameAndVersion(String name, String version) {
        javaScriptFrameworkVersionRepository.deleteById(findByNameAndVersion(name, version).getId());
    }
}
