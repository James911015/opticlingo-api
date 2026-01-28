package com.devnexuslabs.opticlingo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devnexuslabs.opticlingo.dto.StudyRequest;
import com.devnexuslabs.opticlingo.service.StudyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgressController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<String> logStudySession(@RequestBody StudyRequest request) {
        // Validaciones básicas
        if (request.getFlashcardId() == null || request.getIsCorrect() == null) {
            return ResponseEntity.badRequest().body("Error: Faltan datos (flashcardId o isCorrect)");
        }

        try {
            studyService.logProgress(request);
            return ResponseEntity.ok("Progreso actualizado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al guardar: " + e.getMessage());
        }
    }
}
