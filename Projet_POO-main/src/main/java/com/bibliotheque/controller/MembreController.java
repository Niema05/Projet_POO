package com.bibliotheque.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bibliotheque.model.Membre;
import com.bibliotheque.service.BibliothequeService;

import java.util.Optional;

/**
 * Controller pour la gestion des membres (CRUD + activation + historique)
 */
public class MembreController {

    /* ==================== COMPOSANTS FXML ==================== */

    @FXML
    private TableView<Membre> tableMembres;

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
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private CheckBox chkActif;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Button btnModifier, btnSupprimer, btnActiver, btnDesactiver, btnHistorique;

    @FXML
    private Label lblStatistiques;

    /* ==================== ATTRIBUTS ==================== */

    private BibliothequeService service;
    private ObservableList<Membre> listeMembres;
    private Membre membreSelectionne;

    /* ==================== INITIALISATION ==================== */

    @FXML
    public void initialize() {
        service = new BibliothequeService();
        listeMembres = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colActif.setCellValueFactory(new PropertyValueFactory<>("actif"));

        colActif.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean actif, boolean empty) {
                super.updateItem(actif, empty);
                if (empty || actif == null) {
                    setText(null);
                } else {
                    setText(actif ? "Oui" : "Non");
                    setStyle(actif ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });

        tableMembres.setItems(listeMembres);

        tableMembres.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> onMembreSelected(newVal));

        chargerMembres();
        afficherStatistiques();
        desactiverBoutons();
    }

    /* ==================== CRUD ==================== */

    @FXML
    private void handleAjouter() {
        try {
            Membre membre = new Membre(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    txtEmail.getText().trim(),
                    true
            );

            service.ajouterMembre(membre);
            afficherSucces("Membre ajouté avec succès");
            chargerMembres();
            viderFormulaire();
            afficherStatistiques();

        } catch (Exception e) {
            afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (membreSelectionne == null) {
            afficherAvertissement("Sélectionnez un membre");
            return;
        }

        try {
            membreSelectionne.setNom(txtNom.getText().trim());
            membreSelectionne.setPrenom(txtPrenom.getText().trim());
            membreSelectionne.setEmail(txtEmail.getText().trim());
            membreSelectionne.setActif(chkActif.isSelected());

            service.modifierMembre(membreSelectionne);
            afficherSucces("Membre modifié");
            chargerMembres();

        } catch (Exception e) {
            afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (membreSelectionne == null) {
            afficherAvertissement("Sélectionnez un membre");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Confirmer la suppression");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.supprimerMembre(membreSelectionne.getId());
            afficherSucces("Membre supprimé");
            chargerMembres();
            viderFormulaire();
            afficherStatistiques();
        }
    }

    /* ==================== ACTIVATION ==================== */

    @FXML
    private void handleActiver() {
        changerStatut(true);
    }

    @FXML
    private void handleDesactiver() {
        changerStatut(false);
    }

    private void changerStatut(boolean actif) {
        if (membreSelectionne == null) {
            afficherAvertissement("Sélectionnez un membre");
            return;
        }

        service.activerDesactiver(membreSelectionne.getId(), actif);
        afficherSucces("Statut mis à jour");
        chargerMembres();
        afficherStatistiques();
    }

    /* ==================== HISTORIQUE ==================== */

    @FXML
    private void handleHistorique() {
        if (membreSelectionne == null) {
            afficherAvertissement("Sélectionnez un membre");
            return;
        }

        service.getHistorique(membreSelectionne.getId());

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText("Historique des emprunts");
        info.setContentText("Fonctionnalité à compléter avec EmpruntService");
        info.showAndWait();
    }

    /* ==================== RECHERCHE ==================== */

    @FXML
    private void handleRechercher() {
        String motCle = txtRecherche.getText();

        if (motCle == null || motCle.isBlank()) {
            chargerMembres();
            return;
        }

        listeMembres.setAll(service.rechercherMembres(motCle));
    }

    /* ==================== UTILITAIRES ==================== */

    private void chargerMembres() {
        listeMembres.setAll(service.rechercherMembres());
    }

    private void onMembreSelected(Membre membre) {
        membreSelectionne = membre;

        if (membre != null) {
            txtNom.setText(membre.getNom());
            txtPrenom.setText(membre.getPrenom());
            txtEmail.setText(membre.getEmail());
            chkActif.setSelected(membre.isActif());
            activerBoutons();
        }
    }

    private void viderFormulaire() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        chkActif.setSelected(true);
        tableMembres.getSelectionModel().clearSelection();
        desactiverBoutons();
    }

    private void afficherStatistiques() {
        int total = service.rechercherMembres().size();
        int actifs = service.rechercherMembresActifs().size();
        lblStatistiques.setText("Total Membres: " + total + " | Actifs: " + actifs);
    }

    private void activerBoutons() {
        btnModifier.setDisable(false);
        btnSupprimer.setDisable(false);
        btnActiver.setDisable(false);
        btnDesactiver.setDisable(false);
        btnHistorique.setDisable(false);
    }

    private void desactiverBoutons() {
        btnModifier.setDisable(true);
        btnSupprimer.setDisable(true);
        btnActiver.setDisable(true);
        btnDesactiver.setDisable(true);
        btnHistorique.setDisable(true);
    }

    /* ==================== ALERTES ==================== */

    private void afficherSucces(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void afficherErreur(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void afficherAvertissement(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}

