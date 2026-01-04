package com.bibliotheque.service;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.model.Livre;

import java.util.List;

public class LivreService {

    private LivreDAO livreDAO = new LivreDAOImpl();

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
