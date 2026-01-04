package com.bibliotheque.dao.impl;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;

import java.util.ArrayList;
import java.util.List;

public class LivreDAOImpl implements LivreDAO {

    private static List<Livre> livres = new ArrayList<>();

    @Override
    public void save(Livre livre) {
        livres.add(livre);
    }

    @Override
    public Livre findByIsbn(String isbn) {
        return livres.stream()
                .filter(l -> l.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Livre> findAll() {
        return livres;
    }

    @Override
    public void update(Livre livre) {
        delete(livre.getIsbn());
        save(livre);
    }

    @Override
    public void delete(String isbn) {
        livres.removeIf(l -> l.getIsbn().equals(isbn));
    }
}

