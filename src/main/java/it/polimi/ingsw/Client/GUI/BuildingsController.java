package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class BuildingsController {

    @FXML
    private BorderPane pane;

    @FXML
    private ImageView baseBlock;

    @FXML
    private ImageView mediumBlock;

    @FXML
    private ImageView highBlock;

    @FXML
    private ImageView dome;

    public BorderPane getPane() {
        return pane;
    }

    public ImageView getBaseBlock() {
        return baseBlock;
    }

    public ImageView getMediumBlock() {
        return mediumBlock;
    }

    public ImageView getHighBlock() {
        return highBlock;
    }

    public ImageView getDome() {
        return dome;
    }
}
