
package com.example.gestion_reservations;

import com.example.model.Utilisateur;
import com.example.util.DataInitializer;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory(
                        "gestion-reservations-pu"
                );

        try {
            //  Initialiser les données de test
            EntityManager em = emf.createEntityManager();

            try {
                new DataInitializer(em).initData();
            } finally {
                em.close();
            }

            // Récupérer les statistiques Hibernate
            SessionFactory sessionFactory =
                    emf.unwrap(SessionFactory.class);

            Statistics statistics =
                    sessionFactory.getStatistics();

            statistics.setStatisticsEnabled(true);

            // Récupérer l'identifiant d'Alice sans charger son entité complète
            EntityManager emId = emf.createEntityManager();

            Long userId;

            try {
                userId = emId.createQuery(
                        "SELECT u.id FROM Utilisateur u " +
                        "WHERE u.email = :email",
                        Long.class
                )
                .setParameter(
                        "email",
                        "alice.dupont@example.com"
                )
                .getSingleResult();
            } finally {
                emId.close();
            }

            // Vider le cache pour réaliser un test propre
            sessionFactory.getCache()
                    .evictEntityData(Utilisateur.class);

            statistics.clear();

            // Premier chargement
            System.out.println(
                    "\n========== PREMIER CHARGEMENT =========="
            );

            EntityManager em1 = emf.createEntityManager();

            try {
                Utilisateur utilisateur1 =
                        em1.find(Utilisateur.class, userId);

                if (utilisateur1 != null) {
                    System.out.println(
                            "Utilisateur trouvé : "
                                    + utilisateur1.getNom()
                    );

                    System.out.println(
                            "Email : "
                                    + utilisateur1.getEmail()
                    );
                } else {
                    System.out.println("Utilisateur introuvable.");
                }
            } finally {
                em1.close();
            }

            System.out.println(
                    "Hits du cache : "
                            + statistics.getSecondLevelCacheHitCount()
            );

            System.out.println(
                    "Misses du cache : "
                            + statistics.getSecondLevelCacheMissCount()
            );

            // Deuxième chargement dans un nouvel EntityManager
            System.out.println(
                    "\n========== DEUXIEME CHARGEMENT =========="
            );

            EntityManager em2 = emf.createEntityManager();

            try {
                Utilisateur utilisateur2 =
                        em2.find(Utilisateur.class, userId);

                if (utilisateur2 != null) {
                    System.out.println(
                            "Utilisateur trouvé : "
                                    + utilisateur2.getNom()
                    );
                } else {
                    System.out.println("Utilisateur introuvable.");
                }
            } finally {
                em2.close();
            }

            // Statistiques finales
            System.out.println(
                    "\n========== STATISTIQUES FINALES =========="
            );

            System.out.println(
                    "Hits du cache de second niveau : "
                            + statistics.getSecondLevelCacheHitCount()
            );

            System.out.println(
                    "Misses du cache de second niveau : "
                            + statistics.getSecondLevelCacheMissCount()
            );

            System.out.println(
                    "Put dans le cache de second niveau : "
                            + statistics.getSecondLevelCachePutCount()
            );

            System.out.println(
                    "Entités chargées : "
                            + statistics.getEntityLoadCount()
            );

            System.out.println(
                    "Requêtes SQL exécutées : "
                            + statistics.getPrepareStatementCount()
            );

            System.out.println("\nTest terminé.");

        } catch (Exception e) {
            System.err.println(
                    "Erreur pendant l'exécution de l'application."
            );
            e.printStackTrace();

        } finally {
            //  Fermer l'EntityManagerFactory
            if (emf.isOpen()) {
                emf.close();
            }
        }
    }
}
