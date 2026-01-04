package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.model.Livre;

import java.util.List;

public class LivreService {

    private final LivreDAO livreDAO;

    // Default constructor (uses concrete DAO)
    public LivreService() {
        this.livreDAO = new LivreDAOImpl();
    }

    // Constructor for dependency injection / testing
    public LivreService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    public void ajouterLivre(Livre livre) {
        livreDAO.save(livre);
    }

    public List<Livre> listerLivres() {
        return livreDAO.findAll();
    }

    public Livre chercherParIsbn(String isbn) {
        return livreDAO.findByIsbn(isbn);
    }

    public void supprimerLivre(String isbn) {
        livreDAO.delete(isbn);
    }

    public void emprunterLivre(String isbn) throws LivreIndisponibleException {
        Livre livre = livreDAO.findByIsbn(isbn);
        if (livre == null || !livre.isDisponible()) {
            throw new LivreIndisponibleException("Livre indisponible : " + isbn);
        }
        livre.emprunter();
        livreDAO.update(livre);
    }
}
