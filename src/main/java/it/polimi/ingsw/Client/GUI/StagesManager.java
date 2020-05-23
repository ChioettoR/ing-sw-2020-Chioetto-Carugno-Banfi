package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Events.Client.ClientEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class StagesManager extends Application {

    private static Client client;
    private static String ip;
    private static int port;
    Stage stage;
    //GUIRoundStage guiRoundStage = new GUIRoundStage();
    GUILoginStage guiLoginStage = new GUILoginStage();
    boolean login = true;



    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        client = new Client(ip, port);
        client.setupGUI();
        GUIEventsCommunication eventsCommunication = (GUIEventsCommunication) client.getEventsCommunication();
        eventsCommunication.setStagesManager(this);
        //eventsCommunication.setGuiRoundStage(guiRoundStage);
        eventsCommunication.setGuiLoginStage(guiLoginStage);
        guiLoginStage.start(stage, this);
        client.run();
    }

    public void send(ClientEvent event) {
        client.update(event);
    }

//    private void gameStage(Stage stage) {
//        guiRoundStage = new GUIRoundStage();
//        guiRoundStage.start(stage);
//    }

    public void readMessage(String message) {
        if(login) Platform.runLater(() -> guiLoginStage.readMessage(message));
    }

    public void readRequest(String message) {
        if(login) Platform.runLater(() -> guiLoginStage.readRequest(message));
    }

    public void readError(String message) {
        if(login) Platform.runLater(() -> guiLoginStage.readError(message));
    }

    @Override
    public void stop(){
        client.closeConnection();
    }

    public void lobbyInfo(String lobbyName, int lobbySize) {
        if(login)
            Platform.runLater(() -> guiLoginStage.lobbyInfo(lobbyName, lobbySize));
    }

    public void endLogin(ArrayList<String> names) {
        //Platform.runLater(() -> gameStage(stage));
        login = false;
    }

    public static void launch(String ip, int port) throws IOException {
        StagesManager.ip = ip;
        StagesManager.port = port;
        Application.launch();
    }
}
