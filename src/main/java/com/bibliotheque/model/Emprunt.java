import java.time.LocalDate;

public class Emprunt {
    private String id;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue ;
    private LocalDate dateRetoureffective ;
    private Livre livre;
    private Membre membre;
    private double penalite ;
    public Emprunt(String id ,LocalDate dataEmprunt,LocalDate dateRetourPrevue,LocalDate dateRetoureffective,Livre livre,Membre membre,double penalite){
        this.id = id;
        this.dateEmprunt = dataEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetoureffective = dateRetoureffective ; 
        this.livre = livre;
        this.membre = membre;
        this.penalite = penalite;
    }
    //Getters
    public String getId(){
        return id;
    }
    public LocalDate getDateEmprunt(){
        return dateEmprunt;
    }
    public LocalDate getdateRetourPrevue(){
        return dateRetourPrevue;
    }
    public LocalDate getdateRetoureffective(){
        return dateRetoureffective;
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
    public void setId(String id){
        this.id = id;
    }
    public void setDateEmprunt(LocalDate dateEmprunt){
        this.dateEmprunt = dateEmprunt;
    }
    public void setdateRetourPrevue(LocalDate dateRetourPrevue){
        this.dateRetourPrevue = dateRetourPrevue;
    }
    public void setdateRetoureffective(LocalDate dateRetoureffective){
        this.dateRetoureffective = dateRetoureffective;
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
}
