package com.example.gestion_reservations;

import com.example.util.DataInitializer;
import com.example.util.PerformanceReport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MainApp {

    public static void main(String[] args) {

        EntityManagerFactory emf = null;

        try {
            // Initialiser Hibernate et la base de données
            emf = Persistence.createEntityManagerFactory(
                    "gestion-reservations-pu"
            );

            System.out.println("Application démarrée.");

            // Initialiser les données
            EntityManager em = emf.createEntityManager();

            try {
                DataInitializer initializer =
                        new DataInitializer(em);

                initializer.initData();

                System.out.println(
                        "Initialisation des données terminée."
                );

            } finally {
                em.close();
            }

            // Générer le rapport de performance
            PerformanceReport report =
                    new PerformanceReport(emf);

            report.printReport();

            System.out.println(
                    "Exécution de l'application terminée."
            );

        } catch (Exception e) {
            System.err.println(
                    "Erreur pendant l'exécution de l'application."
            );
            e.printStackTrace();

        } finally {
            // Fermer les ressources
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}