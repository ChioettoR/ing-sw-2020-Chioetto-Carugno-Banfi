package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Events.Client.ClientEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class GUIStagesManager extends Application {

    private static Client client;
    private static String ip;
    private static int port;
    Stage stage;
    GUIWinController winController = new GUIWinController();
    GUILoginStage guiLoginStage = new GUILoginStage();
    GUIDrawStage guiDrawStage = new GUIDrawStage();
    GUIRoundStage guiRoundStage = new GUIRoundStage();
    GUIPlayersManager guiPlayersManager = new GUIPlayersManager();
    boolean serverUp = false;
    GUIPhase guiPhase = GUIPhase.LOGIN;
    int timer = 6;
    int seconds = timer;
    Font looney;

    public GUIDrawStage getGuiDrawStage() {
        return guiDrawStage;
    }

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
        eventsCommunication.setGuiRoundStage(guiRoundStage);
        guiLoginStage.start(stage, this);
        try { client.run(); serverUp = true; }
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
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readMessage(message));
    }

    public void readRequest(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readRequest(message));
        else if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.readRequest(message));
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readMessage(message));
    }

    public void readError(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readError(message));
        else if(guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.readError(message));
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readError(message));
    }

    public void readDrawMessage(String draw) {
        Platform.runLater(() -> guiDrawStage.draw(draw));
    }

    @Override
    public void stop(){
        if(serverUp) client.closeConnection();
        System.exit(0);
    }

    public void lobbyInfo(String lobbyName, int lobbySize) {
        Platform.runLater(() -> guiLoginStage.lobbyInfo(lobbyName, lobbySize));
    }

    public void endLogin(ArrayList<String> names) {
        setNames(names);
        setDrawStage(stage, guiPlayersManager);
    }

    public static void launch(String ip, int port) {
        GUIStagesManager.ip = ip;
        GUIStagesManager.port = port;
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
        else if(messageID == 108 && guiPhase == GUIPhase.DRAW) roundTransition();
        else if(messageID == 114 && guiPhase == GUIPhase.DRAW) roundTransition();
        else if(messageID == 501 && guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.threeCardsShow());
        else if(messageID == 502 && guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.twoCardsShow());
        else if(messageID == 407 && guiPhase == GUIPhase.DRAW) Platform.runLater(() -> guiDrawStage.showAgain());
        else if(messageID == 306 && guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.resetButtons());
    }

    public void playerChosenCard(String playerName, String cardName) {
        Platform.runLater(() -> guiDrawStage.setCardsImages(playerName, cardName));
        guiPlayersManager.getPlayer(playerName).setCardName(cardName);
    }

    public void setNames(ArrayList<String> names) {
        for (String name : names) guiPlayersManager.addPlayer(name);
    }

    public void roundTransition() {
        guiPhase = GUIPhase.ROUND;
        Platform.runLater(() -> guiRoundStage.setUp(stage, this));
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> CountDown()));
        animation.setCycleCount(timer);
        animation.setOnFinished(event -> Platform.runLater(() -> guiRoundStage.start(stage)));
        animation.play();
    }

    private void CountDown() {
        Platform.runLater(() -> guiDrawStage.roundTransition(seconds));
        seconds--;
    }

    public void selectFirstPlayer(ArrayList<String> names) {
        guiDrawStage.selectFirstPlayer(names);
    }

    public void win(String winnerName){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Win/win.fxml"));
            Parent root = loader.load();
            winController = loader.getController();
            //winController.getTitle().setFont(Font.loadFont("Win/looney.ttf", 200));
            winController.getTitle().setText("The winner is: " + winnerName + "!");
            stage.setScene(new Scene(root, 600, 600));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

