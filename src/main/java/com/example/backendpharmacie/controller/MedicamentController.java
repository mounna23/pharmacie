package com.example.backendpharmacie.controller;

import com.example.backendpharmacie.model.Medicament;
import com.example.backendpharmacie.repository.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class MedicamentController {

    @Autowired
    private MedicamentRepository medicamentRepository;

    private final String storageDirectoryPath = Paths.get("uploaded-images").toAbsolutePath().toString();

    // POST avec multipart/form-data (incluant qteStock)
    @PostMapping(value = "/medicament", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Medicament newMedicamentWithFile(
            @RequestParam("codeMed") String codeMed,
            @RequestParam("libelle") String libelle,
            @RequestParam("dateExpiration") String dateExpiration,
            @RequestParam(value = "prixUnitaire", required = false) Double prixUnitaire,
            @RequestParam(value = "stockMin", required = false) Integer stockMin,
            @RequestParam("qteStock") Integer qteStock,
            @RequestParam("familleMed") String familleMed,
            @RequestParam("image") MultipartFile file) throws IOException {  // Nouveau paramètre

        String baseUrl = "uploaded-images/";
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        Path storageDirectory = Paths.get(storageDirectoryPath);
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }

        Path destinationPath = storageDirectory.resolve(filename);
        file.transferTo(destinationPath);

        Medicament medicament = new Medicament();
        medicament.setCodeMed(codeMed);
        medicament.setLibelle(libelle);
        medicament.setDateExpiration(java.sql.Date.valueOf(dateExpiration));
        medicament.setPrixUnitaire(prixUnitaire);
        medicament.setStockMin(stockMin);
        medicament.setQteStock(qteStock);
        medicament.setFamilleMed(familleMed);
        medicament.setImage(baseUrl + filename);

        return medicamentRepository.save(medicament);
    }

    // POST avec JSON (utilise directement l'entité Medicament)
    @PostMapping(value = "/medicament", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Medicament newMedicamentWithJson(@RequestBody Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    @GetMapping("/medicaments")
    public List<Medicament> getAllMedicaments() {
        return medicamentRepository.findAll();
    }

    @GetMapping("/medicament/{id}")
    public Optional<Medicament> getMedicamentById(@PathVariable Long id) {
        return medicamentRepository.findById(id);
    }

    @PutMapping("/medicament/{id}")
    public Optional<Medicament> updateMedicament(@RequestBody Medicament newMedicament, @PathVariable Long id) {
        return medicamentRepository.findById(id).map(medicament -> {
            if (newMedicament.getCodeMed() != null) medicament.setCodeMed(newMedicament.getCodeMed());
            if (newMedicament.getLibelle() != null) medicament.setLibelle(newMedicament.getLibelle());
            if (newMedicament.getDateExpiration() != null) medicament.setDateExpiration(newMedicament.getDateExpiration());
            if (newMedicament.getPrixUnitaire() != null) medicament.setPrixUnitaire(newMedicament.getPrixUnitaire());
            if (newMedicament.getStockMin() != null) medicament.setStockMin(newMedicament.getStockMin());
            if (newMedicament.getQteStock() != null) medicament.setQteStock(newMedicament.getQteStock());
            if (newMedicament.getFamilleMed() != null) medicament.setFamilleMed(newMedicament.getFamilleMed());
            if (newMedicament.getImage() != null) medicament.setImage(newMedicament.getImage());

            return medicamentRepository.save(medicament);
        });
    }

    @DeleteMapping("/medicament/{id}")
    public String deleteMedicament(@PathVariable Long id) {
        if (!medicamentRepository.existsById(id)) {
            return "Médicament introuvable";
        }
        medicamentRepository.deleteById(id);
        return "Médicament avec id " + id + " a été supprimé";
    }
}