package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIIpStage {

    public void start(Stage stage, GUIStagesManager stagesManager) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Login/ipPort.fxml"));
        Parent root = loader.load();
        GUIIpController guiIpController = loader.getController();
        guiIpController.setStagesManager(stagesManager);
        guiIpController.initialize();
        stage.setScene(new Scene(root, 600, 600));
        stage.setResizable(false);
        stage.show();
    }
}
