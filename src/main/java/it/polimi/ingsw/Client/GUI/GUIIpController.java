package it.polimi.ingsw.Client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.regex.Pattern;

public class GUIIpController {

    public Button doneButton;
    public AnchorPane backGround;
    private GUIStagesManager stagesManager;
    boolean okPort = false;

    @FXML
    private Text serverIPText;

    @FXML
    private TextField serverIPTextField;

    @FXML
    private Text serverPortText;

    @FXML
    private TextField serverPortTextField;

    @FXML
    private Text xPort;

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    @FXML
    void checkPort() {

        if(!isNumeric(serverPortTextField.getText())){
            xPort.setVisible(true);
            okPort = false;
        }
        else {
            xPort.setVisible(false);
            okPort = true;
        }
    }

    @FXML
    void startLoginStage() throws Exception {
        String ip = serverIPTextField.getText();
        int port;
        if(okPort) {
            port = Integer.parseInt(serverPortTextField.getText());
            stagesManager.startLoginStage(ip, port);
        }
    }

    public void initialize() {
        final Font minecraft = Font.loadFont(getClass().getResourceAsStream("/Login/Minecraft.ttf"), 20);
        serverIPText.setFont(minecraft);
        serverIPTextField.setFont(minecraft);
        serverPortText.setFont(minecraft);
        serverPortTextField.setFont(minecraft);
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;
        return pattern.matcher(strNum).matches();
    }
}
