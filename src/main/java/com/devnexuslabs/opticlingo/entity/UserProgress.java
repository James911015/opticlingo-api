package com.devnexuslabs.opticlingo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // Genera Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // Constructor vacío (Obligatorio para JPA)
@AllArgsConstructor // Constructor con todos los campos
@Builder
@Entity
@Table(name = "user_progress", schema = "core", uniqueConstraints = {
        // Mapeamos la restricción única de la base de datos (Usuario + Concepto +
        // Idioma)
        @UniqueConstraint(columnNames = { "user_id", "concept_id", "target_lang_code" })
})
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // Relación con el Concepto (La idea/imagen)
    // FetchType.LAZY es mejor para rendimiento (no trae el concepto si no lo pides)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concept_id", nullable = false)
    @ToString.Exclude // Evita bucles infinitos al imprimir el objeto en logs
    private Concept concept;

    @Column(name = "target_lang_code", nullable = false, length = 10)
    private String targetLangCode;

    // Sistema Leitner (Cajas 1 a 5)
    @Column(name = "box_level", nullable = false)
    @Builder.Default
    private Integer boxLevel = 1;

    @CreationTimestamp
    @Column(name = "last_reviewed_at")
    @Builder.Default
    private LocalDateTime lastReviewedAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "next_review_at")
    @Builder.Default
    private LocalDateTime nextReviewAt = LocalDateTime.now();
}