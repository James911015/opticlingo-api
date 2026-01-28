package com.devnexuslabs.opticlingo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devnexuslabs.opticlingo.entity.UserProgress;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    // BÚSQUEDA CLAVE:
    // Necesitamos encontrar si James ya tiene progreso para la "Manzana"
    // (conceptId)
    // en "Inglés" (langCode).
    Optional<UserProgress> findByUserIdAndConceptIdAndTargetLangCode(
            String userId,
            Long conceptId,
            String targetLangCode);
}