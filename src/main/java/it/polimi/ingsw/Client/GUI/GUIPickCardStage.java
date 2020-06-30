package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Model.CardSimplified;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUIPickCardStage {

    private GUIPickCardPhaseController guiPickCardPhaseController;
    private GUIPlayersManager guiPlayersManager;
    private final GUICards guiCards = new GUICards();

    /**
     * Starts the Scene of the draw phase
     * @param stage stage of the game
     * @param stagesManager managed for different stages
     * @param guiPlayersManager manager for different players
     */
    public void start(Stage stage, GUIStagesManager stagesManager, GUIPlayersManager guiPlayersManager){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/PickCardPhase/pickCardPhase.fxml"));
        Parent root;
        try {
            root = loader.load();
            guiPickCardPhaseController = loader.getController();
            guiPickCardPhaseController.setStagesManager(stagesManager);
            guiPickCardPhaseController.setGuiCards(guiCards);
            this.guiPlayersManager = guiPlayersManager;
            setTextNames(guiPlayersManager.getNames());
            stage.setScene(new Scene(root, 600, 600));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) { e.printStackTrace();}
    }

    public GUICards getGuiCards() {
        return guiCards;
    }

    /**
     * Sets the message into the message box
     * @param message string
     */
    public void readMessage(String message) {
        guiPickCardPhaseController.getMiddlePane().setVisible(true);
        guiPickCardPhaseController.getErrorPane().setVisible(false);
        guiPickCardPhaseController.getMessagePane().setVisible(true);
        guiPickCardPhaseController.getMessageText().setText(message);
    }

    /**
     * Sets the request into the message box
     * @param request string
     */
    public void readRequest(String request) {
        guiPickCardPhaseController.getMiddlePane().setVisible(true);
        guiPickCardPhaseController.getErrorPane().setVisible(false);
        guiPickCardPhaseController.getMessagePane().setVisible(true);
        guiPickCardPhaseController.getMessageText().setText(request);
    }

    /**
     * Sets the error into the message box
     * @param error string
     */
    public void readError(String error) {
        guiPickCardPhaseController.getMiddlePane().setVisible(true);
        guiPickCardPhaseController.getErrorPane().setVisible(true);
        guiPickCardPhaseController.getMessagePane().setVisible(false);
        guiPickCardPhaseController.getErrorText().setText(error);
    }

    public void setTextNames(ArrayList<String> names) {
        guiPickCardPhaseController.getDrawPhasePane().setVisible(true);
        guiPickCardPhaseController.getDownPane().setVisible(true);

        if(names.size() == 2) {
            guiPickCardPhaseController.getDownThreeCardsPane().setVisible(false);
            guiPickCardPhaseController.getDownTwoCardsPane().setVisible(true);
            guiPickCardPhaseController.getPlayerName2p1().setText(names.get(0));
            guiPickCardPhaseController.getPlayerName2p2().setText(names.get(1));
        }
        else {
            guiPickCardPhaseController.getDownThreeCardsPane().setVisible(true);
            guiPickCardPhaseController.getDownTwoCardsPane().setVisible(false);
            guiPickCardPhaseController.getPlayerName3p1().setText(names.get(0));
            guiPickCardPhaseController.getPlayerName3p2().setText(names.get(1));
            guiPickCardPhaseController.getPlayerName3p3().setText(names.get(2));
        }
    }

    /**
     * Shows the god cards pane to the first player
     * @param choose message with the number of cards to choose
     */
    public void showFullDeck(String choose) {
        guiPickCardPhaseController.getUpPane().setVisible(true);
        guiPickCardPhaseController.getDraw().setVisible(true);
        guiPickCardPhaseController.getMiddlePane().setVisible(true);
        guiPickCardPhaseController.getMessageText().setText(choose);
    }

    /**
     * Sends the graphic of the deck to the user
     * @param cards list of cards
     */
    public void sendDeck(ArrayList<CardSimplified> cards) {

        setDownPaneForDrag();

        for (CardSimplified card : cards) guiCards.addDescription(card);

        if(cards.size() == 2) {
            guiPickCardPhaseController.getUpPane().setVisible(true);
            guiPickCardPhaseController.getUpThreeCardsPane().setDisable(true);
            guiPickCardPhaseController.getUpThreeCardsPane().setVisible(false);
            guiPickCardPhaseController.getUpTwoCardsPane().setDisable(false);
            guiPickCardPhaseController.getUpTwoCardsPane().setVisible(true);
            guiPickCardPhaseController.getGodName2p1().setText(cards.get(0).getName());
            guiPickCardPhaseController.getGodName2p2().setText(cards.get(1).getName());
            guiPickCardPhaseController.getGodImageUp2p1().setImage(guiCards.getFullImage(cards.get(0).getName()));
            guiPickCardPhaseController.getGodImageUp2p2().setImage(guiCards.getFullImage(cards.get(1).getName()));

            Button godButtonUp2p1 = guiPickCardPhaseController.getInfoButtonUp2p1();
            ImageView godImageUp2p1 = guiPickCardPhaseController.getGodImageUp2p1();

            godButtonUp2p1.setOnDragDetected(event -> {
                Dragboard db = godButtonUp2p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp2p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(0).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp2p2 = guiPickCardPhaseController.getInfoButtonUp2p2();
            ImageView godImageUp2p2 = guiPickCardPhaseController.getGodImageUp2p2();

            godButtonUp2p2.setOnDragDetected(event -> {
                Dragboard db = godButtonUp2p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp2p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(1).getName());
                db.setContent(content);
                event.consume();
            });

        }
        else if(cards.size() == 3) {
            guiPickCardPhaseController.getUpPane().setVisible(true);
            guiPickCardPhaseController.getUpTwoCardsPane().setDisable(true);
            guiPickCardPhaseController.getUpTwoCardsPane().setVisible(false);
            guiPickCardPhaseController.getUpThreeCardsPane().setDisable(false);
            guiPickCardPhaseController.getUpThreeCardsPane().setVisible(true);
            guiPickCardPhaseController.getGodName3p1().setText(cards.get(0).getName());
            guiPickCardPhaseController.getGodName3p2().setText(cards.get(1).getName());
            guiPickCardPhaseController.getGodName3p3().setText(cards.get(2).getName());
            guiPickCardPhaseController.getGodImageUp3p1().setImage(guiCards.getFullImage(cards.get(0).getName()));
            guiPickCardPhaseController.getGodImageUp3p2().setImage(guiCards.getFullImage(cards.get(1).getName()));
            guiPickCardPhaseController.getGodImageUp3p3().setImage(guiCards.getFullImage(cards.get(2).getName()));

            Button godButtonUp3p1 = guiPickCardPhaseController.getInfoButtonUp3p1();
            ImageView godImageUp3p1 = guiPickCardPhaseController.getGodImageUp3p1();

            godButtonUp3p1.setOnDragDetected(event -> {
                Dragboard db = godButtonUp3p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp3p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(0).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp3p2 = guiPickCardPhaseController.getInfoButtonUp3p2();
            ImageView godImageUp3p2 = guiPickCardPhaseController.getGodImageUp3p2();

            godButtonUp3p2.setOnDragDetected(event -> {
                Dragboard db = godButtonUp3p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp3p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(1).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp3p3 = guiPickCardPhaseController.getInfoButtonUp3p3();
            ImageView godImageUp3p3 = guiPickCardPhaseController.getGodImageUp3p3();

            godButtonUp3p3.setOnDragDetected(event -> {
                Dragboard db = godButtonUp3p3.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp3p3.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(2).getName());
                db.setContent(content);
                event.consume();
            });
        }
    }

    /**
     * Sets the lower part of the GUI draggable for the choice of the cards
     */
    public void setDownPaneForDrag() {
        AnchorPane downPane = guiPickCardPhaseController.getDownPane();
        downPane.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        downPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                guiPickCardPhaseController.sendToStagesManager(db.getString());
                guiPickCardPhaseController.getUpPane().setVisible(false);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Sets the images of the cards
     * @param playerName name of the player
     * @param cardName name of the card
     */
    public void setCardsImages(String playerName, String cardName) {
        if(guiPickCardPhaseController.getPlayerName2p1().getText().equalsIgnoreCase(playerName)){
            guiPickCardPhaseController.getGodImageDown2p1().setImage(guiCards.getFullImage(cardName));
            guiPickCardPhaseController.getInfoButtonDown2p1().setDisable(false);
        }
        else if(guiPickCardPhaseController.getPlayerName2p2().getText().equalsIgnoreCase(playerName)) {
            guiPickCardPhaseController.getGodImageDown2p2().setImage(guiCards.getFullImage(cardName));
            guiPickCardPhaseController.getInfoButtonDown2p2().setDisable(false);
        }
        else if(guiPickCardPhaseController.getPlayerName3p1().getText().equalsIgnoreCase(playerName)) {
            guiPickCardPhaseController.getGodImageDown3p1().setImage(guiCards.getFullImage(cardName));
            guiPickCardPhaseController.getInfoButtonDown3p1().setDisable(false);
        }
        else if(guiPickCardPhaseController.getPlayerName3p2().getText().equalsIgnoreCase(playerName)) {
            guiPickCardPhaseController.getGodImageDown3p2().setImage(guiCards.getFullImage(cardName));
            guiPickCardPhaseController.getInfoButtonDown3p2().setDisable(false);
        }
        else if(guiPickCardPhaseController.getPlayerName3p3().getText().equalsIgnoreCase(playerName)) {
            guiPickCardPhaseController.getGodImageDown3p3().setImage(guiCards.getFullImage(cardName));
            guiPickCardPhaseController.getInfoButtonDown3p3().setDisable(false);
        }
    }

    /**
     * Transition for the beginning of the game
     * @param seconds time left for the game visible in GUI
     */
    public void roundTransition(int seconds) {
        guiPickCardPhaseController.getMiddlePane().setVisible(true);
        guiPickCardPhaseController.getErrorPane().setVisible(false);
        guiPickCardPhaseController.getMessagePane().setVisible(true);
        guiPickCardPhaseController.getMessageText().setText("Round starts in: " + seconds);
    }

    /**
     * Sends the full deck for the initial choice
     * @param cards list of cards
     */
    public void sendFullDeck(ArrayList<CardSimplified> cards) {

        int godIndex;

        for (CardSimplified card : cards) guiCards.addDescription(card);

        guiPickCardPhaseController.setFullDeck(cards);
        guiPickCardPhaseController.getDraw().setVisible(true);
        godIndex = guiPickCardPhaseController.getGodIndex();
        String godName = cards.get(godIndex).getName();
        guiPickCardPhaseController.getGodImageToChoose().setImage(guiCards.getFullImage(godName));
        guiPickCardPhaseController.getGodNameToChoose().setText(godName);

        Button infoButtonToChoose = guiPickCardPhaseController.getInfoButtonToChoose();
        ImageView godImageToChoose = guiPickCardPhaseController.getGodImageToChoose();

        infoButtonToChoose.setOnDragDetected(event -> {
            Dragboard db = infoButtonToChoose.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            db.setDragView(godImageToChoose.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString(godName);
            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Shows three cards in GUI (3 players)
     */
    public void threeCardsShow() {
        guiPickCardPhaseController.getCardsToSelect().setVisible(true);
        guiPickCardPhaseController.getCardsToSelect().setDisable(false);
        guiPickCardPhaseController.getTwoCardsPane().setVisible(false);
        guiPickCardPhaseController.getTwoCardsPane().setDisable(true);
        guiPickCardPhaseController.getThreeCardsPane().setDisable(false);
        guiPickCardPhaseController.getThreeCardsPane().setVisible(true);
        setRightPaneForDrag();
    }

    /**
     * Shows two cards in GUI (2 players)
     */
    public void twoCardsShow() {
        guiPickCardPhaseController.getCardsToSelect().setVisible(true);
        guiPickCardPhaseController.getCardsToSelect().setDisable(false);
        guiPickCardPhaseController.getThreeCardsPane().setVisible(false);
        guiPickCardPhaseController.getThreeCardsPane().setDisable(true);
        guiPickCardPhaseController.getTwoCardsPane().setDisable(false);
        guiPickCardPhaseController.getTwoCardsPane().setVisible(true);
        setRightPaneForDrag();
    }

    /**
     * Sets the right pane draggable for the god cards choice
     */
    public void setRightPaneForDrag() {
        AnchorPane rightPane = guiPickCardPhaseController.getCardsToSelect();
        rightPane.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        rightPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String godName = db.getString();
                if(guiPickCardPhaseController.getThreeCardsPane().isVisible()) {
                    if(guiPickCardPhaseController.getGodToSelect3p1().getImage() == null) {
                        guiPickCardPhaseController.getGodToSelect3p1().setImage(guiCards.getFullImage(godName));
                        guiPickCardPhaseController.addNamesToSend(godName);
                    }
                    else if(guiPickCardPhaseController.getGodToSelect3p2().getImage() == null)  {
                        guiPickCardPhaseController.getGodToSelect3p2().setImage(guiCards.getFullImage(godName));
                        guiPickCardPhaseController.addNamesToSend(godName);
                    }
                    else if(guiPickCardPhaseController.getGodToSelect3p3().getImage() == null) {
                        guiPickCardPhaseController.getGodToSelect3p3().setImage(guiCards.getFullImage(godName));
                        guiPickCardPhaseController.addNamesToSend(godName);
                        guiPickCardPhaseController.sendToServer();
                    }
                }
                else {
                    if(guiPickCardPhaseController.getGodToSelect2p1().getImage() == null) {
                        guiPickCardPhaseController.getGodToSelect2p1().setImage(guiCards.getFullImage(godName));
                        guiPickCardPhaseController.addNamesToSend(godName);
                    }
                    else if(guiPickCardPhaseController.getGodToSelect2p2().getImage() == null) {
                        guiPickCardPhaseController.getGodToSelect2p2().setImage(guiCards.getFullImage(godName));
                        guiPickCardPhaseController.addNamesToSend(godName);
                        guiPickCardPhaseController.sendToServer();
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Invoked when the first player sends the wrong cards to the others
     */
    public void showAgain() {
        guiPickCardPhaseController.getDraw().setDisable(false);
        guiPickCardPhaseController.getDraw().setVisible(true);
        guiPickCardPhaseController.getCardsToSelect().setDisable(false);
        guiPickCardPhaseController.getCardsToSelect().setVisible(true);
        guiPickCardPhaseController.getGodToSelect2p1().setImage(null);
        guiPickCardPhaseController.getGodToSelect2p2().setImage(null);
        guiPickCardPhaseController.getGodToSelect3p1().setImage(null);
        guiPickCardPhaseController.getGodToSelect3p2().setImage(null);
        guiPickCardPhaseController.getGodToSelect3p3().setImage(null);
        guiPickCardPhaseController.getNamesToSend().clear();
    }

    /**
     * Sets the First player in his box
     * @param names list of names of players
     */
    public void selectFirstPlayer(ArrayList<String> names) {
        guiPickCardPhaseController.getUpPane().setVisible(true);
        guiPickCardPhaseController.getUpTwoCardsPane().setVisible(false);
        guiPickCardPhaseController.getUpThreeCardsPane().setVisible(false);
        guiPickCardPhaseController.getDraw().setVisible(false);
        guiPickCardPhaseController.getStartPlayerPane().setVisible(true);

        AnchorPane dragStartingPlayer = guiPickCardPhaseController.getDragStartingPlayer();
        dragStartingPlayer.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        AnchorPane downPane = guiPickCardPhaseController.getDownPane();
        downPane.setOnDragDropped(e -> {});


        dragStartingPlayer.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String playerName = db.getString();
                guiPickCardPhaseController.getStartPlayerName().setText(playerName);
                guiPickCardPhaseController.getStartPlayerGod().setImage(guiCards.getFullImage(guiPlayersManager.getPlayer(playerName).getCardName()));
                guiPickCardPhaseController.sendToStageTheFirstPlayer(playerName);
                dragStartingPlayer.setOnDragDropped(e -> {});
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        if(names.size() == 2) {

            Button godButtonDown2p1 = guiPickCardPhaseController.getInfoButtonDown2p1();
            ImageView godImageDown2p1 = guiPickCardPhaseController.getGodImageDown2p1();

            godButtonDown2p1.setOnDragDetected(event -> {
                Dragboard db = godButtonDown2p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown2p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiPickCardPhaseController.getPlayerName2p1().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown2p2 = guiPickCardPhaseController.getInfoButtonDown2p2();
            ImageView godImageDown2p2 = guiPickCardPhaseController.getGodImageDown2p2();

            godButtonDown2p2.setOnDragDetected(event -> {
                Dragboard db = godButtonDown2p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown2p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiPickCardPhaseController.getPlayerName2p2().getText());
                db.setContent(content);
                event.consume();
            });

        }
        else {

            Button godButtonDown3p1 = guiPickCardPhaseController.getInfoButtonDown3p1();
            ImageView godImageDown3p1 = guiPickCardPhaseController.getGodImageDown3p1();

            godButtonDown3p1.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiPickCardPhaseController.getPlayerName3p1().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown3p2 = guiPickCardPhaseController.getInfoButtonDown3p2();
            ImageView godImageDown3p2 = guiPickCardPhaseController.getGodImageDown3p2();

            godButtonDown3p2.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiPickCardPhaseController.getPlayerName3p2().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown3p3 = guiPickCardPhaseController.getInfoButtonDown3p3();
            ImageView godImageDown3p3 = guiPickCardPhaseController.getGodImageDown3p3();

            godButtonDown3p3.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p3.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p3.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiPickCardPhaseController.getPlayerName3p3().getText());
                db.setContent(content);
                event.consume();
            });
        }
    }

    /**
     * Shows the whole cards
     */
    public void showCards() {
        if(guiPlayersManager.getNames().size() == 3) {
            if(guiPlayersManager.getPlayer(guiPlayersManager.getNames().get(1)).getCardName() != null) {
                showTwoCardsToSelect();
            }
            else {
                showThreeCardsToSelect();
            }
        }else if(guiPlayersManager.getNames().size() == 2) {
            showTwoCardsToSelect();
        }
    }

    /**
     * Shows the two selected cards
     */
    public void showTwoCardsToSelect() {
        guiPickCardPhaseController.getUpPane().setVisible(true);
        guiPickCardPhaseController.getUpTwoCardsPane().setVisible(true);
        guiPickCardPhaseController.getUpTwoCardsPane().setDisable(false);
    }

    /**
     * Shows the three selected cards
     */
    public void showThreeCardsToSelect() {
        guiPickCardPhaseController.getUpPane().setVisible(true);
        guiPickCardPhaseController.getUpThreeCardsPane().setVisible(true);
        guiPickCardPhaseController.getUpThreeCardsPane().setDisable(false);
    }

}

