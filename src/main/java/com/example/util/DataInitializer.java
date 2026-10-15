package com.example.util;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;

public class DataInitializer {

    private final EntityManager em;

    public DataInitializer(EntityManager em) {
        this.em = em;
    }

    public void initData() {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Création des utilisateurs
            Utilisateur user1 = new Utilisateur(
                    "Alice Dupont", "alice.dupont@example.com");
            Utilisateur user2 = new Utilisateur(
                    "Karim Benali", "karim.benali@example.com");
            Utilisateur user3 = new Utilisateur(
                    "Sara Amrani", "sara.amrani@example.com");

            em.persist(user1);
            em.persist(user2);
            em.persist(user3);

            // Création des salles
            Salle salle1 = new Salle(
                    "Salle A", 20, "Premier étage");
            Salle salle2 = new Salle(
                    "Salle B", 35, "Deuxième étage");
            Salle salle3 = new Salle(
                    "Salle C", 10, "Rez-de-chaussée");

            // Création des équipements
            Equipement projecteur = new Equipement("Projecteur");
            Equipement tableau = new Equipement("Tableau blanc");
            Equipement visio = new Equipement("Visioconférence");

            salle1.ajouterEquipement(projecteur);
            salle2.ajouterEquipement(tableau);
            salle3.ajouterEquipement(visio);

            em.persist(salle1);
            em.persist(salle2);
            em.persist(salle3);

            // Dates dynamiques
            LocalDateTime demain =
                    LocalDateTime.now().plusDays(1);

            LocalDateTime apresDemain =
                    LocalDateTime.now().plusDays(2);

            // Réservation d'Alice dans la salle A
            Reservation res1 = new Reservation(
                    demain.withHour(9).withMinute(0).withSecond(0).withNano(0),
                    demain.withHour(11).withMinute(0).withSecond(0).withNano(0),
                    "CONFIRMEE"
            );
            salle1.ajouterReservation(res1);
            user1.ajouterReservation(res1);

            // Réservation de Karim dans la salle B
            Reservation res2 = new Reservation(
                    demain.withHour(14).withMinute(0).withSecond(0).withNano(0),
                    demain.withHour(16).withMinute(0).withSecond(0).withNano(0),
                    "EN_ATTENTE"
            );
            salle2.ajouterReservation(res2);
            user2.ajouterReservation(res2);

            // Réservation de Sara dans la salle C
            Reservation res3 = new Reservation(
                    apresDemain.withHour(10).withMinute(0).withSecond(0).withNano(0),
                    apresDemain.withHour(12).withMinute(0).withSecond(0).withNano(0),
                    "CONFIRMEE"
            );
            salle3.ajouterReservation(res3);
            user3.ajouterReservation(res3);

            // Enregistrement des utilisateurs et de leurs réservations
            // Les réservations sont persistées par cascade depuis Utilisateur.
            tx.commit();

            System.out.println("Jeu de données créé avec succès !");
            System.out.println("3 utilisateurs, 3 salles, 3 équipements, 3 réservations.");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(
                    "Erreur pendant l'initialisation des données.", e);
        }
    }
}
