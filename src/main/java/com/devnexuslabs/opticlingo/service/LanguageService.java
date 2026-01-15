package com.devnexuslabs.opticlingo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devnexuslabs.opticlingo.entity.Languaje;
import com.devnexuslabs.opticlingo.repository.LanguageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Inyección de dependencias automática (Lombok)
public class LanguageService {

    private final LanguageRepository languageRepository;

    // Obtener todos los idiomas disponibles (para el dropdown en Flutter)
    public List<Languaje> getAllLanguages() {
        return languageRepository.findAll();
    }

    // Validar si un código de idioma existe
    public boolean languageExists(String code) {
        return languageRepository.existsById(code);
    }
}
