package com.bibliotheque.service;

import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.model.Membre;
import com.bibliotheque.model.Livre;

import java.util.List;
import java.util.stream.Collectors;

public class BibliothequeService {

    private final MembreDAO membreDAO;
    private final LivreDAO livreDAO;

    // Constructeurs
    public BibliothequeService() {
        this(new LivreDAOImpl(), new MembreDAOImpl());
    }

    public BibliothequeService(LivreDAO livreDAO, MembreDAO membreDAO) {
        if (livreDAO == null || membreDAO == null) {
            throw new IllegalArgumentException("DAO cannot be null");
        }
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
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

    public List<Livre> getTousLesLivres() {
        return livreDAO.findAll();
    }

    public List<Membre> getTousLesMembres() {
        return membreDAO.findAll();
    }

    /**
     * Recherche les membres par mot-clé sur le nom, prénom ou email (insensible à la casse).
     */
    public List<Membre> rechercherMembres(String motCle) {
        if (motCle == null || motCle.isBlank()) {
            return rechercherMembres();
        }

        String cle = motCle.trim().toLowerCase();
        return membreDAO.findAll().stream()
                .filter(m -> (m.getNom() != null && m.getNom().toLowerCase().contains(cle))
                        || (m.getPrenom() != null && m.getPrenom().toLowerCase().contains(cle))
                        || (m.getEmail() != null && m.getEmail().toLowerCase().contains(cle)))
                .collect(Collectors.toList());
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

    public void supprimerMembre(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        Membre membre = membreDAO.findById(id);
        if (membre == null) {
            throw new IllegalArgumentException("Membre introuvable");
        }

        // Suppression simple via la DAO
        membreDAO.delete(id);
    }
}
