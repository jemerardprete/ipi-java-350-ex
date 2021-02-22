package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService = new EmployeService();

    @Autowired
    private EmployeRepository employeRepository;

    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        // Given (pas d'employés en base)
        String nom = "Doe";
        String prenom = "Prenom";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        // When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        // Then
        List<Employe> employes = employeRepository.findAll();
        Assertions.assertThat(employes).hasSize(1);
        Employe employe = employeRepository.findAll().get(0);
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    // 4. Tester de manière intégrée un cas nominal de la méthode précédente //

    @Test
    public void testCalculPerformanceCommercial() throws EmployeException {
        // Given
        String nom = "Gauthier";
        String prenom = "Jean";
        String matricule = "C00001";
        LocalDate dateEmbauche = LocalDate.now().minusYears(8);
        Double salaire = Entreprise.SALAIRE_BASE;
        Integer performance = 3;
        Double tempsPartiel = 1.0;

        Long objectifCa = 10000L;
        Long caTraite = 20000L;
        employeRepository.save(new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel));

        // When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        // Then
        Employe employe = employeRepository.findByMatricule(matricule);
        Assertions.assertThat(employe.getPerformance()).isEqualTo(8);
    }

}
