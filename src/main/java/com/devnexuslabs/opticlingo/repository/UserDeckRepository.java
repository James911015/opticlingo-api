package com.devnexuslabs.opticlingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devnexuslabs.opticlingo.entity.UserDeck;

@Repository
public interface UserDeckRepository extends JpaRepository<UserDeck, Long> {
    // Buscar todos los mazos de un usuario
    List<UserDeck> findByUserId(String userId);
}
