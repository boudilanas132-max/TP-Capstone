package com.example.model;

import javax.persistence.*;
import java.util.HashSet;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.Set;

@Entity
@Table(name = "salles")
@org.hibernate.annotations.Cache(
	    usage = CacheConcurrencyStrategy.READ_WRITE
	)
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private int capacite;

    private String localisation;

    // Une salle peut avoir plusieurs réservations
    @OneToMany(mappedBy = "salle")
    private Set<Reservation> reservations = new HashSet<>();

    // Une salle peut posséder plusieurs équipements
    @ManyToMany(
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JoinTable(
        name = "salle_equipement",
        joinColumns = @JoinColumn(name = "salle_id"),
        inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();

    public Salle() {
    }

    public Salle(String nom, int capacite, String localisation) {
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = localisation;
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

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Set<Equipement> getEquipements() {
        return equipements;
    }

    // Ajouter une réservation à la salle
    public void ajouterReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException(
                "La réservation ne peut pas être null."
            );
        }

        reservations.add(reservation);
        reservation.setSalle(this);
    }

    // Retirer la réservation de la collection en mémoire
    public void retirerReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.remove(reservation);
        }
    }

    // Ajouter un équipement à la salle
    public void ajouterEquipement(Equipement equipement) {
        if (equipement == null) {
            throw new IllegalArgumentException(
                "L'équipement ne peut pas être null."
            );
        }

        if (equipements.add(equipement)) {
            equipement.getSalles().add(this);
        }
    }

    // Retirer un équipement de la salle
    public void retirerEquipement(Equipement equipement) {
        if (equipement != null && equipements.remove(equipement)) {
            equipement.getSalles().remove(this);
        }
    }
}
