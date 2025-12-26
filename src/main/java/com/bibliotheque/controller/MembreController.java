package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Membre;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur pour la gestion des membres.
 */
public class MembreController {

    @FXML
    private TableView<Membre> tableViewMembres;
    @FXML
    private TableColumn<Membre, Integer> colId;
    @FXML
    private TableColumn<Membre, String> colNom;
    @FXML
    private TableColumn<Membre, String> colPrenom;
    @FXML
    private TableColumn<Membre, String> colEmail;
    @FXML
    private TableColumn<Membre, Boolean> colActif;
    @FXML
    private TableColumn<Membre, LocalDate> colDateInscription;

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfEmail;
    @FXML
    private CheckBox cbActif;

    @FXML
    private TextField tfRecherche;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRechercher;

    private BibliothequeService service;

    /**
     * Définit le service.
     *
     * @param service le service de bibliothèque
     */
    public void setService(BibliothequeService service) {
        this.service = service;
        chargerMembres();
    }

    /**
     * Charge tous les membres dans le tableau.
     */
    private void chargerMembres() {
        try {
            List<Membre> membres = service.getTousLesMembres();
            ObservableList<Membre> data = FXCollections.observableArrayList(membres);
            tableViewMembres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les membres : " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau membre.
     */
    @FXML
    public void handleAjouter() {
        try {
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String email = tfEmail.getText();
            boolean actif = cbActif.isSelected();

            Membre membre = new Membre(nom, prenom, email, actif, LocalDate.now());
            service.ajouterMembre(membre);

            afficherSucces("Succès", "Membre ajouté avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Modifie le membre sélectionné.
     */
    @FXML
    public void handleModifier() {
        Membre selected = tableViewMembres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un membre");
            return;
        }

        try {
            selected.setNom(tfNom.getText());
            selected.setPrenom(tfPrenom.getText());
            selected.setEmail(tfEmail.getText());
            selected.setActif(cbActif.isSelected());

            service.modifierMembre(selected);

            afficherSucces("Succès", "Membre modifié avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Supprime le membre sélectionné.
     */
    @FXML
    public void handleSupprimer() {
        Membre selected = tableViewMembres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un membre");
            return;
        }

        try {
            // Dans une vraie application, on pourrait avoir une méthode supprimant le membre
            // Pour l'instant, on le désactive
            service.activerDesactiverMembre(selected.getId(), false);
            afficherSucces("Succès", "Membre désactivé avec succès!");
            nettoyerFormulaire();
            chargerMembres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Recherche des membres.
     */
    @FXML
    public void handleRechercher() {
        String critere = tfRecherche.getText();
        if (critere.isEmpty()) {
            chargerMembres();
            return;
        }

        try {
            List<Membre> membres = service.rechercherMembres(critere);
            ObservableList<Membre> data = FXCollections.observableArrayList(membres);
            tableViewMembres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    /**
     * Nettoie le formulaire.
     */
    private void nettoyerFormulaire() {
        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        cbActif.setSelected(true);
    }

    /**
     * Affiche une alerte d'erreur.
     */
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de succès.
     */
    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
