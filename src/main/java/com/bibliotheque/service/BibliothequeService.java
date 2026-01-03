package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.StringValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service gérant la logique métier pour les livres.
 * Coordonne les opérations et applique les règles métier.
 */
public class BibliothequeService {

    private LivreDAO livreDAO;

    /**
     * Constructeur qui initialise le DAO.
     */
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
    }

    /**
     * Ajoute un nouveau livre après validation.
     *
     * @param livre le livre à ajouter
     * @throws ValidationException si les données sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void ajouterLivre(Livre livre) throws ValidationException, SQLException {
        // Validation des données
        validerLivre(livre);

        // Vérifier si l'ISBN existe déjà
        if (livreDAO.existsByISBN(livre.getIsbn())) {
            throw new ValidationException("Un livre avec cet ISBN existe déjà : " + livre.getIsbn());
        }

        // Enregistrer le livre
        livreDAO.save(livre);
    }

    /**
     * Modifie un livre existant après validation.
     *
     * @param livre le livre à modifier
     * @throws ValidationException si les données sont invalides
     * @throws SQLException        si une erreur de base de données survient
     */
    public void modifierLivre(Livre livre) throws ValidationException, SQLException {
        // Validation des données
        validerLivre(livre);

        // Vérifier que le livre existe
        Livre existant = livreDAO.findById(livre.getIsbn());
        if (existant == null) {
            throw new ValidationException("Livre introuvable avec l'ISBN : " + livre.getIsbn());
        }

        // Mettre à jour le livre
        livreDAO.update(livre);
    }

    /**
     * Supprime un livre.
     *
     * @param isbn l'ISBN du livre à supprimer
     * @throws SQLException si une erreur de base de données survient
     */
    public void supprimerLivre(String isbn) throws SQLException {
        // Note : Dans une vraie application, on vérifierait qu'il n'y a pas d'emprunts en cours
        Livre livre = livreDAO.findById(isbn);
        if (livre == null) {
            throw new SQLException("Livre introuvable avec l'ISBN : " + isbn);
        }

        livreDAO.delete(isbn);
    }

    /**
     * Recherche avancée de livres.
     * Combine recherche par titre, auteur et ISBN.
     * Élimine les doublons.
     *
     * @param recherche le terme de recherche
     * @return liste des livres trouvés sans doublons
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> rechercherLivres(String recherche) throws SQLException {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getTousLesLivres();
        }

        Set<Livre> resultatsUniques = new HashSet<>();

        // Recherche par titre
        List<Livre> parTitre = livreDAO.findByTitre(recherche);
        resultatsUniques.addAll(parTitre);

        // Recherche par auteur
        List<Livre> parAuteur = livreDAO.findByAuteur(recherche);
        resultatsUniques.addAll(parAuteur);

        // Recherche par ISBN exact
        Livre parIsbn = livreDAO.findById(recherche);
        if (parIsbn != null) {
            resultatsUniques.add(parIsbn);
        }

        return new ArrayList<>(resultatsUniques);
    }

    /**
     * Récupère tous les livres disponibles pour emprunt.
     *
     * @return liste des livres disponibles
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getLivresDisponibles() throws SQLException {
        return livreDAO.findDisponibles();
    }

    /**
     * Récupère tous les livres.
     *
     * @return liste de tous les livres
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getTousLesLivres() throws SQLException {
        return livreDAO.findAll();
    }

    /**
     * Récupère un livre par son ISBN.
     *
     * @param isbn l'ISBN du livre
     * @return le livre ou null
     * @throws SQLException si une erreur de base de données survient
     */
    public Livre getLivreParIsbn(String isbn) throws SQLException {
        return livreDAO.findById(isbn);
    }

    /**
     * Récupère des statistiques sur les livres.
     *
     * @return tableau [total, disponibles, empruntés]
     * @throws SQLException si une erreur de base de données survient
     */
    public int[] getStatistiques() throws SQLException {
        List<Livre> tous = livreDAO.findAll();
        int total = tous.size();
        int disponibles = (int) tous.stream().filter(Livre::isDisponible).count();
        int empruntes = total - disponibles;

        return new int[]{total, disponibles, empruntes};
    }

    /**
     * Valide les données d'un livre.
     *
     * @param livre le livre à valider
     * @throws ValidationException si les données sont invalides
     */
    private void validerLivre(Livre livre) throws ValidationException {
        if (livre == null) {
            throw new ValidationException("Le livre ne peut pas être null");
        }

        // Validation ISBN
        StringValidator.validateISBN(livre.getIsbn());

        // Validation titre
        StringValidator.validateTitre(livre.getTitre());

        // Validation auteur
        StringValidator.validateNotEmpty(livre.getAuteur(), "L'auteur");
        if (livre.getAuteur().length() > 100) {
            throw new ValidationException("Le nom de l'auteur ne peut pas dépasser 100 caractères");
        }

        // Validation année
        StringValidator.validateAnneePublication(livre.getAnneePublication());
    }

    /**
     * Récupère les livres d'un auteur spécifique.
     *
     * @param auteur le nom de l'auteur
     * @return liste des livres de cet auteur
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getLivresParAuteur(String auteur) throws SQLException {
        return livreDAO.findByAuteur(auteur);
    }

    /**
     * Récupère les livres par titre (recherche partielle).
     *
     * @param titre le titre ou partie du titre
     * @return liste des livres correspondants
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Livre> getLivresParTitre(String titre) throws SQLException {
        return livreDAO.findByTitre(titre);
    }
}
