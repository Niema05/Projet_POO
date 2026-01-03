package com.bibliotheque.model;

/**
 * Classe représentant un livre dans la bibliothèque.
 */
public class Livre extends Document implements Empruntable {
    private String isbn;
    private String auteur;
    private int anneePublication;
    private boolean disponible;
    
    // Pénalité : 2 DH par jour de retard
    private static final double PENALITE_PAR_JOUR = 2.0;

    /**
     * Constructeur par défaut.
     */
    public Livre() {
        super();
        this.disponible = true;
    }

    /**
     * Constructeur d'un livre.
     *
     * @param isbn               l'ISBN du livre (format 978-XXXXXXXXXX)
     * @param titre              le titre du livre
     * @param auteur             l'auteur du livre
     * @param anneePublication   l'année de publication
     * @param disponible         la disponibilité du livre
     */
    public Livre(String isbn, String titre, String auteur, int anneePublication, boolean disponible) {
        super(isbn, titre);
        this.isbn = isbn;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
    }

    /**
     * Constructeur simplifié (disponible par défaut = true).
     */
    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        this(isbn, titre, auteur, anneePublication, true);
    }

    /**
     * Calcule la pénalité de retard pour un livre.
     * Pénalité : 2 DH par jour de retard.
     *
     * @param joursRetard le nombre de jours de retard
     * @return la pénalité en DH
     */
    @Override
    public double calculerPenaliteRetard(int joursRetard) {
        if (joursRetard <= 0) {
            return 0.0;
        }
        return joursRetard * PENALITE_PAR_JOUR;
    }

    @Override
    public boolean peutEtreEmprunte() {
        return disponible;
    }

    @Override
    public void emprunter() {
        this.disponible = false;
    }

    @Override
    public void retourner() {
        this.disponible = true;
    }

    // Getters et Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
        setId(isbn); // L'ISBN sert aussi d'ID
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "isbn='" + isbn + '\'' +
                ", titre='" + getTitre() + '\'' +
                ", auteur='" + auteur + '\'' +
                ", annee=" + anneePublication +
                ", disponible=" + disponible +
                '}';
    }
}
