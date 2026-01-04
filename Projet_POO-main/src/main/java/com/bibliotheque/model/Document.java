package com.bibliotheque.model;

public abstract class Document {

    protected String isbn;
    protected String titre;
    protected String id;

    public Document(String isbn, String titre) {
        this.isbn = isbn;
        this.titre = titre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public abstract double calculerPenaliteRetard(int jours);

    /**
     * Calcule la pénalité de retard pour un magazine.
     * Pénalité : 1 DH par jour de retard.
     *
     * @return la pénalité en DH
     */
    public double calculerPenaliteRetard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculerPenaliteRetard'");
    }
}
