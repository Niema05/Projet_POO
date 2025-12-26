package com.bibliotheque.controller;

import com.bibliotheque.exception.ValidationException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur pour la gestion des livres.
 */
public class LivreController {

    @FXML
    private TableView<Livre> tableViewLivres;
    @FXML
    private TableColumn<Livre, String> colISBN;
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, Integer> colAnnee;
    @FXML
    private TableColumn<Livre, Boolean> colDisponible;

    @FXML
    private TextField tfISBN;
    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfAuteur;
    @FXML
    private Spinner<Integer> spinnerAnnee;
    @FXML
    private CheckBox cbDisponible;

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
        chargerLivres();
    }

    /**
     * Charge tous les livres dans le tableau.
     */
    private void chargerLivres() {
        try {
            List<Livre> livres = service.getTousLesLivres();
            ObservableList<Livre> data = FXCollections.observableArrayList(livres);
            tableViewLivres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les livres : " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau livre.
     */
    @FXML
    public void handleAjouter() {
        try {
            String isbn = tfISBN.getText();
            String titre = tfTitre.getText();
            String auteur = tfAuteur.getText();
            int annee = spinnerAnnee.getValue();
            boolean disponible = cbDisponible.isSelected();

            Livre livre = new Livre(isbn, titre, auteur, annee, disponible);
            service.ajouterLivre(livre);

            afficherSucces("Succès", "Livre ajouté avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Modifie le livre sélectionné.
     */
    @FXML
    public void handleModifier() {
        Livre selected = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un livre");
            return;
        }

        try {
            selected.setTitre(tfTitre.getText());
            selected.setAuteur(tfAuteur.getText());
            selected.setAnneePublication(spinnerAnnee.getValue());
            selected.setDisponible(cbDisponible.isSelected());

            service.modifierLivre(selected);

            afficherSucces("Succès", "Livre modifié avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (ValidationException e) {
            afficherErreur("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Supprime le livre sélectionné.
     */
    @FXML
    public void handleSupprimer() {
        Livre selected = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherErreur("Erreur", "Veuillez sélectionner un livre");
            return;
        }

        try {
            service.supprimerLivre(selected.getIsbn());
            afficherSucces("Succès", "Livre supprimé avec succès!");
            nettoyerFormulaire();
            chargerLivres();
        } catch (SQLException e) {
            afficherErreur("Erreur de base de données", e.getMessage());
        }
    }

    /**
     * Recherche des livres.
     */
    @FXML
    public void handleRechercher() {
        String critere = tfRecherche.getText();
        if (critere.isEmpty()) {
            chargerLivres();
            return;
        }

        try {
            List<Livre> livres = service.rechercherLivres(critere);
            ObservableList<Livre> data = FXCollections.observableArrayList(livres);
            tableViewLivres.setItems(data);
        } catch (SQLException e) {
            afficherErreur("Erreur de recherche", e.getMessage());
        }
    }

    /**
     * Nettoie le formulaire.
     */
    private void nettoyerFormulaire() {
        tfISBN.clear();
        tfTitre.clear();
        tfAuteur.clear();
        cbDisponible.setSelected(true);
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
