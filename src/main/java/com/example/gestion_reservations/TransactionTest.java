package com.example.gestion_reservations;

import com.example.model.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class TransactionTest {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(
                        "gestion-reservations-pu"
                );

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // Commencer la transaction
            tx.begin();

            // Rechercher une réservation existante
            Reservation reservation =
                    em.createQuery(
                            "SELECT r FROM Reservation r",
                            Reservation.class
                    )
                    .setMaxResults(1)
                    .getSingleResult();

            // Modifier le statut
            reservation.setStatut("CONFIRMEE");

            // Valider la transaction
            tx.commit();

            System.out.println(
                    "Transaction validée avec succès."
            );
            System.out.println(
                    "Statut : " + reservation.getStatut()
            );

        } catch (Exception e) {

            // Annuler la transaction en cas d'erreur
            if (tx.isActive()) {
                tx.rollback();
            }

            System.err.println(
                    "Transaction annulée : " + e.getMessage()
            );

        } finally {
            em.close();
            emf.close();
        }
    }
}