package com.etnetera.hr.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class JavaScriptFrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "framework_id")
    private JavaScriptFramework framework;

    private String version;

    private LocalDate deprecationDate;

    private int hypeLevel;
}
