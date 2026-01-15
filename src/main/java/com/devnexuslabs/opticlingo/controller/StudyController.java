package com.devnexuslabs.opticlingo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devnexuslabs.opticlingo.entity.Languaje;
import com.devnexuslabs.opticlingo.entity.UserDeck;
import com.devnexuslabs.opticlingo.repository.FlashcardProjection;
import com.devnexuslabs.opticlingo.service.LanguageService;
import com.devnexuslabs.opticlingo.service.StudyService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api") // Todas las rutas empezarán con /api
@CrossOrigin(origins = "*") // ¡IMPORTANTE! Permite que Flutter (Web/Móvil) se conecte sin bloqueos
@RequiredArgsConstructor
public class StudyController {

    private final LanguageService languageService;
    private final StudyService studyService;

    // --- ENDPOINTS DE IDIOMAS ---

    // GET /api/languages
    // Para llenar el selector de banderas en Flutter
    @GetMapping("/languages")
    public ResponseEntity<List<Languaje>> getLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    // --- ENDPOINTS DE MAZOS (DECKS) ---

    // GET /api/decks/{userId}
    // Ver los cursos que tiene inscritos un usuario
    @GetMapping("/decks/{userId}")
    public ResponseEntity<List<UserDeck>> getUserDecks(@PathVariable String userId) {
        return ResponseEntity.ok(studyService.getUserDecks(userId));
    }

    // POST /api/decks
    // Crear un nuevo curso (ej: Un usuario quiere aprender Inglés desde Español)
    @PostMapping("/decks")
    public ResponseEntity<UserDeck> createDeck(@RequestBody CreateDeckRequest request) {
        UserDeck newDeck = studyService.createUserDeck(
                request.getUserId(),
                request.getSourceLang(),
                request.getTargetLang());
        return ResponseEntity.ok(newDeck);
    }

    // --- ENDPOINT DE ESTUDIO (EL MÁS IMPORTANTE) ---

    // GET /api/flashcards?source=es&target=en
    // Trae las tarjetas listas para jugar
    @GetMapping("/flashcards")
    public ResponseEntity<List<FlashcardProjection>> getFlashcards(
            @RequestParam String source, // ej: "es"
            @RequestParam String target // ej: "en"
    ) {
        List<FlashcardProjection> cards = studyService.getFlashcardsForSession(source, target);
        return ResponseEntity.ok(cards);
    }

    // --- DTOs (Data Transfer Objects) ---
    // Clase auxiliar pequeña para recibir los datos del POST
    @Data
    public static class CreateDeckRequest {
        private String userId;
        private String sourceLang; // código, ej: "es"
        private String targetLang; // código, ej: "en"
    }
}
