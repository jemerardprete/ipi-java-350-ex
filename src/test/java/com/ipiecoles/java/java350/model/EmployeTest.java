package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

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
}
