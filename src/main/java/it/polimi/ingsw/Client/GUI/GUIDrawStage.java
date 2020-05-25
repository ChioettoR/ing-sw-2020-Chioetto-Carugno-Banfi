package it.polimi.ingsw.Client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUIDrawStage{

    GUIDrawPhaseController guiDrawPhaseController;
    GUIPlayersManager guiPlayersManager = new GUIPlayersManager();

    public void start(Stage stage, ArrayList<String> names){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/DrawPhase/drawPhase.fxml"));
        Parent root;
        try {
            root = loader.load();
            guiDrawPhaseController = loader.getController();
            setNames(names);
            stage.setScene(new Scene(root, 600, 600));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) { e.printStackTrace();}
    }

    public void readMessage(String message) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getMessagePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText(message);
    }

    public void readRequest(String request) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getMessagePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText(request);
    }

    public void setNames(ArrayList<String> names) {
        for (String name : names) guiPlayersManager.addPlayer(name);
        setTextNames(names);
    }

    public void setTextNames(ArrayList<String> names) {
        guiDrawPhaseController.getDrawPhasePane().setVisible(true);
        guiDrawPhaseController.getDownPane().setVisible(true);
        if(names.size() == 2) {
            guiDrawPhaseController.getDownThreeCardsPane().setVisible(false);
            guiDrawPhaseController.getDownTwoCardsPane().setVisible(true);
            guiDrawPhaseController.getPlayerName2p1().setText(names.get(0));
            guiDrawPhaseController.getPlayerName2p2().setText(names.get(1));
        }
        else {
            guiDrawPhaseController.getDownThreeCardsPane().setVisible(true);
            guiDrawPhaseController.getDownTwoCardsPane().setVisible(false);
            guiDrawPhaseController.getPlayerName3p1().setText(names.get(0));
            guiDrawPhaseController.getPlayerName3p2().setText(names.get(1));
            guiDrawPhaseController.getPlayerName3p3().setText(names.get(2));
        }
    }

    public void draw(String draw) {
        guiDrawPhaseController.getUpPane().setVisible(true);
        guiDrawPhaseController.getDraw().setVisible(true);
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText(draw);
    }
}
