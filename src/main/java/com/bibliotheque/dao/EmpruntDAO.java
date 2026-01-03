package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;
import java.lang.reflect.Member;
import java.util.List;

public interface EmpruntDAO {
    void save(Emprunt emprunt);
    Emprunt findById(int id);
    List<Emprunt> findAll();
    void update(Emprunt emprunt);
    List<Emprunt> findByMember(Member member);
    List<Emprunt> findEnCours();
    int countEmpruntEnCours(Member member);
}
