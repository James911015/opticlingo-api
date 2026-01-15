package com.devnexuslabs.opticlingo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "languages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Languaje {

    @Id
    @Column(name = "code", nullable = false, length = 10)
    private String code; // e.g., "en", "es", "fr"

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "flag_icon")
    private String flagIcon;

}
