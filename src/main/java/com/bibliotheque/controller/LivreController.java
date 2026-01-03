package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Livre;
import service.BibliothequeService;
import exception.ValidationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des livres dans l'interface JavaFX
 */
public class LivreController {
    
    // Composants FXML - TableView
    @FXML
    private TableView<Livre> tableViewLivres;
    @FXML
    private TableColumn<Livre, String> colIsbn;
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, Integer> colAnnee;
    @FXML
    private TableColumn<Livre, Boolean> colDisponible;
    
    // Composants FXML - Formulaire
    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitre;
    @FXML
    private TextField txtAuteur;
    @FXML
    private TextField txtAnnee;
    @FXML
    private TextField txtRecherche;
    
    // Composants FXML - Statistiques
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblDisponibles;
    @FXML
    private Label lblEmpruntes;
    
    // Boutons
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRechercher;
    @FXML
    private Button btnRafraichir;
    @FXML
    private Button btnNouveau;
    
    // Service
    private BibliothequeService service;
    private ObservableList<Livre> livresObservable;
    
    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        service = new BibliothequeService();
        livresObservable = FXCollections.observableArrayList();
        
        // Configuration des colonnes du TableView
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        
        // Lier les données au TableView
        tableViewLivres.setItems(livresObservable);
        
        // Listener pour la sélection dans le tableau
        tableViewLivres.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    afficherLivre(newSelection);
                }
            }
        );
        
        // Charger les données initiales
        chargerTousLesLivres();
        rafraichirStatistiques();
    }
    
    /**
     * Gère l'ajout d'un nouveau livre
     */
    @FXML
    private void handleAjouter() {
        try {
            Livre livre = creerLivreDepuisFormulaire();
            service.ajouterLivre(livre);
            
            afficherSucces("Livre ajouté avec succès");
            viderFormulaire();
            chargerTousLesLivres();
            rafraichirStatistiques();
            
        } catch (ValidationException e) {
            afficherErreur("Validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur base de données", e.getMessage());
        }
    }
    
    /**
     * Gère la modification d'un livre existant
     */
    @FXML
    private void handleModifier() {
        Livre selection = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un livre à modifier");
            return;
        }
        
        try {
            Livre livre = creerLivreDepuisFormulaire();
            livre.setDisponible(selection.isDisponible()); // Conserver le statut
            service.modifierLivre(livre);
            
            afficherSucces("Livre modifié avec succès");
            chargerTousLesLivres();
            
        } catch (ValidationException e) {
            afficherErreur("Validation", e.getMessage());
        } catch (SQLException e) {
            afficherErreur("Erreur base de données", e.getMessage());
        }
    }
    
    /**
     * Gère la suppression d'un livre
     */
    @FXML
    private void handleSupprimer() {
        Livre selection = tableViewLivres.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un livre à supprimer");
            return;
        }
        
        // Confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer le livre ?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer : " + selection.getTitre());
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.supprimerLivre(selection.getIsbn());
                afficherSucces("Livre supprimé avec succès");
                viderFormulaire();
                chargerTousLesLivres();
                rafraichirStatistiques();
                
            } catch (SQLException e) {
                afficherErreur("Erreur base de données", e.getMessage());
            }
        }
    }
    
    /**
     * Gère la recherche de livres
     */
    @FXML
    private void handleRechercher() {
        String recherche = txtRecherche.getText().trim();
        
        if (recherche.isEmpty()) {
            chargerTousLesLivres();
            return;
        }
        
        try {
            List<Livre> resultats = service.rechercherLivres(recherche);
            livresObservable.clear();
            livresObservable.addAll(resultats);
            
            if (resultats.isEmpty()) {
                afficherInformation("Recherche", "Aucun livre trouvé");
            }
            
        } catch (SQLException e) {
            afficherErreur("Erreur recherche", e.getMessage());
        }
    }
    
    /**
     * Rafraîchit la liste complète des livres
     */
    @FXML
    private void handleRafraichir() {
        chargerTousLesLivres();
        rafraichirStatistiques();
        txtRecherche.clear();
        afficherInformation("Rafraîchissement", "Liste mise à jour");
    }
    
    /**
     * Prépare le formulaire pour un nouveau livre
     */
    @FXML
    private void handleNouveau() {
        viderFormulaire();
        tableViewLivres.getSelectionModel().clearSelection();
    }
    
    /**
     * Charge tous les livres depuis la base de données
     */
    private void chargerTousLesLivres() {
        try {
            List<Livre> livres = service.getTousLesLivres();
            livresObservable.clear();
            livresObservable.addAll(livres);
        } catch (SQLException e) {
            afficherErreur("Erreur chargement", e.getMessage());
        }
    }
    
    /**
     * Rafraîchit les statistiques affichées
     */
    private void rafraichirStatistiques() {
        try {
            int[] stats = service.getStatistiques();
            lblTotal.setText(String.valueOf(stats[0]));
            lblDisponibles.setText(String.valueOf(stats[1]));
            lblEmpruntes.setText(String.valueOf(stats[2]));
        } catch (SQLException e) {
            afficherErreur("Erreur statistiques", e.getMessage());
        }
    }
    
    /**
     * Crée un objet Livre à partir des données du formulaire
     */
    private Livre creerLivreDepuisFormulaire() throws ValidationException {
        try {
            String isbn = txtIsbn.getText().trim();
            String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            int annee = Integer.parseInt(txtAnnee.getText().trim());
            
            return new Livre(isbn, titre, auteur, annee);
            
        } catch (NumberFormatException e) {
            throw new ValidationException("L'année doit être un nombre valide");
        }
    }
    
    /**
     * Affiche un livre dans le formulaire
     */
    private void afficherLivre(Livre livre) {
        txtIsbn.setText(livre.getIsbn());
        txtTitre.setText(livre.getTitre());
        txtAuteur.setText(livre.getAuteur());
        txtAnnee.setText(String.valueOf(livre.getAnneePublication()));
    }
    
    /**
     * Vide tous les champs du formulaire
     */
    private void viderFormulaire() {
        txtIsbn.clear();
        txtTitre.clear();
        txtAuteur.clear();
        txtAnnee.clear();
    }
    
    // Méthodes d'affichage de messages
    
    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherAvertissement(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherInformation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
