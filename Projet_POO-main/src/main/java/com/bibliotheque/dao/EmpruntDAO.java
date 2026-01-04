package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Membre;

import java.sql.SQLException;
import java.util.List;

public interface EmpruntDAO {
    void save(Emprunt emprunt) throws SQLException;
    Emprunt findById(int id) throws SQLException;
    List<Emprunt> findAll() throws SQLException;
    void update(Emprunt emprunt) throws SQLException;
    List<Emprunt> findEnCours() throws SQLException;
    int countEmpruntEnCours(Membre member) throws SQLException;
    List<Emprunt> findByMember(Membre member) throws SQLException;
}
