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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class GUIStagesManager extends Application {

    private static Client client;
    private Stage stage;
    private final GUIIpStage guiIpStage = new GUIIpStage();
    private final GUILoginStage guiLoginStage = new GUILoginStage();
    private final GUIPickCardStage guiPickCardStage = new GUIPickCardStage();
    private final GUIRoundStage guiRoundStage = new GUIRoundStage();
    private final GUIPlayersManager guiPlayersManager = new GUIPlayersManager();
    private boolean serverUp = false;
    private GUIPhase guiPhase = GUIPhase.LOGIN;
    private final int timer = 6;
    private int seconds = timer;
    private boolean useDisconnectionScene = true;
    private Timeline animation;


    public void setUseDisconnectionScene(boolean useDisconnectionScene) {
        this.useDisconnectionScene = useDisconnectionScene;
    }

    public GUIPickCardStage getGuiPickCardStage() {
        return guiPickCardStage;
    }

    public GUIPlayersManager getGuiPlayersManager() {
        return guiPlayersManager;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        guiIpStage.start(stage, this);
    }

    public void send(ClientEvent event) {
        client.update(event);
    }

    public void setPickCardStage(Stage stage, GUIPlayersManager guiPlayersManager) {
        guiPhase = GUIPhase.PICKCARD;
        Platform.runLater(() -> guiPickCardStage.start(stage,this, guiPlayersManager));
    }

    /**
     * Redirects the messages from the server to the current stage
     * @param  message to redirect
     */
    public void readMessage(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readMessage(message));
        else if(guiPhase == GUIPhase.PICKCARD) Platform.runLater(() -> guiPickCardStage.readMessage(message));
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readMessage(message));
    }

    /**
     * Redirects the requests from the server to the current stage
     * @param message to redirect
     */
    public void readRequest(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readRequest(message));
        else if(guiPhase == GUIPhase.PICKCARD) Platform.runLater(() -> guiPickCardStage.readRequest(message));
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readMessage(message));
    }

    /**
     * Redirects the errors from the server to the current stage
     * @param message to redirect
     */
    public void readError(String message) {
        if(guiPhase == GUIPhase.LOGIN) Platform.runLater(() -> guiLoginStage.readError(message));
        else if(guiPhase == GUIPhase.PICKCARD) Platform.runLater(() -> guiPickCardStage.readError(message));
        else if(guiPhase == GUIPhase.ROUND) Platform.runLater(() -> guiRoundStage.readError(message));
    }

    /**
     * Redirects the message to the pick card stage
     * @param choose string of the pick
     */
    public void readChooseCardMessage(String choose) {
        Platform.runLater(() -> guiPickCardStage.showFullDeck(choose));
    }

    @Override
    public void stop(){
        if(serverUp) client.closeConnection();
        System.exit(0);
    }

    /**
     * Redirects the message to the login stage
     * @param lobbyName name of the creator of the lobby
     * @param lobbySize size of the lobby
     */
    public void lobbyInfo(String lobbyName, int lobbySize) {
        Platform.runLater(() -> guiLoginStage.lobbyInfo(lobbyName, lobbySize));
    }

    /**
     * Sets the names and invokes the transition for the new stage
     * @param names names to set
     */
    public void endLogin(ArrayList<String> names) {
        setNames(names);
        setPickCardStage(stage, guiPlayersManager);
    }

    /**
     * Starts the GUI
     */
    public static void launch() {
        Application.launch();
    }

    public void setDisconnectedScene() throws IOException{
        if(animation != null) animation.stop();
        if(!useDisconnectionScene) return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Disconnection/disconnection.fxml"));
        Parent disconnection = loader.load();
        stage.setScene(new Scene(disconnection, 600, 600));
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Handles the special messages to apply changes in the scenes
     * @param messageID id of the message
     */
    public void messagesToDirect(int messageID) {
        if(messageID == 304) Platform.runLater(guiLoginStage::waitingPlayer);
        else if(messageID == 302) Platform.runLater(guiLoginStage::waitingPlayers);
        else if(messageID == 113) Platform.runLater(guiLoginStage::insertNumber);
        else if(messageID == 414) Platform.runLater(guiLoginStage::lobbyFull);
        else if(messageID == 119 && guiPhase == GUIPhase.PICKCARD) roundTransition();
        else if(messageID == 118 && guiPhase == GUIPhase.PICKCARD) roundTransition();
        else if(messageID == 501 && guiPhase == GUIPhase.PICKCARD) Platform.runLater(guiPickCardStage::threeCardsShow);
        else if(messageID == 502 && guiPhase == GUIPhase.PICKCARD) Platform.runLater(guiPickCardStage::twoCardsShow);
        else if(messageID == 407 && guiPhase == GUIPhase.PICKCARD) Platform.runLater(guiPickCardStage::showAgain);
        else if(messageID == 411 && guiPhase == GUIPhase.PICKCARD) Platform.runLater(guiPickCardStage::showCards);
        else if(messageID == 306 && guiPhase == GUIPhase.ROUND) Platform.runLater(guiRoundStage::resetButtons);
        else if(messageID == 108 && guiPhase == GUIPhase.ROUND) Platform.runLater(guiRoundStage::setBounds);
        else if(messageID == 114 && guiPhase == GUIPhase.ROUND) Platform.runLater(guiRoundStage::setBounds);
    }

    /**
     * Invoked by guiEventsCommunication every time a playerchosencard event arrives
     * @param playerName name of the player
     * @param cardName name of the card
     */
    public void playerChosenCard(String playerName, String cardName) {
        Platform.runLater(() -> guiPickCardStage.setCardsImages(playerName, cardName));
        guiPlayersManager.getPlayer(playerName).setCardName(cardName);
    }

    public void setNames(ArrayList<String> names) {
        for (String name : names) guiPlayersManager.addPlayer(name);
    }

    /**
     * Transitioning from the pick card phase to the round phase
     */
    public void roundTransition() {
        guiPhase = GUIPhase.ROUND;
        Platform.runLater(() -> guiRoundStage.setUp(stage, this));
        animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> CountDown()));
        animation.setCycleCount(timer);
        animation.setOnFinished(event -> Platform.runLater(() -> guiRoundStage.start(stage)));
        animation.play();
    }

    /**
     * Method used to delay the switch of the scenes
     */
    private void CountDown() {
        Platform.runLater(() -> guiPickCardStage.roundTransition(seconds));
        seconds--;
    }

    /**
     * Invoked by guiEventsCommunication when the firstPlayer event arrives
     * @param names list of names
     */
    public void selectFirstPlayer(ArrayList<String> names) {
        guiPickCardStage.selectFirstPlayer(names);
    }

    /**
     * Sets up the win scene
     * @param winnerName name of the winnes
     */
    public void win(String winnerName){
        try {
            guiRoundStage.getStagesManager().setUseDisconnectionScene(false);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Win/win.fxml"));
            Parent root = loader.load();
            GUIWinController winController = loader.getController();
            stage.setScene(new Scene(root, 600, 600));
            stage.setHeight(600);
            stage.setWidth(600);
            stage.setResizable(false);
            winController.setWinnerBanner(winnerName);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirects the colors to the guiRoundStage
     * @param colorsName list of colors
     */
    public void showColors(ArrayList<String> colorsName) {
        guiRoundStage.showColors(colorsName);
    }

    /**
     * Sets up the first scene showed
     * @param ip ip of the server
     * @param port port of the server
     * @throws Exception when server unavailable
     */
    public void startLoginStage(String ip, int port) throws Exception {
        client = new Client(ip, port);
        client.setupGUI();
        GUIEventsCommunication eventsCommunication = (GUIEventsCommunication) client.getEventsCommunication();
        eventsCommunication.setStagesManager(this);
        eventsCommunication.setGuiLoginStage(guiLoginStage);
        eventsCommunication.setGuiPickCardStage(guiPickCardStage);
        eventsCommunication.setGuiRoundStage(guiRoundStage);
        guiLoginStage.start(stage, this);
        try {
            client.run();
            serverUp = true;
        } catch (Exception e) {
            guiLoginStage.serverUnavailable();
        }
    }
}