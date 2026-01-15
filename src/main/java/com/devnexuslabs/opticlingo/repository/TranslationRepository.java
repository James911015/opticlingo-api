package com.devnexuslabs.opticlingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devnexuslabs.opticlingo.entity.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * Esta consulta replica tu SQL de pgAdmin.
     * Cruza el concepto con la traducción origen (source) y la destino (target).
     */
    @Query(value = """
            SELECT
                c.image_path as imagePath,
                t_source.word_text as sourceWord,
                t_target.word_text as targetWord,
                t_target.phonetic as phonetic
            FROM core.concepts c
            JOIN core.translations t_source
                ON c.id = t_source.concept_id AND t_source.language_code = :sourceLang
            JOIN core.translations t_target
                ON c.id = t_target.concept_id AND t_target.language_code = :targetLang
            """, nativeQuery = true)
    List<FlashcardProjection> findFlashcards(@Param("sourceLang") String sourceLang,
            @Param("targetLang") String targetLang);
}