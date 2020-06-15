package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.LobbySizeEvent;
import it.polimi.ingsw.Events.Client.LoginNameEvent;
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

    boolean isWaiting = false;
    int buttonClickedCounter = 0;

    GUIStagesManager stagesManager;

    public TextField getNameText() {
        return nameText;
    }

    public Text getErrorText() {
        return errorText;
    }

    public Text getSuccessText() {
        return successText;
    }

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    @FXML
    private AnchorPane startLoginScreen;

    @FXML
    private AnchorPane backGround;

    @FXML
    private AnchorPane serverUnavailable;

    @FXML
    private VBox errorVBox;

    @FXML
    private Text errorText;

    @FXML
    private VBox nameTextVBox;

    @FXML
    private HBox hBoxText;

    @FXML
    private TextField nameText;

    @FXML
    private VBox lobbyPartVBox;

    @FXML
    private VBox choosePlayerNumbers;

    @FXML
    private Text chooseText;

    @FXML
    private CheckBox two;

    @FXML
    private CheckBox three;

    @FXML
    private AnchorPane startButtonPart;

    @FXML
    private Button buttonStart;

    @FXML
    private VBox messageVBOX;

    @FXML
    private Text successText;

    @FXML
    private Button playButton;

    @FXML
    private AnchorPane lobbyCreation;

    @FXML
    private AnchorPane lobbyFound;

    @FXML
    private AnchorPane waitingPlayers;

    @FXML
    private AnchorPane waitingPlayer;

    @FXML
    void changeScreen(ActionEvent event) {
        buttonClickedCounter++;
        backGround.setVisible(true);
        playButton.setVisible(false);
        playButton.setDisable(true);

        if(!isWaiting) return;

        backGround.setVisible(false);
        backGround.setDisable(true);
        lobbyCreation.setVisible(true);
    }

    @FXML
    void handleThreeBox(MouseEvent event) {
        if (three.isSelected()) two.setSelected(false);
    }

    @FXML
    void handleTwoBox(MouseEvent event) {
        if (two.isSelected()) three.setSelected(false);
    }

    /**
     * Checks if the name in the box is correct
     * @param keyevent enter event
     */
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

    /**
     * Starts the new phase when the lobby is full
     * @param event
     */
    @FXML
    void getStarted(ActionEvent event) {
        if(two.isSelected()){ stagesManager.send(new LobbySizeEvent(2)); }
        else if(three.isSelected()){ stagesManager.send(new LobbySizeEvent(3)); }
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
    }

    public void setError(String message) {
        nameText.setText("");
        errorText.setText(message);
        errorText.setVisible(true);
    }

    /**
     * Wakes up a client in waiting list
     */
    public void wakeUpClient(){

        if(!isWaiting) return;

        lobbyCreation.setVisible(false);

        if(!backGround.isVisible()) {

            playButton.setDisable(false);
            playButton.setVisible(true);
            isWaiting = false;
            backGround.setDisable(false);

            if(buttonClickedCounter == 1) {

                buttonClickedCounter++;
                backGround.setVisible(true);
                playButton.setVisible(false);
                playButton.setDisable(true);
            }
        }
        nameText.setText("");
        errorText.setText("");
        errorText.setVisible(true);
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public void waitingPlayer() {
        backGround.setVisible(false);
        backGround.setDisable(true);
        waitingPlayer.setVisible(true);
    }

    public void waitingPlayers() {
        backGround.setVisible(false);
        backGround.setDisable(true);
        waitingPlayers.setVisible(true);
    }

    public void insertNumber() {
        successText.setText("");
        errorVBox.setVisible(false);
        nameTextVBox.setVisible(false);
        lobbyPartVBox.setVisible(true);
    }

    public void lobbyFull() {
        stagesManager.setUseDisconnectionScene(false);
        backGround.setVisible(true);
        backGround.setDisable(false);
        lobbyFound.setVisible(true);
    }

    public void serverUnavailable(){
        startLoginScreen.setDisable(true);
        startLoginScreen.setVisible(false);
        serverUnavailable.setVisible(true);
    }
}