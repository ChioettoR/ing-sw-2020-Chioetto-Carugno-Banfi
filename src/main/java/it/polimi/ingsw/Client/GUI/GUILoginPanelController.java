package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
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
    boolean isWaiting = false;

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
    private AnchorPane lobbyFound;

    @FXML
    private AnchorPane lobbyCreation;

    @FXML
    private AnchorPane waitingPlayers;

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
    private VBox errorVBox;

    @FXML
    private VBox messageVBOX;

    @FXML
    private VBox nameTextVBox;

    @FXML
    private VBox choosePlayerNumbers;

    @FXML
    private VBox lobbyPartVBox;

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
        waitingPlayers.setVisible(true);
        lobbyFound.setVisible(true);
        lobbyCreation.setVisible(true);
        backGround.setVisible(true);
        playButton.setVisible(false);
        playButton.setDisable(true);
        if(isWaiting){
            backGround.setVisible(false);
            backGround.setDisable(true);
        }
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
            else if(string.split("\\s+").length>1) setError("Names longer than one word are not accepted");
            else stagesManager.send(new LoginNameEvent(string));
        }
    }

    @FXML
    void getStarted(ActionEvent event) {
        if(two.isSelected()){
            stagesManager.send(new LobbySizeEvent(3));
        }else if(three.isSelected()){
            stagesManager.send(new LobbySizeEvent(3));
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
        if(message.equals("Waiting for another player...")){
            backGround.setVisible(false);
            backGround.setDisable(true);
            lobbyCreation.setVisible(false);
            lobbyCreation.setDisable(true);
            lobbyFound.setVisible(false);
            lobbyFound.setDisable(true);
        }else if(message.equals("Waiting for other players...")){
            backGround.setVisible(false);
            backGround.setDisable(true);
            lobbyCreation.setVisible(false);
            lobbyCreation.setDisable(true);
            lobbyFound.setVisible(false);
            lobbyFound.setDisable(true);
        }
    }

    public void setRequest(String message) {
        nameText.setText("");
        nameText.setPromptText(message);
        nameText.setMaxWidth(210);
        if(nameText.getPromptText().equals("Insert lobby players number")){
            successText.setText("");
            errorVBox.setVisible(false);
            nameTextVBox.setVisible(false);
            lobbyPartVBox.setVisible(true);
        }
    }

    public void setError(String message) {
        nameText.setText("");
        errorText.setText(message);
        errorText.setVisible(true);
    }

    public void wakeUpClient(){
        backGround.setVisible(true);
        backGround.setDisable(false);
        nameText.setText("");
        errorText.setText("");
        errorText.setVisible(true);
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }
}