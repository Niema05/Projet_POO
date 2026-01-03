package com.biblio.model;

public interface Empruntable {

    boolean peutEtreEmprunte();
    void emprunter();
    void retourner();
}
