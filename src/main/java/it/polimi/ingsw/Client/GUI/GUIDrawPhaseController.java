package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.DrawEvent;
import it.polimi.ingsw.Events.Client.PickCardEvent;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.PlayersManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GUIDrawPhaseController {

    GUICards guiCards;
    StagesManager stagesManager;

    public void setStagesManager(StagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    public void setGuiCards(GUICards guiCards) {
        this.guiCards = guiCards;
    }

    @FXML
    private AnchorPane backGround;

    @FXML
    private AnchorPane drawPhasePane;

    //upPane

    @FXML
    private AnchorPane upPane;

    @FXML
    private AnchorPane upThreeCardsPane;

    @FXML
    private AnchorPane upTwoCardsPane;

    @FXML
    private AnchorPane draw;

    @FXML
    private Button drawCards;

    @FXML
    private Button infoButtonUp2p1;

    @FXML
    private ImageView godImageUp2p1;

    @FXML
    private Text godName2p1;

    @FXML
    private Button infoButtonUp2p2;

    @FXML
    private ImageView godImageUp2p2;

    @FXML
    private Text godName2p2;

    @FXML
    private Button infoButtonUp3p1;

    @FXML
    private ImageView godImageUp3p1;

    @FXML
    private Text godName3p1;

    @FXML
    private Button infoButtonUp3p2;

    @FXML
    private ImageView godImageUp3p2;

    @FXML
    private Text godName3p2;

    @FXML
    private Button infoButtonUp3p3;

    @FXML
    private ImageView godImageUp3p3;

    @FXML
    private Text godName3p3;

    //middlePane

    @FXML
    private AnchorPane middlePane;

    @FXML
    private AnchorPane errorPane;

    @FXML
    private Text errorText;

    @FXML
    private AnchorPane messagePane;

    @FXML
    private Text messageText;

    //downPane

    @FXML
    private AnchorPane downPane;

    @FXML
    private AnchorPane downThreeCardsPane;

    @FXML
    private AnchorPane downTwoCardsPane;

    @FXML
    private Button infoButtonDown2p1;

    @FXML
    private ImageView godImageDown2p1;

    @FXML
    private Text playerName2p1;

    @FXML
    private Button infoButtonDown2p2;

    @FXML
    private ImageView godImageDown2p2;

    @FXML
    private Text playerName2p2;

    @FXML
    private Button infoButtonDown3p1;

    @FXML
    private ImageView godImageDown3p1;

    @FXML
    private Text playerName3p1;

    @FXML
    private Button infoButtonDown3p2;

    @FXML
    private ImageView godImageDown3p2;

    @FXML
    private Text playerName3p2;

    @FXML
    private Button infoButtonDown3p3;

    @FXML
    private ImageView godImageDown3p3;

    @FXML
    private Text playerName3p3;

    //infoPane

    @FXML
    private AnchorPane infoPane;

    @FXML
    private Text infoGodName;

    @FXML
    private Text infoDescription;

    @FXML
    private Button closeInfoButton;

    @FXML
    private ImageView infoGodImage;


    public AnchorPane getDrawPhasePane() {
        return drawPhasePane;
    }

    public AnchorPane getUpPane() {
        return upPane;
    }

    public AnchorPane getUpThreeCardsPane() {
        return upThreeCardsPane;
    }

    public AnchorPane getUpTwoCardsPane() {
        return upTwoCardsPane;
    }

    public AnchorPane getDraw() {
        return draw;
    }

    public Button getDrawCards() {
        return drawCards;
    }

    public Button getInfoButtonUp2p1() {
        return infoButtonUp2p1;
    }

    public ImageView getGodImageUp2p1() {
        return godImageUp2p1;
    }

    public Text getGodName2p1() {
        return godName2p1;
    }

    public Button getInfoButtonUp2p2() {
        return infoButtonUp2p2;
    }

    public ImageView getGodImageUp2p2() {
        return godImageUp2p2;
    }

    public Text getGodName2p2() {
        return godName2p2;
    }

    public Button getInfoButtonUp3p1() {
        return infoButtonUp3p1;
    }

    public ImageView getGodImageUp3p1() {
        return godImageUp3p1;
    }

    public Text getGodName3p1() {
        return godName3p1;
    }

    public Button getInfoButtonUp3p2() {
        return infoButtonUp3p2;
    }

    public ImageView getGodImageUp3p2() {
        return godImageUp3p2;
    }

    public Text getGodName3p2() {
        return godName3p2;
    }

    public Button getInfoButtonUp3p3() {
        return infoButtonUp3p3;
    }

    public ImageView getGodImageUp3p3() {
        return godImageUp3p3;
    }

    public Text getGodName3p3() {
        return godName3p3;
    }

    public AnchorPane getMiddlePane() {
        return middlePane;
    }

    public AnchorPane getErrorPane() {
        return errorPane;
    }

    public Text getErrorText() {
        return errorText;
    }

    public AnchorPane getMessagePane() {
        return messagePane;
    }

    public Text getMessageText() {
        return messageText;
    }

    public AnchorPane getDownPane() {
        return downPane;
    }

    public AnchorPane getDownThreeCardsPane() {
        return downThreeCardsPane;
    }

    public AnchorPane getDownTwoCardsPane() {
        return downTwoCardsPane;
    }

    public Button getInfoButtonDown2p1() {
        return infoButtonDown2p1;
    }

    public ImageView getGodImageDown2p1() {
        return godImageDown2p1;
    }

    public Text getPlayerName2p1() {
        return playerName2p1;
    }

    public Button getInfoButtonDown2p2() {
        return infoButtonDown2p2;
    }

    public ImageView getGodImageDown2p2() {
        return godImageDown2p2;
    }

    public Text getPlayerName2p2() {
        return playerName2p2;
    }

    public Button getInfoButtonDown3p1() {
        return infoButtonDown3p1;
    }

    public ImageView getGodImageDown3p1() {
        return godImageDown3p1;
    }

    public Text getPlayerName3p1() {
        return playerName3p1;
    }

    public Button getInfoButtonDown3p2() {
        return infoButtonDown3p2;
    }

    public ImageView getGodImageDown3p2() {
        return godImageDown3p2;
    }

    public Text getPlayerName3p2() {
        return playerName3p2;
    }

    public Button getInfoButtonDown3p3() {
        return infoButtonDown3p3;
    }

    public ImageView getGodImageDown3p3() {
        return godImageDown3p3;
    }

    public Text getPlayerName3p3() {
        return playerName3p3;
    }

    public AnchorPane getInfoPane() {
        return infoPane;
    }

    public Text getInfoGodName() {
        return infoGodName;
    }

    public Text getInfoDescription() {
        return infoDescription;
    }

    public Button getCloseInfoButton() {
        return closeInfoButton;
    }

    public ImageView getInfoGodImage() {
        return infoGodImage;
    }

    @FXML
    void returnToDraw(ActionEvent event) {
        infoPane.setDisable(true);
        infoPane.setVisible(false);
        drawPhasePane.setDisable(false);
        drawPhasePane.setVisible(true);
    }


    @FXML
    void checkInfoDown2p1(ActionEvent event) {
        setInfoPane();
        String godName = stagesManager.getGuiPlayersManager().getPlayer(playerName2p1.getText()).getCardName();
        setInfo(godName);
    }

    @FXML
    void checkInfoDown2p2(ActionEvent event) {
        setInfoPane();
        String godName = stagesManager.getGuiPlayersManager().getPlayer(playerName2p2.getText()).getCardName();
        setInfo(godName);
    }

    @FXML
    void checkInfoDown3p1(ActionEvent event) {
        setInfoPane();
        String godName = stagesManager.getGuiPlayersManager().getPlayer(playerName3p1.getText()).getCardName();
        setInfo(godName);
    }

    @FXML
    void checkInfoDown3p2(ActionEvent event) {
        setInfoPane();
        String godName = stagesManager.getGuiPlayersManager().getPlayer(playerName3p2.getText()).getCardName();
        setInfo(godName);
    }

    @FXML
    void checkInfoDown3p3(ActionEvent event) {
        setInfoPane();
        String godName = stagesManager.getGuiPlayersManager().getPlayer(playerName3p3.getText()).getCardName();
        setInfo(godName);
    }

    @FXML
    void checkInfoUp2p1(ActionEvent event) {
        setInfoPane();
        String godName = godName2p1.getText();
        setInfo(godName);
    }

    @FXML
    void checkInfoUp2p2(ActionEvent event) {
        setInfoPane();
        String godName = godName2p2.getText();
        setInfo(godName);
    }

    @FXML
    void checkInfoUp3p1(ActionEvent event) {
        setInfoPane();
        String godName = godName3p1.getText();
        setInfo(godName);
    }

    @FXML
    void checkInfoUp3p2(ActionEvent event) {
        setInfoPane();
        String godName = godName3p2.getText();
        setInfo(godName);
    }

    @FXML
    void checkInfoUp3p3(ActionEvent event) {
        setInfoPane();
        String godName = godName3p3.getText();
        setInfo(godName);
    }

    @FXML
    void drawCards(ActionEvent event) {
        draw.setDisable(true);
        draw.setVisible(false);
        stagesManager.send(new DrawEvent());
    }

    public void setInfoPane() {
        drawPhasePane.setDisable(true);
        drawPhasePane.setVisible(false);
        infoPane.setDisable(false);
        infoPane.setVisible(true);
    }

    public void setInfo(String godName) {
        infoGodName.setText(godName);
        infoGodImage.setImage(guiCards.getSmallImage(godName));
        infoDescription.setText(guiCards.getDescription(godName));
    }

    public void sendToStagesManager(String cardName) {
        stagesManager.send(new PickCardEvent(cardName));
    }
}
