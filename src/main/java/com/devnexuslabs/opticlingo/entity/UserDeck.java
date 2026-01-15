package com.devnexuslabs.opticlingo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_decks", schema = "core")
@Data
public class UserDeck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "source_lang", nullable = false)
    private Languaje sourceLanguage;

    @ManyToOne
    @JoinColumn(name = "target_lang", nullable = false)
    private Languaje targetLanguage;

    @Column(length = 100)
    private String title;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
