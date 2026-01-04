package model;

public class Membre extends Personne {

    private int id;
    private boolean actif;
    public Membre() {
    }
    // Constructeur sans id (insertion)
    public Membre(String nom, String prenom, String email, boolean actif) {
        super(nom, prenom, email);
        this.actif = actif;
    }

    // Constructeur avec id (lecture BD)
    public Membre(int id, String nom, String prenom, String email, boolean actif) {
        super(nom, prenom, email);
        this.id = id;
        this.actif = actif;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
    @Override
    public String toString() {
        return "Membre #" + id + ": " + super.toString() + " [Actif: " + actif + "]";
    }
}

