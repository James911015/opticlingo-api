package com.devnexuslabs.opticlingo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devnexuslabs.opticlingo.dto.StudyRequest;
import com.devnexuslabs.opticlingo.entity.Languaje;
import com.devnexuslabs.opticlingo.entity.Translation;
import com.devnexuslabs.opticlingo.entity.UserDeck;
import com.devnexuslabs.opticlingo.entity.UserProgress;
import com.devnexuslabs.opticlingo.repository.FlashcardProjection;
import com.devnexuslabs.opticlingo.repository.LanguageRepository;
import com.devnexuslabs.opticlingo.repository.TranslationRepository;
import com.devnexuslabs.opticlingo.repository.UserDeckRepository;
import com.devnexuslabs.opticlingo.repository.UserProgressRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final TranslationRepository translationRepository;
    private final UserDeckRepository userDeckRepository;
    private final LanguageRepository languageRepository;
    private final UserProgressRepository progressRepository;

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

    @Transactional
    public void logProgress(StudyRequest request) {
        // 1. Paso CRUCIAL: Convertir FlashcardId -> Concepto e Idioma
        // El usuario vio la carta "Apple", necesitamos saber que eso es el Concepto #1
        // (Manzana)
        Translation translation = translationRepository.findById(request.getFlashcardId())
                .orElseThrow(() -> new RuntimeException("Flashcard no encontrada ID: " + request.getFlashcardId()));

        // 2. Definir usuario (Por defecto 'james' para tus pruebas)
        String userId = (request.getUserId() != null && !request.getUserId().isEmpty())
                ? request.getUserId()
                : "james_arias";

        // 3. Buscar si ya existe progreso para este CONCEPTO en este IDIOMA
        UserProgress progress = progressRepository.findByUserIdAndConceptIdAndTargetLangCode(
                userId,
                translation.getConcept().getId(), // Sacamos el ID del Concepto desde la Flashcard
                translation.getLanguage().getCode() // Sacamos el código de idioma ('en')
        ).orElseGet(() -> {
            // Si es la primera vez que la ve, creamos registro nuevo
            return UserProgress.builder()
                    .userId(userId)
                    .concept(translation.getConcept())
                    .targetLangCode(translation.getLanguage().getCode())
                    .boxLevel(1)
                    .lastReviewedAt(LocalDateTime.now())
                    .nextReviewAt(LocalDateTime.now())
                    .build();
        });

        // 4. Aplicar Algoritmo de Repetición Espaciada (Leitner)
        updateLeitnerAlgo(progress, request.getIsCorrect());

        // 5. Guardar cambios
        progressRepository.save(progress);
    }

    private void updateLeitnerAlgo(UserProgress progress, boolean isCorrect) {
        progress.setLastReviewedAt(LocalDateTime.now());

        if (isCorrect) {
            // ACIERTO: Subir de nivel (Caja) hasta máximo 5
            int currentLevel = progress.getBoxLevel();
            int newLevel = Math.min(currentLevel + 1, 5);
            progress.setBoxLevel(newLevel);

            // Calcular próxima revisión sumando días según el nivel
            long daysToAdd = getDaysForLevel(newLevel);
            progress.setNextReviewAt(LocalDateTime.now().plusDays(daysToAdd));
        } else {
            // FALLO: Regresar a la Caja 1 (Castigo: Repasar mañana)
            progress.setBoxLevel(1);
            progress.setNextReviewAt(LocalDateTime.now().plusDays(1));
        }
    }

    // Configuración de intervalos (en días)
    private long getDaysForLevel(int level) {
        return switch (level) {
            case 1 -> 1; // Mañana
            case 2 -> 3; // 3 días
            case 3 -> 7; // 1 semana
            case 4 -> 14; // 2 semanas
            case 5 -> 30; // 1 mes
            default -> 1;
        };
    }
}