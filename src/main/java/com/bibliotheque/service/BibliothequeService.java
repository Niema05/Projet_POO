package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.StringValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service métier pour la gestion des livres et des membres.
 */
public class BibliothequeService {
    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param livreDAO  le DAO des livres
     * @param membreDAO le DAO des membres
     */
    public BibliothequeService(LivreDAO livreDAO, MembreDAO membreDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }

    // ========== MÉTHODES POUR LES LIVRES ==========

    /**
     * Ajoute un nouveau livre à la bibliothèque.
     *
     * @param livre le livre à ajouter
     * @throws ValidationException si les données du livre sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void ajouterLivre(Livre livre) throws ValidationException, SQLException {
        // Validation
        StringValidator.validateISBN(livre.getIsbn());
        StringValidator.validateTitre(livre.getTitre());
        StringValidator.validateNotEmpty(livre.getAuteur(), "L'auteur");
        StringValidator.validateAnneePublication(livre.getAnneePublication());

        // Vérifier que le livre n'existe pas déjà
        if (livreDAO.existsByISBN(livre.getIsbn())) {
            throw new ValidationException("Un livre avec cet ISBN existe déjà : " + livre.getIsbn());
        }

        livreDAO.save(livre);
    }

    /**
     * Modifie un livre existant.
     *
     * @param livre le livre à modifier
     * @throws ValidationException si les données sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void modifierLivre(Livre livre) throws ValidationException, SQLException {
        StringValidator.validateISBN(livre.getIsbn());
        StringValidator.validateTitre(livre.getTitre());
        StringValidator.validateNotEmpty(livre.getAuteur(), "L'auteur");
        StringValidator.validateAnneePublication(livre.getAnneePublication());

        livreDAO.update(livre);
    }

    /**
     * Supprime un livre par son ISBN.
     *
     * @param isbn l'ISBN du livre à supprimer
     * @throws SQLException si une erreur de base de données survient
     */
    public void supprimerLivre(String isbn) throws SQLException {
        livreDAO.delete(isbn);
    }

    /**
     * Recherche des livres par titre ou auteur.
     *
     * @param critere le critère de recherche
     * @return une liste de livres correspondant au critère
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> rechercherLivres(String critere) throws SQLException {
        List<Livre> resultats = new ArrayList<>();
        resultats.addAll(livreDAO.findByTitre(critere));
        
        List<Livre> parAuteur = livreDAO.findByAuteur(critere);
        for (Livre livre : parAuteur) {
            if (!resultats.contains(livre)) {
                resultats.add(livre);
            }
        }
        
        return resultats;
    }

    /**
     * Récupère tous les livres disponibles.
     *
     * @return une liste de livres disponibles
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getLivresDisponibles() throws SQLException {
        return livreDAO.findDisponibles();
    }

    /**
     * Récupère tous les livres.
     *
     * @return une liste de tous les livres
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getTousLesLivres() throws SQLException {
        return livreDAO.findAll();
    }

    // ========== MÉTHODES POUR LES MEMBRES ==========

    /**
     * Ajoute un nouveau membre à la bibliothèque.
     *
     * @param membre le membre à ajouter
     * @throws ValidationException si les données du membre sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void ajouterMembre(Membre membre) throws ValidationException, SQLException {
        // Validation
        StringValidator.validateNomPrenom(membre.getNom(), membre.getPrenom());
        StringValidator.validateEmail(membre.getEmail());

        // Vérifier que l'email n'existe pas déjà
        if (membreDAO.existsByEmail(membre.getEmail())) {
            throw new ValidationException("Un membre avec cet email existe déjà : " + membre.getEmail());
        }

        membreDAO.save(membre);
    }

    /**
     * Modifie un membre existant.
     *
     * @param membre le membre à modifier
     * @throws ValidationException si les données sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void modifierMembre(Membre membre) throws ValidationException, SQLException {
        StringValidator.validateNomPrenom(membre.getNom(), membre.getPrenom());
        StringValidator.validateEmail(membre.getEmail());

        membreDAO.update(membre);
    }

    /**
     * Active ou désactive un membre.
     *
     * @param id    l'identifiant du membre
     * @param actif true pour activer, false pour désactiver
     * @throws SQLException si une erreur de base de données survient
     */
    public void activerDesactiverMembre(int id, boolean actif) throws SQLException {
        Membre membre = membreDAO.findByIntId(id);
        if (membre != null) {
            membre.setActif(actif);
            membreDAO.update(membre);
        }
    }

    /**
     * Recherche des membres par nom ou prénom.
     *
     * @param critere le critère de recherche
     * @return une liste de membres correspondant au critère
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Membre> rechercherMembres(String critere) throws SQLException {
        List<Membre> resultats = new ArrayList<>();
        List<Membre> tous = membreDAO.findAll();
        
        for (Membre membre : tous) {
            if (membre.getNom().toLowerCase().contains(critere.toLowerCase()) ||
                membre.getPrenom().toLowerCase().contains(critere.toLowerCase()) ||
                membre.getEmail().toLowerCase().contains(critere.toLowerCase())) {
                resultats.add(membre);
            }
        }
        
        return resultats;
    }

    /**
     * Récupère tous les membres actifs.
     *
     * @return une liste de membres actifs
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Membre> getMembresActifs() throws SQLException {
        return membreDAO.findActifs();
    }

    /**
     * Récupère tous les membres.
     *
     * @return une liste de tous les membres
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Membre> getTousLesMembres() throws SQLException {
        return membreDAO.findAll();
    }

    /**
     * Récupère un membre par son ID.
     *
     * @param id l'identifiant du membre
     * @return le membre trouvé, null sinon
     * @throws SQLException si une erreur de base de données survient
     */
    public Membre getMembre(int id) throws SQLException {
        return membreDAO.findByIntId(id);
    }
}
