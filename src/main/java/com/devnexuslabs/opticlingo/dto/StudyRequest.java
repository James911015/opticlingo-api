package com.devnexuslabs.opticlingo.dto;

import lombok.Data;

@Data
public class StudyRequest {
    // El ID de la traducción/palabra que se mostró en pantalla
    private Long flashcardId;

    // Si el usuario dijo "I knew it" (true) o "I forgot" (false)
    private Boolean isCorrect;

    // Opcional: Para pruebas. En el futuro esto sale del Token de seguridad.
    private String userId;
}
