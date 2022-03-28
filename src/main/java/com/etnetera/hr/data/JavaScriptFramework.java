package com.etnetera.hr.data;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 *
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@OneToMany(mappedBy = "framework", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JavaScriptFrameworkVersion> versions = new ArrayList<>();

	@Override
	public String toString() {
		return "JavaScriptFramework [id=" + id + ", name=" + name + "]";
	}
}
