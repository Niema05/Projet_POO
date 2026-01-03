package com.biblio.dao;

import com.biblio.model.Livre;
import java.util.List;

public interface LivreDAO {

    void save(Livre livre);
    Livre findByIsbn(String isbn);
    List<Livre> findAll();
    void update(Livre livre);
    void delete(String isbn);
}

