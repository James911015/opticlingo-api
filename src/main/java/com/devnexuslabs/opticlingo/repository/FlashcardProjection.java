package com.devnexuslabs.opticlingo.repository;

public interface FlashcardProjection {
    String getImagePath();

    String getSourceWord(); // Pregunta (Español)

    String getTargetWord(); // Respuesta (Inglés)

    String getPhonetic(); // Pronunciación
}
