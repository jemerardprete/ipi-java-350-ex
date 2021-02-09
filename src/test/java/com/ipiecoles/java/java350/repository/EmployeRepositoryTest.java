package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.Java350Application;
import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

// Tester des classes gérées par Spring : Le contexte Spring doit être lancé en même temps que le test (utilisé aussi dans EmployeServiceTest
// De ce fait, l'annotation Autowired fonctionnera

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Java350Application.class})*/
//@DataJpaTest
@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    // Test avec Spring
    @Test
    public void testFindLastMatricule0Employe() {
        // Given

        // When
        String lastMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(lastMatricule).isNull();
    }

    // Test avec Spring : Vérifier que c'est bien le matricule de l'employé qui est envoyé
    @Test
    public void testFindLastMatricule1Employe() {
        // Given
        // Insérer des données en base
        employeRepository.save(new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0));

        // When
        // Executer des requêtes en BDD
        String lastMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void textFindLastMatriculeNEmploye() {
        // Given
        employeRepository.save(new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "M40325",
                LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jim", "C06432",
                LocalDate.now(), 1500d, 1, 1.0));

        // When
        String lastMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(lastMatricule).isEqualTo("40325");
    }

    // Réinitialisation du contexte avant l'exécution du test : Vider la base de données :
    @BeforeEach
    @AfterEach
    public void purgeBdd() {
        employeRepository.deleteAll();
    }

}
