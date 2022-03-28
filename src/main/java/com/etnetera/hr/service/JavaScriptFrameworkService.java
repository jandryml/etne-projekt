package com.etnetera.hr.service;

import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import com.etnetera.hr.exception.ResourceAlreadyExistsException;
import com.etnetera.hr.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface JavaScriptFrameworkService {

    /**
     * @return all JavaScript frameworks
     */
    List<JavaScriptFrameworkWithVersionsResponse> findAll();

    /**
     * Retrieves JavaScript framework by its name
     *
     * @param name name of JavaScript framework
     * @return the JavaScript framework with given name
     * @throws ResourceNotFoundException if not found
     */
    JavaScriptFrameworkWithVersionsResponse findByName(String name) throws ResourceNotFoundException;

    /**
     * Retrieves a JavaScript framework by its name and version
     *
     * @param name name of JavaScript framework
     * @param version version of JavaScript framework
     * @return the JavaScript framework with given name and version
     * @throws ResourceNotFoundException if not found
     */
    JavaScriptFrameworkPlainResponse findByNameAndVersion(String name, String version) throws ResourceNotFoundException;

    /**
     * Creates JavaScript framework.
     *
     * @param plainRequest must not be null and be valid
     * @return created JavaScript framework
     * @throws ResourceAlreadyExistsException if a JavaScript framework with same name and version already exists
     */
    JavaScriptFrameworkWithVersionsResponse create(@NotNull @Valid JavaScriptFrameworkPlainRequest plainRequest) throws ResourceAlreadyExistsException;

    /**
     * Updates JavaScript framework with given name and version
     *
     * @param name name of JavaScript framework
     * @param version version of JavaScript framework
     * @param dataPatchRequest must not be null and be valid
     * @return updated JavaScript framework
     * @throws ResourceNotFoundException if a JavaScript framework with this name and version not found
     */
    JavaScriptFrameworkWithVersionsResponse updateDeprecationDateAndHypeLevel(
            String name,
            String version,
            @NotNull @Valid JavaScriptFrameworkDataPatchRequest dataPatchRequest) throws ResourceNotFoundException;

    /**
     * Delete JavaScript framework by given name
     */
    void deleteByName(String name);

    /**
     * Delete JavaScript framework by given name and version
     */
    void deleteByNameAndVersion(String name, String version);
}
