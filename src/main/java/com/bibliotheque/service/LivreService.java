package com.biblio.service;

import com.biblio.dao.LivreDAO;
import com.biblio.dao.impl.LivreDAOImpl;
import com.biblio.model.Livre;

import java.util.List;

public class LivreService {

    private LivreDAO livreDAO = new LivreDAOImpl();

    public void ajouterLivre(String isbn, String titre, String auteur) {
        Livre livre = new Livre(isbn, titre, auteur);
        livreDAO.save(livre);
    }

    public List<Livre> listerLivres() {
        return livreDAO.findAll();
    }

    public void supprimerLivre(String isbn) {
        livreDAO.delete(isbn);
    }
}

