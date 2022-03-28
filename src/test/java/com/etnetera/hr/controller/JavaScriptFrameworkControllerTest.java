package com.etnetera.hr.controller;

import com.etnetera.hr.dto.response.JavaScriptFrameworkVersionResponse;
import com.etnetera.hr.exception.ResourceAlreadyExistsException;
import com.etnetera.hr.exception.ResourceNotFoundException;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.etnetera.hr.data.mother.JavaScriptFrameworkMother.*;
import static com.etnetera.hr.util.TestUtil.toJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JavaScriptFrameworkController.class)
class JavaScriptFrameworkControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private JavaScriptFrameworkService javaScriptFrameworkService;

    @Test
    public void findAll_Multiple() throws Exception {
        when(javaScriptFrameworkService.findAll()).thenReturn(
                List.of(getJavaScriptFrameworkWithVersionsResponseBuilder()
                                .build(),
                        getJavaScriptFrameworkWithVersionsResponseBuilder()
                                .name("Bad framework")
                                .versions(List.of(new JavaScriptFrameworkVersionResponse("1.0.1", LocalDate.of(2015, 1, 1).toString(), 2)))
                                .build())
        );

        mvc.perform(get("/frameworks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("Super framework"))
                .andExpect(jsonPath("$.[0].versions.length()").value(1))
                .andExpect(jsonPath("$.[1].name").value("Bad framework"))
                .andExpect(jsonPath("$.[1].versions.length()").value(1));

        verify(javaScriptFrameworkService, times(1)).findAll();
    }

    @Test
    public void findAll_One() throws Exception {
        when(javaScriptFrameworkService.findAll()).thenReturn(
                List.of(getJavaScriptFrameworkWithVersionsResponseBuilder().build())
        );

        mvc.perform(get("/frameworks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].name").value("Super framework"))
                .andExpect(jsonPath("$.[0].versions.length()").value(1));

        verify(javaScriptFrameworkService, times(1)).findAll();
    }

    @Test
    public void findAll_Empty() throws Exception {
        when(javaScriptFrameworkService.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/frameworks")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(javaScriptFrameworkService, times(1)).findAll();
    }

    @Test
    public void findByName_OK() throws Exception {
        final String frameworkName = "Super framework";

        when(javaScriptFrameworkService.findByName(frameworkName)).thenReturn(
                getJavaScriptFrameworkWithVersionsResponseBuilder().build()
        );

        mvc.perform(get("/frameworks/{name}", frameworkName)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(frameworkName))
                .andExpect(jsonPath("$.versions.length()").value(1))
                .andExpect(jsonPath("$.versions.[0].version").value("2.0.1"))
                .andExpect(jsonPath("$.versions.[0].deprecationDate").value(LocalDate.of(2025, 1, 1).toString()))
                .andExpect(jsonPath("$.versions.[0].hypeLevel").value(8));

        verify(javaScriptFrameworkService, times(1)).findByName(frameworkName);
    }

    @Test
    public void findByName_NotFound() throws Exception {
        final String frameworkName = "Super framework";

        doThrow(new ResourceNotFoundException("JS framework not found!"))
                .when(javaScriptFrameworkService).findByName(frameworkName);

        mvc.perform(get("/frameworks/{name}", frameworkName)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(javaScriptFrameworkService, times(1)).findByName(frameworkName);
    }

    @Test
    public void findByNameAndVersion_OK() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        when(javaScriptFrameworkService.findByNameAndVersion(frameworkName, frameworkVersion)).thenReturn(
                getJavaScriptFrameworkPlainResponseBuilder().build()
        );

        mvc.perform(get("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(frameworkName))
                .andExpect(jsonPath("$.version").value(frameworkVersion))
                .andExpect(jsonPath("$.deprecationDate").value(LocalDate.of(2025, 1, 1).toString()))
                .andExpect(jsonPath("$.hypeLevel").value(8));

        verify(javaScriptFrameworkService, times(1)).findByNameAndVersion(frameworkName, frameworkVersion);
    }

    @Test
    public void findByNameAndVersion_NotFound() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        doThrow(new ResourceNotFoundException("JS framework not found!"))
                .when(javaScriptFrameworkService).findByNameAndVersion(frameworkName, frameworkVersion);

        mvc.perform(get("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(javaScriptFrameworkService, times(1)).findByNameAndVersion(frameworkName, frameworkVersion);
    }

    @Test
    public void create_OK() throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder().build();

        when(javaScriptFrameworkService.create(any())).thenReturn(
                getJavaScriptFrameworkWithVersionsResponseBuilder().build()
        );

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Super framework"))
                .andExpect(jsonPath("$.versions.length()").value(1))
                .andExpect(jsonPath("$.versions[0].version").value("2.0.1"))
                .andExpect(jsonPath("$.versions[0].deprecationDate").value(LocalDate.of(2025, 1, 1).toString()))
                .andExpect(jsonPath("$.versions[0].hypeLevel").value(8));

        verify(javaScriptFrameworkService, times(1)).create(any());
    }

    @Test
    public void create_Conflict() throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder().build();

        doThrow(new ResourceAlreadyExistsException("JS framework already exists found!"))
                .when(javaScriptFrameworkService).create(any());

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Name definitely longer than 15 characters"})
    public void create_NotValidName(String name) throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .name(name)
                .build();

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"56", "xxx", "1.0", "1..0", "best version"})
    public void create_NotValidVersion(String version) throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .version(version)
                .build();

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_NotValidDeprecationDate() throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .deprecationDate(null)
                .build();

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11, 42, 666})
    public void create_NotValidHypeLevel(int hypeLevel) throws Exception {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .hypeLevel(hypeLevel)
                .build();

        mvc.perform(post("/frameworks")
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDeprecationDateAndHypeLevel_OK() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        var requestData = getJavaScriptFrameworkDataPatchRequestBuilder().build();

        when(javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(eq(frameworkName), eq(frameworkVersion), any())).thenReturn(
                getJavaScriptFrameworkWithVersionsResponseBuilder()
                        .build()
        );

        mvc.perform(patch("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Super framework"))
                .andExpect(jsonPath("$.versions.length()").value(1));

        verify(javaScriptFrameworkService, times(1)).updateDeprecationDateAndHypeLevel(eq(frameworkName), eq(frameworkVersion), any());
    }

    @Test
    public void updateDeprecationDateAndHypeLevel_NotFound() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        var requestData = getJavaScriptFrameworkDataPatchRequestBuilder().build();

        when(javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(eq(frameworkName), eq(frameworkVersion), any()))
                .thenThrow(new ResourceNotFoundException("JS framework not found!"));

        mvc.perform(patch("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(javaScriptFrameworkService, times(1)).updateDeprecationDateAndHypeLevel(eq(frameworkName), eq(frameworkVersion), any());

    }

    @Test
    public void updateDeprecationDateAndHypeLevel_NotValidDeprecationDate() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        var requestData = getJavaScriptFrameworkDataPatchRequestBuilder()
                .deprecationDate(null)
                .build();

        mvc.perform(patch("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11, 42, 666})
    public void updateDeprecationDateAndHypeLevel_NotValidHypeLevel(int hypeLevel) throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        var requestData = getJavaScriptFrameworkDataPatchRequestBuilder()
                .hypeLevel(hypeLevel)
                .build();

        mvc.perform(patch("/frameworks/{name}/{version}", frameworkName, frameworkVersion)
                        .content(toJsonString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteByName_OK() throws Exception {
        final String frameworkName = "Super framework";

        mvc.perform(delete("/frameworks/{name}", frameworkName))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteByName_NotFound() throws Exception {
        final String frameworkName = "Super framework";

        doThrow(new ResourceNotFoundException("Sensor not found!"))
                .when(javaScriptFrameworkService).deleteByName(frameworkName);

        mvc.perform(delete("/frameworks/{name}", frameworkName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteByNameAndVersion_OK() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        mvc.perform(delete("/frameworks/{name}/{version}", frameworkName, frameworkVersion))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteByNameAndVersion_NotFound() throws Exception {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        doThrow(new ResourceNotFoundException("Sensor not found!"))
                .when(javaScriptFrameworkService).deleteByNameAndVersion(frameworkName, frameworkVersion);

        mvc.perform(delete("/frameworks/{name}/{version}", frameworkName, frameworkVersion))
                .andExpect(status().isNotFound());
    }
}
