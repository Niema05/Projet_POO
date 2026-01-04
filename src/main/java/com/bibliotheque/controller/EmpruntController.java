package com.bibliotheque.controller;

import com.bibliotheque.exception.LimiteEmpruntDepasseeException;
import com.bibliotheque.exception.LivreIndisponibleException;
import com.bibliotheque.exception.MembreInactifException;
import com.bibliotheque.model.Emprunt;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.service.EmpruntService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;


public class EmpruntController {

    @FXML
    private TableView<Emprunt> tableViewEmprunts;
    @FXML
    private TableColumn<Emprunt, Integer> colId;
    @FXML
    private TableColumn<Emprunt, String> colLivre;
    @FXML
    private TableColumn<Emprunt, String> colMembre;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateEmprunt;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetourPrevue;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetourEffective;
    @FXML
    private TableColumn<Emprunt, Double> colPenalite;

    @FXML
    private ComboBox<String> comboLivres;
    @FXML
    private ComboBox<String> comboMembres;
    @FXML
    private Button btnEmprunter;
    @FXML
    private Button btnRetourner;
    @FXML
    private Button btnAfficherEnCours;
    @FXML
    private Button btnAfficherEnRetard;
    @FXML
    private Button btnAfficherTous;

    private BibliothequeService bibliothequeService;
    private EmpruntService empruntService;


    public void setServices(BibliothequeService bibliothequeService, EmpruntService empruntService) {
        this.bibliothequeService = bibliothequeService;
        this.empruntService = empruntService;
        chargerDonnees();
    }


    private void chargerDonnees() {
        try {
            chargerLivres();
            chargerMembres();
            chargerEmprunts();
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", e.getMessage());
        }
    }

    private void chargerLivres() throws SQLException {
        var livres = bibliothequeService.getTousLesLivres();
        ObservableList<String> items = FXCollections.observableArrayList();
        livres.forEach(l -> items.add(l.getIsbn() + " - " + l.getTitre()));
        comboLivres.setItems(items);
    }

    private void chargerMembres() throws SQLException {
        var membres = bibliothequeService.getTousLesMembres();
        ObservableList<String> items = FXCollections.observableArrayList();
        membres.forEach(m -> items.add(m.getId() + " - " + m.getNomComplet()));
        comboMembres.setItems(items);
    }

    private void chargerEmprunts() throws SQLException {
        var emprunts = empruntService.getTousEmprunt();
        ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
        tableViewEmprunts.setItems(data);
    }


    @FXML
    public void handleEmprunter() {
        try {
            String livreStr = comboLivres.getValue();
            String membreStr = comboMembres.getValue();

            if (livreStr == null || membreStr == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un livre et un membre");
                return;
            }

            String isbn = livreStr.split(" - ")[0];
            int membreId = Integer.parseInt(membreStr.split(" - ")[0]);

            empruntService.emprunterLivre(isbn, membreId);
            afficherSucces("Succès", "Livre emprunté avec succès!");
            chargerEmprunts();
            chargerLivres();
        } catch (MembreInactifException | LivreIndisponibleException | LimiteEmpruntDepasseeException e) {
            afficherErreur("Erreur", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    @FXML
    public void handleRetourner() throws LivreIndisponibleException, MembreInactifException {
        Emprunt selected = tableViewEmprunts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un emprunt");
            return;
        }

        try {
            empruntService.retournerLivre(selected.getLivre().getIsbn(),selected.getMembre().getId());
            afficherSucces("Succès", "Livre retourné avec succès!");
            chargerEmprunts();
            chargerLivres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    @FXML
    public void handleAfficherEnCours() {
        try {
            var emprunts = empruntService.getEmpruntsEnCours();
            ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
            tableViewEmprunts.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }


    @FXML
    public void handleAfficherEnRetard() {
        try {
            var emprunts = empruntService.getEmpruntEnRetard();
            ObservableList<Emprunt> data = FXCollections.observableArrayList(emprunts);
            tableViewEmprunts.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }

    
    @FXML
    public void handleAfficherTous() {
        try {
            chargerEmprunts();
        } catch (SQLException e) {
            afficherErreur("Erreur", e.getMessage());
        }
    }


    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
