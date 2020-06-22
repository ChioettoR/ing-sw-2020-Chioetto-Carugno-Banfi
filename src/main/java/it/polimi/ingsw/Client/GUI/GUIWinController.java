package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GUIWinController {

    public BorderPane winPane;
    @FXML
    private Text winnerBanner;

    public void setWinnerBanner(String winnerName) {
        final Font looney = Font.loadFont(getClass().getResourceAsStream("/Win/looney.ttf"), 40);
        winnerBanner.setFont(looney);
        String name = winnerName.toLowerCase();
        winnerBanner.setText(name + " won!");
    }
}