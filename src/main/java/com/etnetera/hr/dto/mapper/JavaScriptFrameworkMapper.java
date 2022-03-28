package com.etnetera.hr.dto.mapper;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JavaScriptFrameworkMapper {

    JavaScriptFrameworkWithVersionsResponse toJavaScriptFrameworkWithVersionsResponse(JavaScriptFramework model);

    List<JavaScriptFrameworkWithVersionsResponse> toJavaScriptFrameworkWithVersionsResponse(List<JavaScriptFramework> models);

    default JavaScriptFrameworkPlainResponse toJavaScriptFrameworkPlainResponse(JavaScriptFrameworkVersion frameworkVersion) {
        return JavaScriptFrameworkPlainResponse.builder()
                .name(frameworkVersion.getFramework().getName())
                .version(frameworkVersion.getVersion())
                .deprecationDate(frameworkVersion.getDeprecationDate().toString())
                .hypeLevel(frameworkVersion.getHypeLevel())
                .build();
    }

    JavaScriptFrameworkVersion toJavaScriptFrameworkVersion(JavaScriptFrameworkPlainRequest request);

}
