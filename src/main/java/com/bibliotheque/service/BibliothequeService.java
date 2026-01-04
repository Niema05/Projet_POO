package service;

import dao.MembreDAO;
import dao.impl.MembreDAOImpl;
import model.Membre;

import java.util.List;

public class BibliothequeService {

    private final MembreDAO membreDAO;

    // Constructeur
    public BibliothequeService() {
        this.membreDAO = new MembreDAOImpl();
    }

    /* ============================
       Ajouter un membre
       ============================ */
    public void ajouterMembre(Membre membre) {

        if (membre == null) {
            throw new IllegalArgumentException("Le membre ne peut pas être null");
        }

        if (membre.getNom() == null || membre.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (membre.getEmail() == null || membre.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        // Vérifier unicité email
        if (membreDAO.findByEmail(membre.getEmail()) != null) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // Par défaut : membre actif
        membre.setActif(true);

        membreDAO.save(membre);
    }

    /* ============================
       Modifier un membre
       ============================ */
    public void modifierMembre(Membre membre) {

        if (membre == null || membre.getId() <= 0) {
            throw new IllegalArgumentException("Membre invalide");
        }

        Membre existant = membreDAO.findById(membre.getId());
        if (existant == null) {
            throw new IllegalArgumentException("Membre introuvable");
        }

        membreDAO.update(membre);
    }

    /* ============================
       Activer / Désactiver membre
       ============================ */
    public void activerDesactiver(int id, boolean actif) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        Membre membre = membreDAO.findById(id);
        if (membre == null) {
            throw new IllegalArgumentException("Membre introuvable");
        }

        membre.setActif(actif);
        membreDAO.update(membre);
    }

    /* ============================
       Rechercher membres
       ============================ */
    public List<Membre> rechercherMembres() {
        return membreDAO.findAll();
    }

    public List<Membre> rechercherMembresActifs() {
        return membreDAO.findActifs();
    }

    public Membre rechercherParId(int id) {
        return membreDAO.findById(id);
    }

    /* ============================
       Historique des emprunts
       ============================ */
    public void getHistorique(int membreId) {

        if (membreId <= 0) {
            throw new IllegalArgumentException("ID membre invalide");
        }

        
    }
}
