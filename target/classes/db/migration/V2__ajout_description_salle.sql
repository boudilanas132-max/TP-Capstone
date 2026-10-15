-- Migration V2 : ajout d'une description aux salles
ALTER TABLE salles
ADD COLUMN description VARCHAR(255);

-- Création d'un index unique sur l'email des utilisateurs
-- Vérifier qu'aucun email n'est dupliqué avant d'exécuter ce script.
CREATE UNIQUE INDEX idx_utilisateur_email
ON utilisateurs(email);
