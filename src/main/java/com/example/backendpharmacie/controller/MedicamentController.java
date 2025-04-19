package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.service.MedicamentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentController {

    @Autowired
    private MedicamentService medicamentService;

    // Endpoint pour ajouter un médicament
    @PostMapping
    public ResponseEntity<Medicament> ajouterMedicament(@Valid @RequestBody Medicament medicament) {
        Medicament nouveauMedicament = medicamentService.ajouterMedicament(medicament);
        return ResponseEntity.ok(nouveauMedicament);
    }

    // Endpoint pour récupérer tous les médicaments
    @GetMapping
    public ResponseEntity<List<Medicament>> listerMedicaments() {
        List<Medicament> medicaments = medicamentService.listerMedicaments();
        return ResponseEntity.ok(medicaments);
    }

    // Endpoint pour récupérer un médicament par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> trouverMedicamentParId(@PathVariable Long id) {
        Optional<Medicament> optionalMedicament = medicamentService.trouverMedicamentParId(id);
        if (optionalMedicament.isPresent()) {
            return ResponseEntity.ok(optionalMedicament.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour mettre à jour un médicament
    @PutMapping("/{id}")
    public ResponseEntity<Medicament> mettreAJourMedicament(@PathVariable Long id, @Valid @RequestBody Medicament medicamentDetails) {
        Optional<Medicament> optionalMedicament = medicamentService.trouverMedicamentParId(id);
        if (optionalMedicament.isPresent()) {
            Medicament medicament = optionalMedicament.get();
            medicament.setCodeMed(medicamentDetails.getCodeMed());
            medicament.setLibelle(medicamentDetails.getLibelle());
            medicament.setDateExpiration(medicamentDetails.getDateExpiration());
            medicament.setPrixUnitaire(medicamentDetails.getPrixUnitaire());
            medicament.setStockMin(medicamentDetails.getStockMin());
            medicament.setFamilleMed(medicamentDetails.getFamilleMed());

            Medicament medicamentMiseAJour = medicamentService.mettreAJourMedicament(medicament);
            return ResponseEntity.ok(medicamentMiseAJour);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour supprimer un médicament
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMedicament(@PathVariable Long id) {
        Optional<Medicament> optionalMedicament = medicamentService.trouverMedicamentParId(id);
        if (optionalMedicament.isPresent()) {
            medicamentService.supprimerMedicament(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = "search")
    public ResponseEntity<List<Medicament>> rechercherMedicaments(@RequestParam String search) {
        List<Medicament> medicaments = medicamentService.rechercherMedicaments(search);
        return ResponseEntity.ok(medicaments);
    }
}