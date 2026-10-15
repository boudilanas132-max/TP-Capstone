package com.example.gestion_reservations;

import com.example.model.Reservation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

public class RequeteReservationTest {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(
                        "gestion-reservations-pu"
                );

        EntityManager em = emf.createEntityManager();

        try {
            // Date de référence : maintenant
            LocalDateTime date = LocalDateTime.now();


            // REQUETE AVEC JPQL

            String jpql =
                    "SELECT r FROM Reservation r " +
                    "WHERE r.salle.nom = :nomSalle " +
                    "AND r.dateDebut > :date";

            List<Reservation> reservationsJPQL =
                    em.createQuery(jpql, Reservation.class)
                            .setParameter("nomSalle", "Salle A")
                            .setParameter("date", date)
                            .getResultList();

            System.out.println("===== RESULTATS JPQL =====");

            for (Reservation r : reservationsJPQL) {
                System.out.println(
                        "Réservation ID : " + r.getId()
                );
                System.out.println(
                        "Début : " + r.getDateDebut()
                );
                System.out.println(
                        "Fin : " + r.getDateFin()
                );
            }

            System.out.println(
                    "Nombre de résultats JPQL : "
                            + reservationsJPQL.size()
            );


            // REQUETE AVEC CRITERIA API

            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Reservation> cq =
                    cb.createQuery(Reservation.class);

            Root<Reservation> root =
                    cq.from(Reservation.class);

            Predicate memeSalle = cb.equal(
                    root.get("salle").get("nom"),
                    "Salle A"
            );

            Predicate dateFuture = cb.greaterThan(
                    root.<LocalDateTime>get("dateDebut"),
                    date
            );

            cq.select(root).where(
                    cb.and(memeSalle, dateFuture)
            );

            List<Reservation> reservationsCriteria =
                    em.createQuery(cq).getResultList();

            System.out.println("\n===== RESULTATS CRITERIA API =====");

            for (Reservation r : reservationsCriteria) {
                System.out.println(
                        "Réservation ID : " + r.getId()
                );
                System.out.println(
                        "Début : " + r.getDateDebut()
                );
                System.out.println(
                        "Fin : " + r.getDateFin()
                );
            }

            System.out.println(
                    "Nombre de résultats Criteria : "
                            + reservationsCriteria.size()
            );

        } finally {
            em.close();
            emf.close();
        }
    }
}