package com.biblio.model;

public abstract class Document {

    protected String isbn;
    protected String titre;

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
}
