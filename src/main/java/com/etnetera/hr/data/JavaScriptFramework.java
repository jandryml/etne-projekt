package com.etnetera.hr.data;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToMany(mappedBy = "framework")
	private List<JavaScriptFrameworkVersion> versions;

	public JavaScriptFramework(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "JavaScriptFramework [id=" + id + ", name=" + name + "]";
	}
}
