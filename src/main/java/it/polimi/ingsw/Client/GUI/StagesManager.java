package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Events.Client.ClientEvent;
import it.polimi.ingsw.Model.CardSimplified;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class StagesManager extends Application {

    private static Client client;
    private static String ip;
    private static int port;
    Stage stage;
    GUILoginStage guiLoginStage = new GUILoginStage();
    GUIDrawStage guiDrawStage = new GUIDrawStage();
    GUIPlayersManager guiPlayersManager = new GUIPlayersManager();
    boolean serverUp = false;
    GUIPhase guiPhase = GUIPhase.LOGIN;

    public GUIPlayersManager getGuiPlayersManager() {
        return guiPlayersManager;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        client = new Client(ip, port);
        client.setupGUI();
        GUIEventsCommunication eventsCommunication = (GUIEventsCommunication) client.getEventsCommunication();
        eventsCommunication.setStagesManager(this);
        eventsCommunication.setGuiLoginStage(guiLoginStage);
        eventsCommunication.setGuiDrawStage(guiDrawStage);
        guiLoginStage.start(stage, this);
        try { client.run();serverUp = true; }
        catch(Exception e) { guiLoginStage.serverUnavailable(); }
    }

    public void send(ClientEvent event) {
        client.update(event);
    }

    public void setDrawStage(Stage stage, GUIPlayersManager guiPlayersManager) {
        guiPhase = GUIPhase.DRAW;
        Platform.runLater(() -> guiDrawStage.start(stage,this, guiPlayersManager));
    }

    public void readMessage(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readMessage(message));
        else if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.readMessage(message));
    }

    public void readRequest(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readRequest(message));
        else if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.readRequest(message));
    }

    public void readError(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readError(message));
        else if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.readError(message));
    }

    public void readDrawMessage(String draw) {
        if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.draw(draw));
    }

    @Override
    public void stop(){
        if(serverUp) client.closeConnection();
    }

    public void lobbyInfo(String lobbyName, int lobbySize) {
        if(guiPhase == GUIPhase.LOGIN ) Platform.runLater(() -> guiLoginStage.lobbyInfo(lobbyName, lobbySize));
    }

    public void endLogin(ArrayList<String> names) {
        setNames(names);
        setDrawStage(stage, guiPlayersManager);
    }

    public static void launch(String ip, int port) throws IOException {
        StagesManager.ip = ip;
        StagesManager.port = port;
        Application.launch();
    }

    public void setDisconnectedScene() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Disconnection/disconnection.fxml"));
        Parent disconnection = loader.load();
        stage.setScene(new Scene(disconnection, 600, 600));
        stage.setResizable(false);
        stage.show();
    }

    public void messagesToDirect(int messageID) {
        if(messageID == 304) Platform.runLater(() -> guiLoginStage.waitingPlayer());
        else if(messageID == 302) Platform.runLater(() -> guiLoginStage.waitingPlayers());
        else if(messageID == 113) Platform.runLater(() -> guiLoginStage.insertNumber());
        else if(messageID == 414) Platform.runLater(() -> guiLoginStage.lobbyFull());
        else if(messageID == 108) roundTransition();
        else if(messageID == 114) roundTransition();
    }

    public void sendDeck(ArrayList<CardSimplified> cards) {
        if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.sendDeck(cards));
    }

    public void playerChosenCard(String playerName, String cardName) {
        if(guiPhase == GUIPhase.DRAW){
            Platform.runLater(() -> guiDrawStage.setCardsImages(playerName, cardName));
            guiPlayersManager.getPlayer(playerName).setCardName(cardName);
        }
    }

    public void sendCard() {
        if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.sendCard());
    }

    public void setNames(ArrayList<String> names) {
        for (String name : names) guiPlayersManager.addPlayer(name);
    }

    public void roundTransition() {
        guiPhase = GUIPhase.ROUND;
        guiDrawStage.roundTransition();
        //TODO: TIMER PER LA TRANSITION
    }
}
