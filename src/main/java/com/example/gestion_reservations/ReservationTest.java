package com.example.gestion_reservations;

import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory(
                "gestion-reservations-pu"
        );
    }

    @AfterAll
    static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void testCreateReservation() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Créer un utilisateur
            Utilisateur user = new Utilisateur(
                    "Test User",
                    "test.user@example.com"
            );
            em.persist(user);

            // 2. Créer une salle
            Salle salle = new Salle(
                    "Salle Test",
                    20,
                    "Premier étage"
            );
            em.persist(salle);

            // 3. Créer la réservation
            Reservation reservation = new Reservation(
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusHours(1),
                    "CONFIRMEE"
            );

            reservation.setUtilisateur(user);
            reservation.setSalle(salle);

            em.persist(reservation);

            // 4. Valider la transaction
            em.getTransaction().commit();

            // 5. Vérifier que la réservation a été créée
            assertNotNull(
                    reservation.getId(),
                    "L'identifiant de la réservation doit exister"
            );

            System.out.println(
                    "Test réussi : réservation créée, ID = "
                            + reservation.getId()
            );

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
