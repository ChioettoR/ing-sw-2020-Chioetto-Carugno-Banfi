package it.polimi.ingsw.Client.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new GUILogin().start(stage);
    }

    public static void launch() {
        Application.launch(GUIMain.class);
    }
}
