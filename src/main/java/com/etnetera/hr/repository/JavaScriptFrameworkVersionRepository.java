package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring data repository interface used for accessing the data in database.
 * 
 * @author Etnetera
 *
 */
public interface JavaScriptFrameworkVersionRepository extends CrudRepository<JavaScriptFrameworkVersion, Long> {

    Optional<JavaScriptFrameworkVersion> findByFrameworkNameAndVersion(String name, String version);
}
