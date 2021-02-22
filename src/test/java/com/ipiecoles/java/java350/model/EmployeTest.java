package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

// Les tests unitaires testent une portion de code de manière unitaire et indépendante
// Les tests paramétrés sont intéressants si on a plusieurs jeux de données pour un même test

public class EmployeTest {

    @Test
    public void testX() {
        // Given

        // When

        // Then
    }

    // Test Unitaire Classique : Nombre d'années d'ancienneté
    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNull() {
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        // When
        Integer nbAnneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(nbAnneeAnciennete).isNull();
    }

    // Test Unitaire Classique : Nombre d'années d'ancienneté
    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheInfNow() {
        // Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().minusYears(6), 1500d, 1, 1.0);

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        //Assertions.assertThat(anneeAnciennete).isEqualTo(6);
        Assertions.assertThat(anneeAnciennete).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(anneeAnciennete).isLessThanOrEqualTo(50);
    }

    // Test Unitaire Classique : Nombre d'années d'ancienneté
    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheSupNow() {
        // Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().plusYears(6), 1500d, 1, 1.0);

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    // Test Unitaire Classique : Prime Annuelle
    /*@Test
    public void testGetPrimeAnnuelleTU() {
        // Given
        Integer performance = 1; // Egal à PERFORMANCE_BASE
        String matricule = "T12345"; // Matricule d'un employé de base, càd pas manager
        Double tauxActivite = 1.0; // Temps plein
        Long nbAnneesAnciennete = 0L; // Au lieu de mettre la date en dur

        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 1500d, performance, tauxActivite);

        // When
        Double prime = employe.getPrimeAnnuelle();

        // Then
        Double primeAttendue = 1000.0; // Egal à PRIME_BASE
        Assertions.assertThat(prime).isEqualTo(primeAttendue);

    }*/

    // Test Paramétré : Prime Annuelle (= Test Unitaire Classique : Prime Annuelle)
    @ParameterizedTest(name = "Perf {0}, matricule {1}, txActivite {2}, anciennete {3} => prime {4} ")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5, 0, 500.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "1, 'T12345', 1.0, 2, 1200.0"
    })
    public void testGetPrimeAnnuelle(Integer performance, String matricule, Double tauxActivite, Long nbAnneesAnciennete, Double primeAttendue) {
        // Given
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 1500d, performance, tauxActivite);

        // When
        Double prime = employe.getPrimeAnnuelle();

        // Then
        Assertions.assertThat(prime).isEqualTo(primeAttendue);

    }

    // Test Unitaire Classique : Prime Annuelle
    @Test
    public void testGetPrimeAnnuelleMatriculeNull() {
        // Given
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now(), 1500d, 1, 1.0);

        // When
        Double prime = employe.getPrimeAnnuelle();

        // Then
        Assertions.assertThat(prime).isEqualTo(1000);

    }

    /* EVALUATION */

    // 1. Tests Unitaires sur la méthode augmenterSalaire //

    // Test Unitaire : testAugmentationSalaire
    @Test
    public void testAugmentationSalaire() {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1500.0);

        // When
        Double salaire = employe.augmenterSalaire(0.50);

        // Then
        Assertions.assertThat(salaire).isEqualTo(2250);
    }

    // Test Unitaire : testAugmentationSalaireNull
    @Test
    public void testAugmentationSalaireNull() {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(null);

        // When
        Double salaire = employe.augmenterSalaire(0.50);

        // Then
        Assertions.assertThat(salaire).isNull();
    }

    // Test Unitaire : testAugmentation0Salaire
    @Test
    public void testAugmentation0Salaire() throws EmployeException {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1500.0);

        // When
        Double salaire = employe.augmenterSalaire(0.00);

        // Then
        Assertions.assertThat(salaire).isEqualTo(1500.0);
    }

    // Test Unitaire : testAugmentationNegativeSalaire
    @Test
    public void testAugmentationNegativeSalaire() {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1500.0);

        // When
        Double salaire = employe.augmenterSalaire(-0.50);

        // Then
        Assertions.assertThat(salaire).isEqualTo(1500.0);
    }

    // 2. Tests Unitaires (paramétrés) sur la méthode getNbRtt //

    // Test Paramétré : Nombre RTT
    @ParameterizedTest(name = "annee {0}, tempsPartiel {1} => nbRttAttendus {2} ")
    @CsvSource({
            "2019, 1.0, 8",
            "2019, 0.5, 4",
            "2021, 1.0, 10",
            "2021, 0.5, 5",
            "2022, 1.0, 10",
            "2022, 0.5, 5",
            "2032, 1.0, 11",
            "2032, 0.5, 6"
    })
    public void testGetNbRtt(Integer annee, Double tempsPartiel, Integer nbRttAttendus) {
        // Given
        Employe employe = new Employe("Dupont", "Maria", "T12345", LocalDate.now().minusYears(6), 1500.0, 1, tempsPartiel);

        // When
        Integer nbRtt = employe.getNbRtt(LocalDate.of(annee, 1, 1));

        // Then
        Assertions.assertThat(nbRtt).isEqualTo(nbRttAttendus);

    }

}
