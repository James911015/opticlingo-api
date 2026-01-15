package com.devnexuslabs.opticlingo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "translations", schema = "core", uniqueConstraints = @UniqueConstraint(columnNames = { "concept_id",
        "language_code" }))
@Data
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Concept (Muchas traducciones -> Un concepto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concept_id", nullable = false)
    private Concept concept;

    // Relación con Languaje (Muchas traducciones -> Un lenguaje)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_code", nullable = false)
    private Languaje language;

    @Column(name = "word_text", nullable = false, length = 150)
    private String wordText;

    @Column(name = "phonetic", nullable = false, length = 150)
    private String phonetic;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(columnDefinition = "TEXT")
    private String description;
}
