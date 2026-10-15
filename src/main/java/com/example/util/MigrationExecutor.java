package com.example.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MigrationExecutor {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public MigrationExecutor(
            String jdbcUrl,
            String username,
            String password) {

        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void executeMigration(String scriptPath) throws Exception {

        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(scriptPath);

        if (is == null) {
            throw new IllegalArgumentException(
                    "Fichier SQL introuvable : " + scriptPath
            );
        }

        try (InputStream input = is;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             input, StandardCharsets.UTF_8));
             Connection conn = DriverManager.getConnection(
                     jdbcUrl, username, password)) {

            conn.setAutoCommit(false);

            try {
                StringBuilder sql = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();

                    // Ignorer les lignes vides et les commentaires SQL
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }

                    sql.append(line).append("\n");
                }

                // Exécuter chaque instruction séparément
                try (Statement stmt = conn.createStatement()) {
                    for (String instruction : sql.toString().split(";")) {
                        if (!instruction.trim().isEmpty()) {
                            stmt.execute(instruction.trim());
                        }
                    }
                }

                conn.commit();
                System.out.println("Migration exécutée avec succès.");

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
