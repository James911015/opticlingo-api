package com.devnexuslabs.opticlingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devnexuslabs.opticlingo.entity.Concept;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Long> {
    // Buscar conceptos por categoría (ej: "FOOD")
    List<Concept> findByCategory(String category);
}
