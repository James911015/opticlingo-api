package com.devnexuslabs.opticlingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devnexuslabs.opticlingo.entity.Languaje;

@Repository
public interface LanguageRepository extends JpaRepository<Languaje, String> {
    // Ya incluye métodos como findAll(), findById(), save(), etc.
}
