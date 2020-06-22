package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.AllPlayersCardsEvent;
import it.polimi.ingsw.Events.Client.FirstPlayerChosenEvent;
import it.polimi.ingsw.Events.Client.PickCardEvent;
import it.polimi.ingsw.Model.CardSimplified;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GUIPickCardPhaseController {

    GUICards guiCards;
    GUIStagesManager stagesManager;

    ArrayList<String> cardNames = new ArrayList<>();
    ArrayList<String> namesToSend = new ArrayList<>();
    int godIndex = 0;

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
    }

    public void setGuiCards(GUICards guiCards) {
        this.guiCards = guiCards;
    }

    public ArrayList<String> getNamesToSend() {
        return namesToSend;
    }

    public void addNamesToSend(String name) {
        namesToSend.add(name);
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

    @FXML
    private ImageView godImageToChoose;

    @FXML
    private Text godNameToChoose;

    @FXML
    private Button infoButtonToChoose;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @FXML
    private AnchorPane threeCardsPane;

    @FXML
    private AnchorPane twoCardsPane;

    @FXML
    private AnchorPane startPlayerPane;

    @FXML
    private ImageView startPlayerGod;

    @FXML
    private Text startPlayerName;

    @FXML
    private Button startPlayerButtonInfo;

    @FXML
    private AnchorPane dragStartingPlayer;

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

    @FXML
    private AnchorPane cardsToSelect;

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

    @FXML
    private ImageView godToSelect3p1;

    @FXML
    private ImageView godToSelect3p2;

    @FXML
    private ImageView godToSelect3p3;

    @FXML
    private ImageView godToSelect2p1;

    @FXML
    private ImageView godToSelect2p2;

    public int getGodIndex() {
        return godIndex;
    }

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

    public ImageView getGodImageToChoose() {
        return godImageToChoose;
    }

    public Text getGodNameToChoose() {
        return godNameToChoose;
    }

    public Button getInfoButtonToChoose() {
        return infoButtonToChoose;
    }

    public AnchorPane getThreeCardsPane() {
        return threeCardsPane;
    }

    public AnchorPane getTwoCardsPane() {
        return twoCardsPane;
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

    public AnchorPane getCardsToSelect() {
        return cardsToSelect;
    }

    public GUICards getGuiCards() {
        return guiCards;
    }

    public ImageView getGodToSelect3p1() {
        return godToSelect3p1;
    }

    public ImageView getGodToSelect3p2() {
        return godToSelect3p2;
    }

    public ImageView getGodToSelect3p3() {
        return godToSelect3p3;
    }

    public ImageView getGodToSelect2p1() {
        return godToSelect2p1;
    }

    public ImageView getGodToSelect2p2() {
        return godToSelect2p2;
    }

    public AnchorPane getStartPlayerPane() {
        return startPlayerPane;
    }

    public ImageView getStartPlayerGod() {
        return startPlayerGod;
    }

    public Text getStartPlayerName() {
        return startPlayerName;
    }

    public Button getStartPlayerButtonInfo() {
        return startPlayerButtonInfo;
    }

    public AnchorPane getDragStartingPlayer() {
        return dragStartingPlayer;
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

    /**
     * Arrow to view the previous card
     * @param event click event
     */
    @FXML
    void goBack(ActionEvent event) {
        godIndex--;
        if(godIndex < 0)
            godIndex = cardNames.size() - 1;
        String godName = cardNames.get(godIndex);
        godImageToChoose.setImage(guiCards.getFullImage(godName));
        godNameToChoose.setText(godName);

        infoButtonToChoose.setOnDragDetected(e -> {
            Dragboard db = infoButtonToChoose.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            db.setDragView(godImageToChoose.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString(godName);
            db.setContent(content);
            e.consume();
        });
    }

    /**
     * Arrow to view the previous card
     * @param event click event
     */
    @FXML
    void goNext(ActionEvent event) {
        godIndex++;
        if(godIndex == cardNames.size())
            godIndex = 0;
        String godName = cardNames.get(godIndex);
        godImageToChoose.setImage(guiCards.getFullImage(godName));
        godNameToChoose.setText(godName);

        infoButtonToChoose.setOnDragDetected(e -> {
            Dragboard db = infoButtonToChoose.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            db.setDragView(godImageToChoose.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString(godName);
            db.setContent(content);
            e.consume();
        });

    }

    /**
     * Removes the selected card in the first box (2 players)
     * @param event click event
     */
    @FXML
    void deselect2p1(ActionEvent event) {
        namesToSend.remove(namesToSend.get(0));
        godToSelect2p1.setImage(null);
    }

    /**
     * Removes the selected card in the second box (2 players)
     * @param event click event
     */
    @FXML
    void deselect2p2(ActionEvent event) {

    }

    /**
     * Removes the selected card in the first box (3 players)
     * @param event click event
     */
    @FXML
    void deselect3p1(ActionEvent event) {
        namesToSend.remove(namesToSend.get(0));
        godToSelect3p1.setImage(null);
        if(godToSelect3p2.getImage() != null) {
            godToSelect3p1.setImage(godToSelect3p2.getImage());
            godToSelect3p2.setImage(null);
        }
    }

    /**
     * Removes the selected card in the second box (3 players)
     * @param event click event
     */
    @FXML
    void deselect3p2(ActionEvent event) {
        namesToSend.remove(namesToSend.get(1));
        godToSelect3p2.setImage(null);
    }

    /**
     * Removes the selected card in the third box (3 players)
     * @param event click event
     */
    @FXML
    void deselect3p3(ActionEvent event) {

    }

    @FXML
    void drawCards(ActionEvent event) {
        draw.setDisable(true);
        draw.setVisible(false);
        //TODO:
        //stagesManager.send(new AllPlayersCardsEvent());
    }

    /**
     * Shows the info box of the card
     * @param event click event
     */
    @FXML
    void checkInfoToChoose(ActionEvent event) {
        setInfoPane();
        String godName = godNameToChoose.getText();
        setInfo(godName);
    }

    /**
     * Sets the info Pane of the card
     */
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

    public void setFullDeck(ArrayList<CardSimplified> cards) {
        for(CardSimplified c : cards) cardNames.add(c.getName());
    }

    public void sendToServer() {
        draw.setDisable(true);
        draw.setVisible(false);
        cardsToSelect.setDisable(true);
        cardsToSelect.setVisible(false);
        ArrayList<String> cardsToSend = new ArrayList<>(namesToSend);
        stagesManager.send(new AllPlayersCardsEvent(cardsToSend));
    }

    public void sendToStageTheFirstPlayer(String playerName) {
        stagesManager.send(new FirstPlayerChosenEvent(playerName));
    }
}
