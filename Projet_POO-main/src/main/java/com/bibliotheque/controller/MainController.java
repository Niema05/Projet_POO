package com.bibliotheque.controller;

import com.bibliotheque.dao.impl.LivreDAOImpl;
import com.bibliotheque.dao.impl.MembreDAOImpl;
import com.bibliotheque.dao.impl.EmpruntDAOImpl;
import com.bibliotheque.service.BibliothequeService;
import com.bibliotheque.service.EmpruntService;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert;

 

/**
 * Contrôleur principal de l'application JavaFX.
 */
public class MainController {

    @FXML
    private TabPane tabPane;

    private BibliothequeService bibliothequeService;
    private EmpruntService empruntService;

    /**
     * Initialise le contrôleur et charge les données.
     */
    @FXML
    public void initialize() {
        try {
            // Initialiser les services
            var livreDAO = new LivreDAOImpl();
            var membreDAO = new MembreDAOImpl();
            var empruntDAO = new EmpruntDAOImpl();

            bibliothequeService = new BibliothequeService(livreDAO, membreDAO);
            empruntService = new EmpruntService(empruntDAO, livreDAO, membreDAO);
        } catch (Exception e) {
            afficherErreur("Erreur d'initialisation", "Impossible d'initialiser l'application : " + e.getMessage());
        }
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param titre   le titre de l'alerte
     * @param message le message d'erreur
     */
    protected void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte de succès.
     *
     * @param titre   le titre de l'alerte
     * @param message le message de succès
     */
    protected void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getters pour les services (utilisés par les contrôleurs enfants)

    public BibliothequeService getBibliothequeService() {
        return bibliothequeService;
    }

    public EmpruntService getEmpruntService() {
        return empruntService;
    }
}
