
package com.example.model;

import javax.persistence.*;
import java.util.HashSet;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
@Cacheable
@org.hibernate.annotations.Cache(
	    usage = CacheConcurrencyStrategy.READ_WRITE
	)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nom_complet", nullable = false)
    private String nom;

    // Gestion des modifications concurrentes
    @Version
    private int version;

    // Un utilisateur peut effectuer plusieurs réservations
    @OneToMany(
        mappedBy = "utilisateur",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Reservation> reservations = new HashSet<>();

    public Utilisateur() {
    }

    public Utilisateur(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVersion() {
        return version;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    // Ajouter une réservation à l'utilisateur
    public void ajouterReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException(
                "La réservation ne peut pas être null."
            );
        }

        reservations.add(reservation);
        reservation.setUtilisateur(this);
    }

    // Supprimer une réservation de la collection
    public void retirerReservation(Reservation reservation) {
        if (reservation != null && reservations.remove(reservation)) {
            // orphanRemoval=true permet à Hibernate
            // de supprimer la réservation en base.
        }
    }
}
