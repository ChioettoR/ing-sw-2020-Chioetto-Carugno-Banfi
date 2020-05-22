package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.regex.Pattern;

public class GUILoginPanelController {

    boolean isLogin = true;
    boolean waiting = false;

    StagesManager stagesManager;

    public TextField getNameText() {
        return nameText;
    }

    public Text getErrorText() {
        return errorText;
    }

    public Text getSuccessText() {
        return successText;
    }

    public void setStagesManager(StagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    @FXML
    private CheckBox two;

    @FXML
    private CheckBox three;

    @FXML
    private Button startButton;

    @FXML
    private VBox loginPanel;

    @FXML
    private AnchorPane backGround;

    @FXML
    private TextField nameText;

    @FXML
    private Text errorText;

    @FXML
    private VBox lobbyPart;

    @FXML
    private VBox vBox;

    @FXML
    private HBox hBoxText;

    @FXML
    private Text successText;

    @FXML
    private Text chooseText;

    @FXML
    private AnchorPane startLoginScreen;

    @FXML
    private AnchorPane startButtonPart;

    @FXML
    private Button buttonStart;

    @FXML
    private Button playButton;

    @FXML
    void changeScreen(ActionEvent event) {
        backGround.setVisible(true);
        playButton.setVisible(false);
        playButton.setDisable(true);
    }

    @FXML
    void handleThreeBox(MouseEvent event) {
        if (three.isSelected())
            two.setSelected(false);
    }

    @FXML
    void handleTwoBox(MouseEvent event) {
        if (two.isSelected())
            three.setSelected(false);
    }

    @FXML
    void checkName(KeyEvent keyevent) {
        String string;
        if (keyevent.getCode() == KeyCode.ENTER) {
            errorText.setVisible(false);
            string = nameText.getText();
            if(string.isBlank()) setError("Insert a valid string");
            else if (isNumeric(string)) setError("Your name can't be a number. Please, insert a valid name");
                //stagesManager.send(new LobbySizeEvent(Integer.parseInt(string)));
            else if(string.split("\\s+").length>1) setError("Names longer than one word are not accepted");
            else stagesManager.send(new LoginNameEvent(string));
        }
    }

    @FXML
    void getStarted(ActionEvent event) {
        if(two.isSelected()){
            stagesManager.send(new LobbySizeEvent(3));
            lobbyPart.setVisible(false);
            startButtonPart.setVisible(false);
            hBoxText.setDisable(false);
            hBoxText.setVisible(true);
            nameText.setPromptText("");
            errorText.setVisible(false);
        }else if(three.isSelected()){
            stagesManager.send(new LobbySizeEvent(3));
            lobbyPart.setVisible(false);
            startButtonPart.setVisible(false);
            hBoxText.setDisable(false);
            hBoxText.setVisible(true);
            nameText.setPromptText("");
            errorText.setVisible(false);
        }
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        return pattern.matcher(strNum).matches();
    }

    public void setMessage(String message) {
        successText.setText(message);
        successText.setVisible(true);
    }

    public void setRequest(String message) {
        nameText.setText("");
        nameText.setPromptText(message);
        nameText.setMaxWidth(210);
        vBox.setVisible(true);
        lobbyPart.setVisible(false);
        if(nameText.getPromptText().equals("Insert lobby players number")){
            startButtonPart.setVisible(true);
            lobbyPart.setVisible(true);
            hBoxText.setVisible(false);
            hBoxText.setDisable(true);
        }
    }

    public void setError(String message) {
        nameText.setText("");
        errorText.setText(message);
        errorText.setVisible(true);
    }
}