package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PerformanceReport {

    private final Statistics stats;

    public PerformanceReport(EntityManagerFactory emf) {
        SessionFactory sessionFactory =
                emf.unwrap(SessionFactory.class);

        this.stats = sessionFactory.getStatistics();
        this.stats.setStatisticsEnabled(true);
    }

    public void printReport() {
        System.out.println("\n===== RAPPORT DE PERFORMANCE HIBERNATE =====");

        System.out.println(
                "Requêtes HQL exécutées : "
                        + stats.getQueryExecutionCount()
        );

        System.out.println(
                "Cache de second niveau - hits : "
                        + stats.getSecondLevelCacheHitCount()
        );

        System.out.println(
                "Cache de second niveau - misses : "
                        + stats.getSecondLevelCacheMissCount()
        );

        System.out.println(
                "Cache de second niveau - mises en cache : "
                        + stats.getSecondLevelCachePutCount()
        );

        System.out.println(
                "Transactions : "
                        + stats.getTransactionCount()
        );

        System.out.println(
                "Préparations de requêtes SQL : "
                        + stats.getPrepareStatementCount()
        );

        System.out.println(
                "Entités chargées : "
                        + stats.getEntityLoadCount()
        );

        System.out.println(
                "Entités insérées : "
                        + stats.getEntityInsertCount()
        );

        System.out.println(
                "Entités mises à jour : "
                        + stats.getEntityUpdateCount()
        );

        System.out.println(
                "Entités supprimées : "
                        + stats.getEntityDeleteCount()
        );

        System.out.println("============================================\n");
    }
}
