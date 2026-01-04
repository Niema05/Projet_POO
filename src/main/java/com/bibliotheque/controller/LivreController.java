package com.bibliotheque.controller;

import com.bibliotheque.model.Livre;
import com.bibliotheque.service.LivreService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.Collections;
import java.util.List;

public class LivreController {

    @FXML
    private TableView<Livre> tableLivres;

    private final LivreService service = new LivreService();

    @FXML
    public void initialize() {
        rafraichirTable();
    }

    @FXML
    public void rafraichirTable() {
        List<Livre> livres = service.listerLivres();
        if (livres == null) {
            livres = Collections.emptyList();
        }
        tableLivres.setItems(FXCollections.observableArrayList(livres));
    }
}
