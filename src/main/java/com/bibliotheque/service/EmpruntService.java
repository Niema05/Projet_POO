package com.bibliotheque.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.exception.MembreInactifException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Membre; 

public class EmpruntService{
    EmpruntDAO empruntDAO ;
    LivreDAO livreDAO ;
    MembreDAO membreDAO;
    EmpruntService (EmpruntDAO empruntDAO, LivreDAO livreDAO , MembreDAO membreDAO){
        this.empruntDAO = empruntDAO ;
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }    
    public Emprunt emprunterLivre(String ISBN , int member_id) throws LivreIndisponibleException , MembreInactifException , LimiteEmpruntDepasseeException , SQLException {
        Livre livre = livreDAO.findByISBN(ISBN);
        if (livre ==  null){
            throw new LivreIndisponibleException("Livre Indisponible");
        }
        if (membreDAO.findByIntId(member_id) == null){
            throw new MembreInactifException("Membre " + member_id + " inactig");
        }
        Membre membre = membreDAO.findByIntId(member_id);
        if (empruntDAO.countEmpruntEnCours(membre)>3){
            throw new LimiteEmpruntDepasseeException("Vous avez depassé votre Limite d'emprunt");
        }
        else{
            LocalDate dateemprunt = LocalDate.now();
            LocalDate dateRetourPrevue = LocalDate.now().plusDays(15);
            Emprunt emprunt = new Emprunt(0,dateemprunt,dateRetourPrevue,null,livre,membre,null);
            empruntDAO.save(emprunt);
            livre.emprunter();
            livreDAO.update(livre);
            return emprunt;
        }

    }
    public double calculerPenalite(LocalDate dateRetourPrevue,LocalDate dataEmprunt , LocalDate dateRetourEffective ){
        if (dateRetourPrevue.equals(null)){
            return 0.0 ;
        }
        if (dataEmprunt.equals(null)){
            return 0.0;
        }
        if (dataEmprunt.isBefore(dataEmprunt.plusDays(15))){
            return 0.0 ; 
        }
        long joursRetard = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        double penalite = joursRetard*1.5;
        return penalite;
    
    }
    public Emprunt RetournerLivre(String ISBN , int member_id) throws LivreIndisponibleException , MembreInactifException , SQLException {
        Livre livre = livreDAO.findByISBN(ISBN);
        if (livre ==  null){
            throw new LivreIndisponibleException("Livre Indisponible");
        }
        if (membreDAO.findByIntId(member_id) == null){
            throw new MembreInactifException("Membre " + member_id + " inactig");
        }
        Membre membre = membreDAO.findByIntId(member_id);
        List<Emprunt> empruntsEnCours = empruntDAO.findEnCours();
        Emprunt empruntActuel = null;
        for ( Emprunt ept : empruntsEnCours){
            if (ept.getLivre().getIsbn().equals(ISBN)){
                empruntActuel = ept;
                break;
            }
        }
        LocalDate dateRetourPrevue = empruntActuel.getdateRetourPrevue() ; 
        LocalDate dateRetourEffective = LocalDate.now();
        LocalDate dateEmprunt = empruntActuel.getDateEmprunt();
        if (calculerPenalite(dateRetourPrevue, dateEmprunt, dateRetourEffective)==0.0){

            Emprunt emprunt = new Emprunt(empruntActuel.getId(),dateEmprunt,dateRetourPrevue,dateRetourEffective,livre,membre,0.0);
            empruntDAO.update(emprunt);
            livreDAO.update(livre);
            System.out.print("Pas de pénalité");
            return emprunt;
        }
        else {
            double penalite = calculerPenalite(dateRetourPrevue, dateEmprunt, dateRetourEffective);
            Emprunt emprunt = new Emprunt(empruntActuel.getId(),dateEmprunt,dateRetourPrevue,dateRetourEffective,livre,membre,penalite);
            empruntDAO.update(emprunt);
            livreDAO.update(livre);
            System.out.print("pénalité de " + penalite + "DH");
            return emprunt;
        }



    }
    public List<Emprunt> getEmpruntEnRetard() throws SQLException{
        LocalDate DateToday = LocalDate.now();
        List<Emprunt> EmpruntEnRetard = new ArrayList<>();
        List<Emprunt> ToutEmprunt = empruntDAO.findAll();
        for (Emprunt emprunt : ToutEmprunt){
            LocalDate PreviewDate = emprunt.getdateRetourPrevue() ; 
            LocalDate EmpruntDate = emprunt.getDateEmprunt();
            if (calculerPenalite(PreviewDate, EmpruntDate, DateToday) > 0.0){
                EmpruntEnRetard.add(emprunt);
            }
            
        }
        return EmpruntEnRetard ;


    }


    }
