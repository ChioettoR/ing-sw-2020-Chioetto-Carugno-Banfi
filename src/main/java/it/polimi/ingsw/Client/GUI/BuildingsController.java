package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    @FXML
    private ImageView imageRound1;

    @FXML
    private ImageView nameBar1;

    @FXML
    private Text nameText1;

    @FXML
    private ImageView frame1;

    @FXML
    private Button buttonInfo1;

    @FXML
    private ImageView imageRound2;

    @FXML
    private ImageView nameBar2;

    @FXML
    private Text nameText2;

    @FXML
    private ImageView frame2;

    @FXML
    private Button buttonInfo2;

    @FXML
    private ImageView imageRound3;

    @FXML
    private ImageView nameBar3;

    @FXML
    private Text nameText3;

    @FXML
    private ImageView frame3;

    @FXML
    private Button buttonInfo3;

    @FXML
    private StackPane infoStackPane;

    @FXML
    private ImageView infoGodImage;

    @FXML
    private Button closeInfoButton;

    @FXML
    private Text infoGodName;

    @FXML
    private Text infoDescription;

    @FXML
    private StackPane rightPane;

    @FXML
    private BorderPane borderPaneCards;

    @FXML
    private Text messageText;

    @FXML
    private Text errorText;


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

    public ImageView getImageRound1() {
        return imageRound1;
    }

    public ImageView getNameBar1() {
        return nameBar1;
    }

    public Text getNameText1() {
        return nameText1;
    }

    public ImageView getFrame1() {
        return frame1;
    }

    public Button getButtonInfo1() {
        return buttonInfo1;
    }

    public ImageView getImageRound2() {
        return imageRound2;
    }

    public ImageView getNameBar2() {
        return nameBar2;
    }

    public Text getNameText2() {
        return nameText2;
    }

    public ImageView getFrame2() {
        return frame2;
    }

    public Button getButtonInfo2() {
        return buttonInfo2;
    }

    public ImageView getImageRound3() {
        return imageRound3;
    }

    public ImageView getNameBar3() {
        return nameBar3;
    }

    public Text getNameText3() {
        return nameText3;
    }

    public ImageView getFrame3() {
        return frame3;
    }

    public Button getButtonInfo3() {
        return buttonInfo3;
    }

    public StackPane getInfoStackPane() {
        return infoStackPane;
    }

    public ImageView getInfoGodImage() {
        return infoGodImage;
    }

    public Button getCloseInfoButton() {
        return closeInfoButton;
    }

    public Text getInfoGodName() {
        return infoGodName;
    }

    public Text getInfoDescription() {
        return infoDescription;
    }

    public StackPane getRightPane() {
        return rightPane;
    }

    public BorderPane getBorderPaneCards() {
        return borderPaneCards;
    }

    public Text getMessageText() {
        return messageText;
    }

    public Text getErrorText() {
        return errorText;
    }
}
