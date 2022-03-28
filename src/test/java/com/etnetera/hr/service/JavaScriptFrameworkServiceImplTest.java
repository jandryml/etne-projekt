package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.dto.mapper.JavaScriptFrameworkMapper;
import com.etnetera.hr.dto.request.JavaScriptFrameworkPlainRequest;
import com.etnetera.hr.dto.response.JavaScriptFrameworkPlainResponse;
import com.etnetera.hr.dto.response.JavaScriptFrameworkWithVersionsResponse;
import com.etnetera.hr.exception.ResourceAlreadyExistsException;
import com.etnetera.hr.exception.ResourceNotFoundException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import com.etnetera.hr.service.impl.JavaScriptFrameworkServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.etnetera.hr.data.mother.JavaScriptFrameworkMother.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JavaScriptFrameworkServiceImplTest {

    @MockBean
    private JavaScriptFrameworkRepository javaScriptFrameworkRepository;

    @MockBean
    private JavaScriptFrameworkVersionRepository javaScriptFrameworkVersionRepository;

    @Autowired
    private JavaScriptFrameworkMapper mapper;

    @Autowired
    private JavaScriptFrameworkServiceImpl javaScriptFrameworkService;

    @Test
    void findAll_Multiple() {
        when(javaScriptFrameworkRepository.findAll()).thenReturn(List.of(getJavaScriptFramework(), getJavaScriptFramework().setName("Another framework")));

        assertEquals(2, javaScriptFrameworkService.findAll().size());

        verify(javaScriptFrameworkRepository, times(1)).findAll();
    }

    @Test
    void findAll_One() {
        when(javaScriptFrameworkRepository.findAll()).thenReturn(List.of(getJavaScriptFramework()));

        assertEquals(1, javaScriptFrameworkService.findAll().size());

        verify(javaScriptFrameworkRepository, times(1)).findAll();
    }

    @Test
    void findAll_Empty() {
        when(javaScriptFrameworkRepository.findAll()).thenReturn(List.of());

        assertEquals(0, javaScriptFrameworkService.findAll().size());

        verify(javaScriptFrameworkRepository, times(1)).findAll();
    }

    @Test
    void findByName_Found() {
        final String frameworkName = "Super framework";
        when(javaScriptFrameworkRepository.findByName(frameworkName)).thenReturn(Optional.of(getJavaScriptFramework()));

        var response = javaScriptFrameworkService.findByName(frameworkName);

        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(frameworkName);
            softly.assertThat(response.getVersions().size()).isEqualTo(2);
        });

        verify(javaScriptFrameworkRepository, times(1)).findByName(frameworkName);
    }

    @Test
    void findByName_NotFound() {
        final String frameworkName = "Super framework";

        when(javaScriptFrameworkRepository.findByName(frameworkName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            javaScriptFrameworkService.findByName(frameworkName);
        });

        verify(javaScriptFrameworkRepository, times(1)).findByName(frameworkName);
    }

    @Test
    void findByNameAndVersion_Found() {
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(frameworkName, frameworkVersion)).thenReturn(Optional.of(getJavaScriptFrameworkVersion()));

        var response = javaScriptFrameworkService.findByNameAndVersion(frameworkName, frameworkVersion);

        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(frameworkName);
            softly.assertThat(response.getVersion()).isEqualTo(frameworkVersion);
        });

        verify(javaScriptFrameworkVersionRepository, times(1)).findByFrameworkNameAndVersion(frameworkName, frameworkVersion);
    }

    @Test
    void findByNameAndVersion_NotFound() {
        final String frameworkName = "Unknown framework";
        final String frameworkVersion = "3.0.1";

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(frameworkName, frameworkVersion)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            javaScriptFrameworkService.findByNameAndVersion(frameworkName, frameworkVersion);
        });

        verify(javaScriptFrameworkVersionRepository, times(1)).findByFrameworkNameAndVersion(frameworkName, frameworkVersion);
    }

    @Test
    void create_OK() {
        // given
        var request = getJavaScriptFrameworkPlainRequestBuilder().build();

        var javaScriptFrameworkVersion = getJavaScriptFrameworkVersion();

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(request.getName(), request.getVersion()))
                .thenReturn(Optional.empty());

        when(javaScriptFrameworkRepository.findByName(request.getName())).thenReturn(Optional.empty());

        when(javaScriptFrameworkVersionRepository.save(any())).thenReturn(javaScriptFrameworkVersion);

        // when
        var response = javaScriptFrameworkService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getName()).isEqualTo("Super framework");
            softly.assertThat(response.getVersions().size()).isEqualTo(1);
            softly.assertThat(response.getVersions().get(0).getHypeLevel()).isEqualTo(8);
        });

        verify(javaScriptFrameworkVersionRepository, times(1)).findByFrameworkNameAndVersion(request.getName(), request.getVersion());
        verify(javaScriptFrameworkRepository, times(1)).findByName(request.getName());
    }

    @Test
    void create_Conflict() {
        var request = getJavaScriptFrameworkPlainRequestBuilder().build();

        var javaScriptFrameworkVersion = getJavaScriptFrameworkVersion();

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(request.getName(), request.getVersion()))
                .thenReturn(Optional.of(javaScriptFrameworkVersion));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            javaScriptFrameworkService.create(request);
        });

        verify(javaScriptFrameworkVersionRepository, times(1)).findByFrameworkNameAndVersion(request.getName(), request.getVersion());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Name definitely longer than 15 characters"})
    public void create_NotValidName(String name) {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .name(name)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.create(requestData);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"56", "xxx", "1.0", "1..0", "best version"})
    public void create_NotValidVersion(String version) {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .version(version)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.create(requestData);
        });
    }

    @Test
    public void create_NotValidDeprecationDate() {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .deprecationDate(null)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.create(requestData);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11, 42, 666})
    public void create_NotValidHypeLevel(int hypeLevel) {
        var requestData = getJavaScriptFrameworkPlainRequestBuilder()
                .hypeLevel(hypeLevel)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.create(requestData);
        });
    }

    @Test
    void updateDeprecationDateAndHypeLevel_OK() {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";
        var request = getJavaScriptFrameworkDataPatchRequestBuilder()
                .deprecationDate(LocalDate.of(2030, 1, 1).toString())
                .hypeLevel(5)
                .build();

        var javaScriptFrameworkVersion = getJavaScriptFrameworkVersion();

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(frameworkName, frameworkVersion))
                .thenReturn(Optional.of(javaScriptFrameworkVersion));

        // when
        var response = javaScriptFrameworkService
                .updateDeprecationDateAndHypeLevel(frameworkName, frameworkVersion, request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(frameworkName);
            softly.assertThat(response.getVersions().size()).isEqualTo(1);
            softly.assertThat(response.getVersions().get(0).getDeprecationDate()).isEqualTo(LocalDate.of(2030, 1, 1).toString());
            softly.assertThat(response.getVersions().get(0).getHypeLevel()).isEqualTo(5);
        });
    }

    @Test
    void updateDeprecationDateAndHypeLevel_NotFound() {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";
        var request = getJavaScriptFrameworkDataPatchRequestBuilder()
                .deprecationDate(LocalDate.of(2030, 1, 1).toString())
                .hypeLevel(5)
                .build();

        // when - then
        assertThrows(ResourceNotFoundException.class, () -> {
            javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(frameworkName, frameworkVersion, request);
        });
    }

    @Test
    void updateDeprecationDateAndHypeLevel_NotValidDeprecationDate() {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";
        var request = getJavaScriptFrameworkDataPatchRequestBuilder()
                .deprecationDate(LocalDate.of(2030, 1, 1).toString())
                .deprecationDate(null)
                .build();

        // when - then
        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(frameworkName, frameworkVersion, request);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11, 42, 666})
    void updateDeprecationDateAndHypeLevel_NotValidHypeLevel(int hypeLevel) {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";
        var request = getJavaScriptFrameworkDataPatchRequestBuilder()
                .deprecationDate(LocalDate.of(2030, 1, 1).toString())
                .hypeLevel(hypeLevel)
                .build();

        // when - then
        assertThrows(ConstraintViolationException.class, () -> {
            javaScriptFrameworkService.updateDeprecationDateAndHypeLevel(frameworkName, frameworkVersion, request);
        });
    }

    @Test
    void deleteByName_OK() {
        // given
        final String frameworkName = "Super framework";

        when(javaScriptFrameworkRepository.findByName(frameworkName)).thenReturn(Optional.of(getJavaScriptFramework()));

        // when - then
        javaScriptFrameworkService.deleteByName(frameworkName);
    }

    @Test
    void deleteByName_NotFound() {
        // given
        final String frameworkName = "Super framework";

        when(javaScriptFrameworkRepository.findByName(frameworkName)).thenReturn(Optional.empty());

        // when - then
        assertThrows(ResourceNotFoundException.class, () -> {
            javaScriptFrameworkService.deleteByName(frameworkName);
        });
    }

    @Test
    void deleteByNameAndVersion_OK() {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";

        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(frameworkName,frameworkVersion))
                .thenReturn(Optional.of(getJavaScriptFrameworkVersion()));

        // when - then
        javaScriptFrameworkService.deleteByNameAndVersion(frameworkName, frameworkVersion);
    }

    @Test
    void deleteByNameAndVersion_NotFound() {
        // given
        final String frameworkName = "Super framework";
        final String frameworkVersion = "2.0.1";


        when(javaScriptFrameworkVersionRepository.findByFrameworkNameAndVersion(frameworkName, frameworkVersion))
                .thenReturn(Optional.empty());

        // when - then
        assertThrows(ResourceNotFoundException.class, () -> {
            javaScriptFrameworkService.deleteByNameAndVersion(frameworkName, frameworkVersion);
        });
    }
}
