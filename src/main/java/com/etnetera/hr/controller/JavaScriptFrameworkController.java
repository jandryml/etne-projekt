package com.etnetera.hr.controller;

import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import com.etnetera.hr.exception.ResourceNotFoundException;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */

@RestController
@RequestMapping("/frameworks")
@RequiredArgsConstructor
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkService javaScriptFrameworkService;

    @GetMapping
    public List<JavaScriptFrameworkWithVersionsResponse> findAll() {
        return javaScriptFrameworkService.findAll();
    }

    @GetMapping("/{name}")
    public JavaScriptFrameworkWithVersionsResponse findByName(@PathVariable String name) {
        return javaScriptFrameworkService.findByName(name);
    }

    @GetMapping("/{name}/{version}")
    public JavaScriptFrameworkPlainResponse findByNameAndVersion(@PathVariable String name, @PathVariable String version) {
        return javaScriptFrameworkService.findByNameAndVersion(name, version);
    }

    @PutMapping
    public ResponseEntity<JavaScriptFrameworkWithVersionsResponse> save(@Valid @RequestBody JavaScriptFrameworkPlainRequest javaScriptFrameworkPlainRequest) {
        JavaScriptFrameworkWithVersionsResponse result;
        HttpStatus httpStatus;
        try {
            result = javaScriptFrameworkService.update(javaScriptFrameworkPlainRequest);
            httpStatus = HttpStatus.OK;
        } catch (ResourceNotFoundException e) {
            result = javaScriptFrameworkService.create(javaScriptFrameworkPlainRequest);
            httpStatus = HttpStatus.CREATED;
        }
        return ResponseEntity.status(httpStatus).body(result);
    }

    @PatchMapping("/{name}/{version}")
    public JavaScriptFrameworkWithVersionsResponse updateDeprecationDateAndHypeLevel(
            @PathVariable String name,
            @PathVariable String version,
            @Valid @RequestBody JavaScriptFrameworkDataPatchRequest javaScriptFrameworkDataPatchRequest) {
        return javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(name, version, javaScriptFrameworkDataPatchRequest);
    }

    @DeleteMapping("/{name}")
    public void deleteByName(@PathVariable String name) {
        javaScriptFrameworkService.deleteByName(name);
    }

    @DeleteMapping("/{name}/{version}")
    public void deleteByNameAndVersion(@PathVariable String name, @PathVariable String version) {
        javaScriptFrameworkService.deleteByNameAndVersion(name, version);
    }

}
