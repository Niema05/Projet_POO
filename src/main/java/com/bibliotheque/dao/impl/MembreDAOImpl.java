package dao.impl;

import dao.MembreDAO;
import model.Membre;
import com.bibliotheque.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAOImpl implements MembreDAO {

    @Override
    public void save(Membre membre) {
        String sql = "INSERT INTO membres (nom,prenom, email, actif) VALUES (?,?,?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setBoolean(4, membre.isActif());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du membre", e);
        }
    }

    @Override
    public Membre findById(int id) {
        String sql = "SELECT * FROM membres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToMembre(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findById", e);
        }
        return null;
    }

    @Override
    public List<Membre> findAll() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapToMembre(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findAll", e);
        }
        return membres;
    }

    @Override
    public void update(Membre membre) {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setBoolean(4, membre.isActif());
            ps.setInt(5, membre.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur update", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM membres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur delete", e);
        }
    }

    @Override
    public Membre findByEmail(String email) {
        String sql = "SELECT * FROM membres WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapToMembre(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findByEmail", e);
        }
        return null;
    }

    @Override
    public List<Membre> findActifs() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres WHERE actif = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                membres.add(mapToMembre(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur findActifs", e);
        }
        return membres;
    }

    //  Méthode de mapping ResultSet → Membre
    private Membre mapToMembre(ResultSet rs) throws SQLException {
        Membre m = new Membre();
        m.setId(rs.getInt("id"));
        m.setNom(rs.getString("nom"));
        m.setId(rs.getInt("prenom"));
        m.setEmail(rs.getString("email"));
        m.setActif(rs.getBoolean("actif"));
        return m;
    }
}
