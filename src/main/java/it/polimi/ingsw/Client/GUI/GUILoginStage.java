package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUILoginStage {

    private GUILoginPanelController guiLoginPanelController;

    /**
     * Starts the login scene
     * @param stage stage of the game
     * @param stagesManager handles the different stages of the match
     */
    public void start(Stage stage, GUIStagesManager stagesManager) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Login/login.fxml"));
        Parent root = loader.load();
        guiLoginPanelController = loader.getController();
        guiLoginPanelController.setStagesManager(stagesManager);
        stage.setScene(new Scene(root, 600, 600));
        stage.setResizable(false);
        stage.show();
    }

    public void readMessage(String message) {
        guiLoginPanelController.setMessage(message);
    }

    public void readRequest(String message) {
        guiLoginPanelController.setRequest(message);
    }

    public void readError(String message) {
        guiLoginPanelController.setError(message);
    }

    public void lobbyInfo(String lobbyName, int lobbySize) {
        String message = "Entered in " + lobbyName + "'s lobby of " + lobbySize + " players";
        guiLoginPanelController.setMessage(message);
    }

    public void waitWake() {
        guiLoginPanelController.wakeUpClient();
    }

    public void waitError() {
        guiLoginPanelController.setWaiting(true);
    }

    public void waitingPlayer() {
        guiLoginPanelController.waitingPlayer();
    }

    public void waitingPlayers() {
        guiLoginPanelController.waitingPlayers();
    }

    public void insertNumber() {
        guiLoginPanelController.insertNumber();
    }

    public void lobbyFull() {
        guiLoginPanelController.lobbyFull();
    }

    public void serverUnavailable(){
        guiLoginPanelController.serverUnavailable();
    }


}
