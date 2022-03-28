package com.etnetera.hr.data.mother;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkVersionResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;

import java.time.LocalDate;
import java.util.List;

import static com.etnetera.hr.dto.request.JavaScriptFrameworkDataPatchRequest.JavaScriptFrameworkDataPatchRequestBuilder;
import static com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest.JavaScriptFrameworkPlainRequestBuilder;
import static com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse.JavaScriptFrameworkPlainResponseBuilder;
import static com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse.JavaScriptFrameworkWithVersionsResponseBuilder;

public class JavaScriptFrameworkMother {

    private JavaScriptFrameworkMother() {
    }

    public static JavaScriptFrameworkPlainRequestBuilder getJavaScriptFrameworkPlainRequestBuilder() {
        return JavaScriptFrameworkPlainRequest.builder()
                .name("Super framework")
                .version("2.0.1")
                .deprecationDate(LocalDate.of(2025, 1, 1).toString())
                .hypeLevel(8);
    }

    public static JavaScriptFrameworkDataPatchRequestBuilder getJavaScriptFrameworkDataPatchRequestBuilder() {
        return JavaScriptFrameworkDataPatchRequest.builder()
                .deprecationDate(LocalDate.of(2026, 1, 1).toString())
                .hypeLevel(7);
    }

    public static JavaScriptFrameworkPlainResponseBuilder getJavaScriptFrameworkPlainResponseBuilder() {
        return JavaScriptFrameworkPlainResponse.builder()
                .name("Super framework")
                .version("2.0.1")
                .deprecationDate(LocalDate.of(2025, 1, 1).toString())
                .hypeLevel(8);
    }

    public static JavaScriptFrameworkWithVersionsResponseBuilder getJavaScriptFrameworkWithVersionsResponseBuilder() {
        List<JavaScriptFrameworkVersionResponse> versions = List.of(
                new JavaScriptFrameworkVersionResponse("2.0.1", LocalDate.of(2025, 1, 1).toString(), 8));
        return JavaScriptFrameworkWithVersionsResponse.builder()
                .name("Super framework")
                .versions(versions);
    }

    public static JavaScriptFramework getJavaScriptFramework() {
        JavaScriptFramework javaScriptFramework = new JavaScriptFramework()
                .setName("Super framework");

        List<JavaScriptFrameworkVersion> versions = List.of(
                new JavaScriptFrameworkVersion()
                        .setVersion("1.0.1")
                        .setDeprecationDate(LocalDate.of(2020, 1, 1))
                        .setHypeLevel(3)
                        .setFramework(javaScriptFramework),
                new JavaScriptFrameworkVersion()
                        .setVersion("2.0.1")
                        .setDeprecationDate(LocalDate.of(2025, 1, 1))
                        .setHypeLevel(8)
                        .setFramework(javaScriptFramework)
        );

        return javaScriptFramework.setVersions(versions);
    }

    public static JavaScriptFrameworkVersion getJavaScriptFrameworkVersion() {
        JavaScriptFrameworkVersion javaScriptFrameworkVersion = new JavaScriptFrameworkVersion()
                .setVersion("2.0.1")
                .setDeprecationDate(LocalDate.of(2025, 1, 1))
                .setHypeLevel(8);

        JavaScriptFramework javaScriptFramework = new JavaScriptFramework()
                .setName("Super framework")
                .setVersions(List.of(javaScriptFrameworkVersion));

        return javaScriptFrameworkVersion.setFramework(javaScriptFramework);
    }
}
