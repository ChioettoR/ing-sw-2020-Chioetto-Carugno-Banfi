package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIIpStage {

    GUIIpController guiIpController;

    public void start(Stage stage, GUIStagesManager stagesManager) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Login/ipPort.fxml"));
        Parent root = loader.load();
        guiIpController = loader.getController();
        guiIpController.setStagesManager(stagesManager);
        guiIpController.initialize();
        stage.setScene(new Scene(root, 600, 600));
        stage.setResizable(false);
        stage.show();
    }
}
