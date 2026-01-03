package com.biblio.controller;

import com.biblio.model.Livre;
import com.biblio.service.LivreService;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class LivreController {

    @FXML
    private TableView<Livre> tableLivres;

    private LivreService service = new LivreService();

    @FXML
    public void initialize() {
        tableLivres.getItems().addAll(service.listerLivres());
    }
}
