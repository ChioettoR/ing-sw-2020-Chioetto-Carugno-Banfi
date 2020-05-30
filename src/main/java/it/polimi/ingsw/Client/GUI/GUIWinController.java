package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class GUIWinController {

    @FXML
    private AnchorPane winPane;

    @FXML
    private Text title;

    public Text getTitle() {
        return title;
    }
}