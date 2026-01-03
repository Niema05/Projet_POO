package com.bibliotheque.dao.impl;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface LivreDAO.
 * Gère toutes les opérations de base de données pour les livres.
 */
public class LivreDAOImpl implements LivreDAO {

    private Connection connection;

    /**
     * Constructeur qui récupère la connexion via le Singleton.
     */
    public LivreDAOImpl() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
        }
    }

    @Override
    public void save(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (isbn, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getIsbn());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Échec de l'ajout du livre");
            }
        }
    }

    @Override
    public Livre findById(String isbn) throws SQLException {
        String sql = "SELECT * FROM livres WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToLivre(rs);
            }
        }
        return null;
    }

    @Override
    public List<Livre> findAll() throws SQLException {
        String sql = "SELECT * FROM livres ORDER BY titre";
        List<Livre> livres = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, annee_publication = ?, disponible = ? WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setInt(3, livre.getAnneePublication());
            stmt.setBoolean(4, livre.isDisponible());
            stmt.setString(5, livre.getIsbn());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun livre trouvé avec l'ISBN : " + livre.getIsbn());
            }
        }
    }

    @Override
    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM livres WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun livre trouvé avec l'ISBN : " + isbn);
            }
        }
    }

    @Override
    public List<Livre> findByAuteur(String auteur) throws SQLException {
        String sql = "SELECT * FROM livres WHERE auteur LIKE ? ORDER BY titre";
        List<Livre> livres = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + auteur + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findByTitre(String titre) throws SQLException {
        String sql = "SELECT * FROM livres WHERE titre LIKE ? ORDER BY titre";
        List<Livre> livres = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + titre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public List<Livre> findDisponibles() throws SQLException {
        String sql = "SELECT * FROM livres WHERE disponible = TRUE ORDER BY titre";
        List<Livre> livres = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(mapResultSetToLivre(rs));
            }
        }
        return livres;
    }

    @Override
    public Livre findByISBN(String isbn) throws SQLException {
        // Utilise la méthode findById héritée
        return findById(isbn);
    }

    @Override
    public boolean existsByISBN(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livres WHERE isbn = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet vers un objet Livre.
     * Évite la duplication de code.
     *
     * @param rs le ResultSet
     * @return un objet Livre
     * @throws SQLException si une erreur survient
     */
    private Livre mapResultSetToLivre(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setIsbn(rs.getString("isbn"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setAnneePublication(rs.getInt("annee_publication"));
        livre.setDisponible(rs.getBoolean("disponible"));
        return livre;
    }
}
