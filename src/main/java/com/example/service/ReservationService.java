
package com.example.service;

import com.example.model.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationService {

    private static final Logger LOGGER =
            Logger.getLogger(ReservationService.class.getName());

    private final EntityManager em;

    public ReservationService(EntityManager em) {
        this.em = em;
    }

    public void createReservation(Reservation reservation) {

        EntityTransaction tx = em.getTransaction();

        try {
            LOGGER.info("Début de la création d'une réservation.");

            tx.begin();

            em.persist(reservation);

            tx.commit();

            LOGGER.info(
                    "Réservation créée avec succès. ID = "
                            + reservation.getId()
            );

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            LOGGER.log(
                    Level.SEVERE,
                    "Erreur lors de la création de la réservation.",
                    e
            );

            throw new RuntimeException(
                    "Impossible de créer la réservation.",
                    e
            );
        }
    }
}
