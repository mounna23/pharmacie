package com.example.backendpharmacie.repository;

import com.example.backendpharmacie.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u WHERE LOWER(u.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Utilisateur> findByNomOrPrenomContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
}


