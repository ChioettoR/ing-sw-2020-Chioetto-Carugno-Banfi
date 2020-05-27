package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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

    @FXML
    private Button rightButton;

    @FXML
    private Button centerButton;

    @FXML
    private Button leftButton;

    @FXML
    private StackPane stackPane;

    public StackPane getStackPane() {
        return stackPane;
    }

    public Button getRightButton() {
        return rightButton;
    }

    public Button getCenterButton() {
        return centerButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

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
