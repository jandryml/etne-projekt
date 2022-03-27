package com.etnetera.hr.dto.mapper;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface JavaScriptFrameworkMapper {

    JavaScriptFrameworkWithVersionsResponse toJavaScriptFrameworkWithVersionsResponse(JavaScriptFramework model);

    default List<JavaScriptFrameworkPlainResponse> toJavaScriptFrameworkPlainResponse(JavaScriptFramework model) {
        return model.getVersions().stream()
                .map(it -> JavaScriptFrameworkPlainResponse.builder()
                        .name(model.getName())
                        .version(it.getVersion())
                        .deprecationDate(it.getDeprecationDate().toString())
                        .hypeLevel(it.getHypeLevel())
                        .build())
                .collect(Collectors.toList());
    }
}
