package com.biblio.model;

public class Livre extends Document implements Empruntable {
    super(isbn, titre);
    private String auteur;
    private boolean disponible = true;

    public Livre(String isbn, String titre, String auteur) {
        super(isbn, titre);
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    public boolean isDisponible() {
        return disponible;
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

    @Override
    public double calculerPenaliteRetard(int jours) {
        return jours * 1.5;
    }
}

