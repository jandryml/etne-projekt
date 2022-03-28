package com.etnetera.hr.data;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class JavaScriptFrameworkVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "framework_id")
    private JavaScriptFramework framework;

    private String version;

    private LocalDate deprecationDate;

    private int hypeLevel;
}
