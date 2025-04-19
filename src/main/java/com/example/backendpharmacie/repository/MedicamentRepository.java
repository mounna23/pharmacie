package com.example.backendpharmacie.repository;

import com.example.backendpharmacie.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    @Query("SELECT m FROM Medicament m WHERE LOWER(m.libelle) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Medicament> findByLibelleContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    // Méthodes supplémentaires si nécessaire
    Medicament findByCodeMed(String codeMed);
}