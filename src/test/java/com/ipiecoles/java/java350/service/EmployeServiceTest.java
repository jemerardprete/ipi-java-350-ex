package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test d'intégration : Appel de Repository

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        // Given (pas d'employés en base)
        String nom = "Doe";
        String prenom = "Prenom";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        // Simuler que la recherche par matricule ne renvoie pas de résultats
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);

        // When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        // Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    @Test
    public void testEmbaucheLimiteMatricule() {
        // Given
        String nom = "Doe";
        String prenom = "Prenom";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        // Simuler qu'il y a 99999 employés en base (ou du moins que le matricule le plus haut est X99999)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        // When
        try {
          employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
          Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (EmployeException e) {
            // Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
            Mockito.verify(employeRepository, Mockito.never()).save(Mockito.any(Employe.class));
        }

    }

    @Test
    public void testEmbaucheEmployeExsiteDeja() {
        // Given
        String nom = "Doe";
        String prenom = "Prenom";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);

        // Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        // Simuler que la recherche par matricule renvoie un employe (un employe a été embauché entre temps)
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        // When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }

    }

    /* EVALUATION */

    // 3. Tester sans dépendance à la BDD la méthode calculPerformanceCommercial //

    // Test Unitaire (paramétré) : calculPerformanceCommercial :
    @ParameterizedTest(name = "caTraite {0} => performance {1} ")
    @CsvSource({
            "1000, 1",
            "9000, 4",
            "9900, 6",
            "11000, 7",
            "20000, 10"
    })
    public void testCalculPerformanceCommercial(Long caTraite, Integer performance) throws EmployeException {
        // Given
        String nom = "Miro";
        String prenom = "Alexia";
        String matricule = "C00001";
        Long objectifCa = 10000L;

        // Simuler que la recherche par matricule renvoie un employe
        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(new Employe(nom, prenom, matricule, LocalDate.now(), 1500d, 5, 1.0));
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1.0);

        // When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        // Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getPerformance()).isEqualTo(performance);

    }

    // Test Unitaire : testCalculPerformanceCommercialCaTraiteNull
    @Test
    public void testCalculPerformanceCommercialCaTraiteNull() {
        // Given
        String matricule = "C00001";
        Long objectifCa = 10000L;
        Long caTraite = null;

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
    }

    // Test Unitaire : testCalculPerformanceCommercialObjectifCaNull
    @Test
    public void testCalculPerformanceCommercialObjectifCaNull() {
        // Given
        String matricule = "C00001";
        Long objectifCa = null;
        Long caTraite = 10000L;

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }

    // Test Unitaire : testCalculPerformanceCommercialObjectifCa0
    @Test
    public void testCalculPerformanceCommercialObjectifCaNegatif() {
        // Given
        String matricule = "C00001";
        Long objectifCa = -6L;
        Long caTraite = 10000L;

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
    }

    // Test Unitaire : testCalculPerformanceCommercialMatriculeIncorrect
    @Test
    public void testCalculPerformanceCommercialMatriculeIncorrect() {
        // Given
        String matricule = "T00001";
        Long objectifCa = 10000L;
        Long caTraite = 10000L;

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }

    // Test Unitaire : testCalculPerformanceCommercialMatriculeNull
    @Test
    public void testCalculPerformanceCommercialMatriculeNull() {
        // Given
        String matricule = null;
        Long objectifCa = 10000L;
        Long caTraite = 10000L;

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
    }

    // Test Unitaire : testCalculPerformanceCommercialEmployeNull
    @Test
    public void testCalculPerformanceCommercialEmployeNull() {
        //Given
        String matricule = "C00001";
        Long caTraite = 10000L;
        Long objectifCa = 10000L;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);

        // When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("calculPerformanceCommercial aurait dû lancer une exception");
        } catch (Exception e) {
            // Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule C00001 n'existe pas !");
        }


    }

}
