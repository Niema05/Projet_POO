package com.bibliotheque.model;

public abstract class Document {

    protected String isbn;
    protected String titre;
    protected String id;

    public Document(String isbn, String titre) {
        this.isbn = isbn;
        this.titre = titre;
    }

    public Document(String id, String isbn, String titre) {
        this.id = id;
        this.isbn = isbn;
        this.titre = titre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract double calculerPenaliteRetard(int jours);
}
