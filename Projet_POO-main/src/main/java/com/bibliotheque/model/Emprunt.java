package com.bibliotheque.model;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue ;
    private LocalDate dateRetourEffective ;
    private Livre livre;
    private Membre membre;
    private double penalite ;
    public Emprunt(int id ,LocalDate dataEmprunt,LocalDate dateRetourPrevue,LocalDate dateRetourEffective,Livre livre,Membre membre,double penalite){
        this.id = id;
        this.dateEmprunt = dataEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective ; 
        this.livre = livre;
        this.membre = membre;
        this.penalite = penalite;
    }
    //Getters
    public int getId(){
        return id;
    }
    public LocalDate getDateEmprunt(){
        return dateEmprunt;
    }
    public LocalDate getdateRetourPrevue(){
        return dateRetourPrevue;
    }
    public LocalDate getdateRetourEffective(){
        return dateRetourEffective;
    }
    public Membre getMembre(){
        return membre;
    }
    public Livre getLivre(){
        return livre;
    }
    public double getPenalite(){
        return penalite;
    }
    //Setters 
    public void setId(int id){
        this.id = id;
    }
    public void setDateEmprunt(LocalDate dateEmprunt){
        this.dateEmprunt = dateEmprunt;
    }
    public void setdateRetourPrevue(LocalDate dateRetourPrevue){
        this.dateRetourPrevue = dateRetourPrevue;
    }
    public void setdateRetoureffective(LocalDate dateRetourEffective){
        this.dateRetourEffective = dateRetourEffective;
    }
    public void setMembre(Membre membre){
        this.membre = membre;
    }
    public void setLivre(Livre livre){
        this.livre = livre;
    }
    public void setPenalite(double penalite){
        this.penalite = penalite;
    }
    public String toString() {
    return "Emprunt{" +
            "id='" + id + '\'' +
            ", dateEmprunt=" + dateEmprunt +
            ", dateRetourPrevue=" + dateRetourPrevue +
            ", dateRetourEffective=" + dateRetourEffective +
            ", livre=" + (livre != null ? livre.getTitre() : "null") +
            ", membre=" + (membre != null ? membre.getNom() + " " + membre.getPrenom() : "null") +
            ", penalite=" + penalite +
            '}';
}
}
