package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */

@RestController
@RequiredArgsConstructor
public class JavaScriptFrameworkController {

    private final JavaScriptFrameworkService javaScriptFrameworkService;

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> frameworks() {
        return javaScriptFrameworkService.findAll();
    }

}
