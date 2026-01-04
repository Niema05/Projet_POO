package com.bibliotheque.dao.impl;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.dao.MembreDAO;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.model.Livre;
import com.bibliotheque.model.Membre;
import com.bibliotheque.util.DatabaseConnection;
public class EmpruntDAOImpl implements EmpruntDAO  {
        private Emprunt ToEmprunt(ResultSet result) throws SQLException {
        LivreDAO livreDAO = new LivreDAOImpl();
        Livre livre = livreDAO.findByISBN(result.getString("isbn"));
        MembreDAO membreDAO = new MembreDAOImpl();
        Membre membre = membreDAO.findByIntId(result.getInt("membre_id")) ;
        return new Emprunt(result.getInt("id"),
                           result.getDate("dateEmprunt").toLocalDate(),
                           result.getDate("dateRetourPrevue").toLocalDate(), 
                           result.getDate("dateRetoureffective").toLocalDate(), 
                           livre, 
                           membre, 
                           result.getDouble("penalite"));
    }

    @Override 
    
    public void save(Emprunt emprunt) throws SQLException{
        String sql = "INSERT INTO emprunts (id, isbn , membre_id , date_emprunt , date_retour_prevue , date_retour_effective , penalite) VALUES (?, ?, ?, ?, ?, ? , ?)";
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, emprunt.getId());
            stmt.setString(2, emprunt.getLivre().getIsbn());
            stmt.setInt(3, emprunt.getMembre().getId());
            stmt.setDate(4, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(5, Date.valueOf(emprunt.getdateRetourPrevue()));
            stmt.setDate(6, Date.valueOf(emprunt.getdateRetourEffective()));
            stmt.setDouble(7, emprunt.getPenalite());
            stmt.executeUpdate();
        }
    }
    @Override
    public Emprunt findById(int id) throws SQLException{
        String sql = "SELECT * FROM emprunts WHERE id = ?" ;
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
                if (result.next()){
                    return ToEmprunt(result);
                }
        }
        return null;

    }
    @Override
    public List<Emprunt> findAll() throws SQLException {
        List<Emprunt> ListEmprunt = new ArrayList<>();
        String sql = "SELECT * FROM empeunts " ; 
         try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)){
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                Emprunt emprunt = ToEmprunt(result);
                ListEmprunt.add(emprunt);
            }
            return ListEmprunt ; 
         }
    }
    @Override 
    public void update(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE emprunts SET id = ? ,  membre_id = ? , date_emprunt = ? , date_retour_prevue = ?, date_retour_effective = ?, penalite = ?" ; 
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)){
            stmt.setInt(1, emprunt.getId());
            stmt.setString(2, emprunt.getLivre().getIsbn());
            stmt.setInt(3, emprunt.getMembre().getId());
            stmt.setDate(4, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(5, Date.valueOf(emprunt.getdateRetourPrevue()));
            stmt.setDate(6, Date.valueOf(emprunt.getdateRetourEffective()));
            stmt.setDouble(7, emprunt.getPenalite());
            stmt.executeUpdate();
        }
    }
    @Override
    public List<Emprunt> findByMember(Membre member) throws SQLException{
        String sql = "SELECT * FROM emprunts WHERE member_id = ?" ;
        List<Emprunt> ListEmprunt = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, member.getId());
            ResultSet result = stmt.executeQuery();
                while(result.next()){
                    Emprunt emprunt = ToEmprunt(result);
                    ListEmprunt.add(emprunt);

                   
                }
            return ListEmprunt;
        }
    }
    @Override
    public List<Emprunt> findEnCours() throws SQLException{
        List<Emprunt> ListEmprunt = new ArrayList<>();
        String sql = "SELECT * FROM emprunts where date_retour_effective IS NULL " ;
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
                while(result.next()){
                    Emprunt emprunt = ToEmprunt(result);
                    ListEmprunt.add(emprunt);

                   
                }
            return ListEmprunt;
        }

    }
    @Override
    public int countEmpruntEnCours(Membre member) throws SQLException{
        List<Emprunt> ListEmprunt = new ArrayList<>();
        String sql = "SELECT * FROM emprunts where date_retour_effective IS NULL AND membre_id = ? " ;
        int count = 0 ;
        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();
            stmt.setInt(1, member.getId());
                while(result.next()){
                    Emprunt emprunt = ToEmprunt(result);
                    ListEmprunt.add(emprunt);
                    count ++ ;
                }
            return count;
        }

    }

    

}
