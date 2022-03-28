package com.etnetera.hr.dto.mapper;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.etnetera.hr.data.mother.JavaScriptFrameworkMother.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class JavaScriptFrameworkMapperTest {

    private final JavaScriptFrameworkMapper javaScriptFrameworkMapper = new JavaScriptFrameworkMapperImpl();

    @Test
    void toJavaScriptFrameworkWithVersionsResponseTest() {
        JavaScriptFramework model = getJavaScriptFramework();

        JavaScriptFrameworkWithVersionsResponse result = javaScriptFrameworkMapper.toJavaScriptFrameworkWithVersionsResponse(model);

        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo("Super framework");
            softly.assertThat(result.getVersions().size()).isEqualTo(2);
            softly.assertThat(result.getVersions().get(0).getVersion()).isEqualTo("1.0.1");
            softly.assertThat(result.getVersions().get(0).getDeprecationDate()).isEqualTo(LocalDate.of(2020, 1, 1).toString());
            softly.assertThat(result.getVersions().get(0).getHypeLevel()).isEqualTo(3);
            softly.assertThat(result.getVersions().get(1).getVersion()).isEqualTo("2.0.1");
            softly.assertThat(result.getVersions().get(1).getDeprecationDate()).isEqualTo(LocalDate.of(2025, 1, 1).toString());
            softly.assertThat(result.getVersions().get(1).getHypeLevel()).isEqualTo(8);
        });
    }

    @Test
    void toJavaScriptFrameworkWithVersionsResponseListTest() {
        List<JavaScriptFramework> models = List.of(getJavaScriptFramework());

        List<JavaScriptFrameworkWithVersionsResponse> result = javaScriptFrameworkMapper.toJavaScriptFrameworkWithVersionsResponse(models);

        assertSoftly(softly -> {
            softly.assertThat(result.size()).isEqualTo(1);
            softly.assertThat(result.get(0).getName()).isEqualTo("Super framework");
            softly.assertThat(result.get(0).getVersions().size()).isEqualTo(2);
            softly.assertThat(result.get(0).getVersions().get(0).getVersion()).isEqualTo("1.0.1");
            softly.assertThat(result.get(0).getVersions().get(0).getDeprecationDate()).isEqualTo(LocalDate.of(2020, 1, 1).toString());
            softly.assertThat(result.get(0).getVersions().get(0).getHypeLevel()).isEqualTo(3);
            softly.assertThat(result.get(0).getVersions().get(1).getVersion()).isEqualTo("2.0.1");
            softly.assertThat(result.get(0).getVersions().get(1).getDeprecationDate()).isEqualTo(LocalDate.of(2025, 1, 1).toString());
            softly.assertThat(result.get(0).getVersions().get(1).getHypeLevel()).isEqualTo(8);
        });
    }

    @Test
    void toJavaScriptFrameworkPlainResponseTest() {
         JavaScriptFrameworkVersion javaScriptFrameworkVersion = getJavaScriptFrameworkVersion();

        JavaScriptFrameworkPlainResponse result = javaScriptFrameworkMapper.toJavaScriptFrameworkPlainResponse(javaScriptFrameworkVersion);

        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo("Super framework");
            softly.assertThat(result.getVersion()).isEqualTo("2.0.1");
            softly.assertThat(result.getDeprecationDate()).isEqualTo(LocalDate.of(2025, 1, 1).toString());
            softly.assertThat(result.getHypeLevel()).isEqualTo(8);
        });
    }

    @Test
    void toJavaScriptFrameworkVersionTest() {
        JavaScriptFrameworkPlainRequest request = getJavaScriptFrameworkPlainRequestBuilder().build();

        JavaScriptFrameworkVersion result = javaScriptFrameworkMapper.toJavaScriptFrameworkVersion(request);

        assertSoftly(softly -> {
            softly.assertThat(result.getVersion()).isEqualTo("2.0.1");
            softly.assertThat(result.getDeprecationDate()).isEqualTo(LocalDate.of(2025, 1, 1).toString());
            softly.assertThat(result.getHypeLevel()).isEqualTo(8);
        });
    }

}