package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Membre;
import service.BibliothequeService;

import java.util.List;

public class MembreController {

    private final BibliothequeService service = new BibliothequeService();

    /* ============================
       Champs JavaFX (FXML)
       ============================ */

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtEmail;

    @FXML
    private CheckBox chkActif;

    @FXML
    private TableView<Membre> tableMembres;

    @FXML
    private Label lblMessage;

    /* ============================
       Ajouter un membre
       ============================ */
    @FXML
    public void handleAjouter() {
        try {
            Membre membre = new Membre();
            membre.setNom(txtNom.getText());
            membre.setPrenom(txtPrenom.getText());
            membre.setEmail(txtEmail.getText());

            service.ajouterMembre(membre);

            lblMessage.setText("Membre ajouté avec succès");
            rafraichirTable();

        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
        }
    }

    /* ============================
       Modifier un membre
       ============================ */
    @FXML
    public void handleModifier() {
        try {
            int id = Integer.parseInt(txtId.getText());

            Membre membre = new Membre();
            membre.setId(id);
            membre.setNom(txtNom.getText());
            membre.setPrenom(txtPrenom.getText());
            membre.setEmail(txtEmail.getText());
            membre.setActif(chkActif.isSelected());

            service.modifierMembre(membre);

            lblMessage.setText("Membre modifié avec succès");
            rafraichirTable();

        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
        }
    }

    /* ============================
       Activer / Désactiver
       ============================ */
    @FXML
    public void handleActiverDesactiver() {
        try {
            int id = Integer.parseInt(txtId.getText());
            boolean actif = chkActif.isSelected();

            service.activerDesactiver(id, actif);

            lblMessage.setText("Statut du membre mis à jour");
            rafraichirTable();

        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
        }
    }

    /* ============================
       Afficher tous les membres
       ============================ */
    @FXML
    public void handleAfficherTous() {
        List<Membre> membres = service.rechercherMembres();
        tableMembres.getItems().setAll(membres);
    }

    /* ============================
       Afficher membres actifs
       ============================ */
    @FXML
    public void handleAfficherActifs() {
        List<Membre> membres = service.rechercherMembresActifs();
        tableMembres.getItems().setAll(membres);
    }

    /* ============================
       Historique des emprunts
       ============================ */
    @FXML
    public void handleAfficherHistorique() {
        try {
            int id = Integer.parseInt(txtId.getText());
            service.getHistorique(id);

            lblMessage.setText("Affichage de l'historique en cours de développement");

        } catch (Exception e) {
            lblMessage.setText(e.getMessage());
        }
    }

    /* ============================
       Méthode utilitaire
       ============================ */
    private void rafraichirTable() {
        tableMembres.getItems().setAll(service.rechercherMembres());
    }
}

