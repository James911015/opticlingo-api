package com.devnexuslabs.opticlingo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devnexuslabs.opticlingo.entity.Languaje;
import com.devnexuslabs.opticlingo.entity.UserDeck;
import com.devnexuslabs.opticlingo.repository.FlashcardProjection;
import com.devnexuslabs.opticlingo.repository.LanguageRepository;
import com.devnexuslabs.opticlingo.repository.TranslationRepository;
import com.devnexuslabs.opticlingo.repository.UserDeckRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final TranslationRepository translationRepository;
    private final UserDeckRepository userDeckRepository;
    private final LanguageRepository languageRepository;

    /**
     * Obtiene la lista de cartas para estudiar entre dos idiomas.
     * Ejemplo: source="es", target="en" -> Devuelve Manzana + Apple
     */
    public List<FlashcardProjection> getFlashcardsForSession(String sourceLang, String targetLang) {
        // Aquí podrías agregar validaciones (ej: verificar si los idiomas existen)
        return translationRepository.findFlashcards(sourceLang, targetLang);
    }

    /**
     * Crea un nuevo mazo para un usuario.
     * Ej: El usuario quiere empezar un curso de Francés desde Español.
     */
    @Transactional
    public UserDeck createUserDeck(String userId, String sourceCode, String targetCode) {
        // 1. Buscamos las entidades de idioma completas
        Languaje source = languageRepository.findById(sourceCode)
                .orElseThrow(() -> new RuntimeException("Idioma origen no encontrado: " + sourceCode));

        Languaje target = languageRepository.findById(targetCode)
                .orElseThrow(() -> new RuntimeException("Idioma destino no encontrado: " + targetCode));

        // 2. Creamos el mazo
        UserDeck newDeck = new UserDeck();
        newDeck.setUserId(userId);
        newDeck.setSourceLanguage(source);
        newDeck.setTargetLanguage(target);
        newDeck.setTitle("Curso de " + target.getName() + " desde " + source.getName());

        return userDeckRepository.save(newDeck);
    }

    // Obtener los mazos de un usuario
    public List<UserDeck> getUserDecks(String userId) {
        return userDeckRepository.findByUserId(userId);
    }
}