package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Model.CardSimplified;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class GUIDrawStage{

    GUIDrawPhaseController guiDrawPhaseController;
    GUIPlayersManager guiPlayersManager;
    GUICards guiCards = new GUICards();

    ArrayList<CardSimplified> deck = new ArrayList<>();


    public void start(Stage stage, GUIStagesManager stagesManager, GUIPlayersManager guiPlayersManager){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/DrawPhase/drawPhase.fxml"));
        Parent root;
        try {
            root = loader.load();
            guiDrawPhaseController = loader.getController();
            guiDrawPhaseController.setStagesManager(stagesManager);
            guiDrawPhaseController.setGuiCards(guiCards);
            this.guiPlayersManager = guiPlayersManager;
            setDownPaneForDrag();
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

    public void readMessage(String message) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getErrorPane().setVisible(false);
        guiDrawPhaseController.getMessagePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText(message);
    }

    public void readRequest(String request) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getErrorPane().setVisible(false);
        guiDrawPhaseController.getMessagePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText(request);
    }

    public void readError(String error) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getErrorPane().setVisible(true);
        guiDrawPhaseController.getMessagePane().setVisible(false);
        guiDrawPhaseController.getErrorText().setText(error);
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

    public void sendDeck(ArrayList<CardSimplified> cards) {

        for (CardSimplified card : cards) guiCards.addDescription(card);

        if(cards.size() == 2) {
            guiDrawPhaseController.getUpPane().setVisible(true);
            guiDrawPhaseController.getUpThreeCardsPane().setDisable(true);
            guiDrawPhaseController.getUpThreeCardsPane().setVisible(false);
            guiDrawPhaseController.getUpTwoCardsPane().setDisable(false);
            guiDrawPhaseController.getUpTwoCardsPane().setVisible(true);
            guiDrawPhaseController.getGodName2p1().setText(cards.get(0).getName());
            guiDrawPhaseController.getGodName2p2().setText(cards.get(1).getName());
            guiDrawPhaseController.getGodImageUp2p1().setImage(guiCards.getFullImage(cards.get(0).getName()));
            guiDrawPhaseController.getGodImageUp2p2().setImage(guiCards.getFullImage(cards.get(1).getName()));

            Button godButtonUp2p1 = guiDrawPhaseController.getInfoButtonUp2p1();
            ImageView godImageUp2p1 = guiDrawPhaseController.getGodImageUp2p1();

            godButtonUp2p1.setOnDragDetected(event -> {
                Dragboard db = godButtonUp2p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp2p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(0).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp2p2 = guiDrawPhaseController.getInfoButtonUp2p2();
            ImageView godImageUp2p2 = guiDrawPhaseController.getGodImageUp2p2();

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
            guiDrawPhaseController.getUpPane().setVisible(true);
            guiDrawPhaseController.getUpTwoCardsPane().setDisable(true);
            guiDrawPhaseController.getUpTwoCardsPane().setVisible(false);
            guiDrawPhaseController.getUpThreeCardsPane().setDisable(false);
            guiDrawPhaseController.getUpThreeCardsPane().setVisible(true);
            guiDrawPhaseController.getGodName3p1().setText(cards.get(0).getName());
            guiDrawPhaseController.getGodName3p2().setText(cards.get(1).getName());
            guiDrawPhaseController.getGodName3p3().setText(cards.get(2).getName());
            guiDrawPhaseController.getGodImageUp3p1().setImage(guiCards.getFullImage(cards.get(0).getName()));
            guiDrawPhaseController.getGodImageUp3p2().setImage(guiCards.getFullImage(cards.get(1).getName()));
            guiDrawPhaseController.getGodImageUp3p3().setImage(guiCards.getFullImage(cards.get(2).getName()));

            Button godButtonUp3p1 = guiDrawPhaseController.getInfoButtonUp3p1();
            ImageView godImageUp3p1 = guiDrawPhaseController.getGodImageUp3p1();

            godButtonUp3p1.setOnDragDetected(event -> {
                Dragboard db = godButtonUp3p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp3p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(0).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp3p2 = guiDrawPhaseController.getInfoButtonUp3p2();
            ImageView godImageUp3p2 = guiDrawPhaseController.getGodImageUp3p2();

            godButtonUp3p2.setOnDragDetected(event -> {
                Dragboard db = godButtonUp3p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageUp3p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(cards.get(1).getName());
                db.setContent(content);
                event.consume();
            });

            Button godButtonUp3p3 = guiDrawPhaseController.getInfoButtonUp3p3();
            ImageView godImageUp3p3 = guiDrawPhaseController.getGodImageUp3p3();

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

    public void setDownPaneForDrag() {
        AnchorPane downPane = guiDrawPhaseController.getDownPane();
        downPane.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        downPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                guiDrawPhaseController.sendToStagesManager(db.getString());
                guiDrawPhaseController.getUpPane().setVisible(false);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void setCardsImages(String playerName, String cardName) {
        if(guiDrawPhaseController.getPlayerName2p1().getText().equalsIgnoreCase(playerName)){
            guiDrawPhaseController.getGodImageDown2p1().setImage(guiCards.getFullImage(cardName));
            guiDrawPhaseController.getInfoButtonDown2p1().setDisable(false);
        }
        else if(guiDrawPhaseController.getPlayerName2p2().getText().equalsIgnoreCase(playerName)) {
            guiDrawPhaseController.getGodImageDown2p2().setImage(guiCards.getFullImage(cardName));
            guiDrawPhaseController.getInfoButtonDown2p2().setDisable(false);
        }
        else if(guiDrawPhaseController.getPlayerName3p1().getText().equalsIgnoreCase(playerName)) {
            guiDrawPhaseController.getGodImageDown3p1().setImage(guiCards.getFullImage(cardName));
            guiDrawPhaseController.getInfoButtonDown3p1().setDisable(false);
        }
        else if(guiDrawPhaseController.getPlayerName3p2().getText().equalsIgnoreCase(playerName)) {
            guiDrawPhaseController.getGodImageDown3p2().setImage(guiCards.getFullImage(cardName));
            guiDrawPhaseController.getInfoButtonDown3p2().setDisable(false);
        }
        else if(guiDrawPhaseController.getPlayerName3p3().getText().equalsIgnoreCase(playerName)) {
            guiDrawPhaseController.getGodImageDown3p3().setImage(guiCards.getFullImage(cardName));
            guiDrawPhaseController.getInfoButtonDown3p3().setDisable(false);
        }
    }

    public void roundTransition(int seconds) {
        guiDrawPhaseController.getMiddlePane().setVisible(true);
        guiDrawPhaseController.getErrorPane().setVisible(false);
        guiDrawPhaseController.getMessagePane().setVisible(true);
        guiDrawPhaseController.getMessageText().setText("Round starts in: " + seconds);
    }

    public void sendFullDeck(ArrayList<CardSimplified> cards) {

        deck = new ArrayList<>(cards);

        int godIndex;

        for (CardSimplified card : cards) guiCards.addDescription(card);

        guiDrawPhaseController.setFullDeck(cards);
        guiDrawPhaseController.getDraw().setVisible(true);
        godIndex = guiDrawPhaseController.getGodIndex();
        String godName = cards.get(godIndex).getName();
        guiDrawPhaseController.getGodImageToChoose().setImage(guiCards.getFullImage(godName));
        guiDrawPhaseController.getGodNameToChoose().setText(godName);

        Button infoButtonToChoose = guiDrawPhaseController.getInfoButtonToChoose();
        ImageView godImageToChoose = guiDrawPhaseController.getGodImageToChoose();

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

    public void threeCardsShow() {
        guiDrawPhaseController.getCardsToSelect().setVisible(true);
        guiDrawPhaseController.getCardsToSelect().setDisable(false);
        guiDrawPhaseController.getTwoCardsPane().setVisible(false);
        guiDrawPhaseController.getTwoCardsPane().setDisable(true);
        guiDrawPhaseController.getThreeCardsPane().setDisable(false);
        guiDrawPhaseController.getThreeCardsPane().setVisible(true);
        setRightPaneForDrag();
    }

    public void twoCardsShow() {
        guiDrawPhaseController.getCardsToSelect().setVisible(true);
        guiDrawPhaseController.getCardsToSelect().setDisable(false);
        guiDrawPhaseController.getThreeCardsPane().setVisible(false);
        guiDrawPhaseController.getThreeCardsPane().setDisable(true);
        guiDrawPhaseController.getTwoCardsPane().setDisable(false);
        guiDrawPhaseController.getTwoCardsPane().setVisible(true);
        setRightPaneForDrag();
    }

    public void setRightPaneForDrag() {
        AnchorPane rightPane = guiDrawPhaseController.getCardsToSelect();
        rightPane.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        rightPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String godName = db.getString();
                if(guiDrawPhaseController.getThreeCardsPane().isVisible()) {
                    if(guiDrawPhaseController.getGodToSelect3p1().getImage() == null) {
                        guiDrawPhaseController.getGodToSelect3p1().setImage(guiCards.getFullImage(godName));
                        guiDrawPhaseController.addNamesToSend(godName);
                    }
                    else if(guiDrawPhaseController.getGodToSelect3p2().getImage() == null)  {
                        guiDrawPhaseController.getGodToSelect3p2().setImage(guiCards.getFullImage(godName));
                        guiDrawPhaseController.addNamesToSend(godName);
                    }
                    else if(guiDrawPhaseController.getGodToSelect3p3().getImage() == null) {
                        guiDrawPhaseController.getGodToSelect3p3().setImage(guiCards.getFullImage(godName));
                        guiDrawPhaseController.addNamesToSend(godName);
                        guiDrawPhaseController.sendToServer();
                    }
                }
                else {
                    if(guiDrawPhaseController.getGodToSelect2p1().getImage() == null) {
                        guiDrawPhaseController.getGodToSelect2p1().setImage(guiCards.getFullImage(godName));
                        guiDrawPhaseController.addNamesToSend(godName);
                    }
                    else if(guiDrawPhaseController.getGodToSelect2p2().getImage() == null) {
                        guiDrawPhaseController.getGodToSelect2p2().setImage(guiCards.getFullImage(godName));
                        guiDrawPhaseController.addNamesToSend(godName);
                        guiDrawPhaseController.sendToServer();
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void showAgain() {
        guiDrawPhaseController.getDraw().setDisable(false);
        guiDrawPhaseController.getDraw().setVisible(true);
        guiDrawPhaseController.getCardsToSelect().setDisable(false);
        guiDrawPhaseController.getCardsToSelect().setVisible(true);
        guiDrawPhaseController.getGodToSelect2p1().setImage(null);
        guiDrawPhaseController.getGodToSelect2p2().setImage(null);
        guiDrawPhaseController.getGodToSelect3p1().setImage(null);
        guiDrawPhaseController.getGodToSelect3p2().setImage(null);
        guiDrawPhaseController.getGodToSelect3p3().setImage(null);
        guiDrawPhaseController.getNamesToSend().clear();
    }

    public void selectFirstPlayer(ArrayList<String> names) {
        guiDrawPhaseController.getUpPane().setVisible(true);
        guiDrawPhaseController.getUpTwoCardsPane().setVisible(false);
        guiDrawPhaseController.getUpThreeCardsPane().setVisible(false);
        guiDrawPhaseController.getDraw().setVisible(false);
        guiDrawPhaseController.getStartPlayerPane().setVisible(true);

        AnchorPane dragStartingPlayer = guiDrawPhaseController.getDragStartingPlayer();
        dragStartingPlayer.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        AnchorPane downPane = guiDrawPhaseController.getDownPane();
        downPane.setOnDragDropped(e -> {});


        dragStartingPlayer.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String playerName = db.getString();
                guiDrawPhaseController.getStartPlayerName().setText(playerName);
                guiDrawPhaseController.getStartPlayerGod().setImage(guiCards.getFullImage(guiPlayersManager.getPlayer(playerName).getCardName()));
                guiDrawPhaseController.sendToStageTheFirstPlayer(playerName);
                dragStartingPlayer.setOnDragDropped(e -> {});
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        if(names.size() == 2) {

            Button godButtonDown2p1 = guiDrawPhaseController.getInfoButtonDown2p1();
            ImageView godImageDown2p1 = guiDrawPhaseController.getGodImageDown2p1();

            godButtonDown2p1.setOnDragDetected(event -> {
                Dragboard db = godButtonDown2p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown2p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiDrawPhaseController.getPlayerName2p1().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown2p2 = guiDrawPhaseController.getInfoButtonDown2p2();
            ImageView godImageDown2p2 = guiDrawPhaseController.getGodImageDown2p2();

            godButtonDown2p2.setOnDragDetected(event -> {
                Dragboard db = godButtonDown2p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown2p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiDrawPhaseController.getPlayerName2p2().getText());
                db.setContent(content);
                event.consume();
            });

        }
        else {

            Button godButtonDown3p1 = guiDrawPhaseController.getInfoButtonDown3p1();
            ImageView godImageDown3p1 = guiDrawPhaseController.getGodImageDown3p1();

            godButtonDown3p1.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p1.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p1.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiDrawPhaseController.getPlayerName3p1().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown3p2 = guiDrawPhaseController.getInfoButtonDown3p2();
            ImageView godImageDown3p2 = guiDrawPhaseController.getGodImageDown3p2();

            godButtonDown3p2.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p2.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p2.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiDrawPhaseController.getPlayerName3p2().getText());
                db.setContent(content);
                event.consume();
            });

            Button godButtonDown3p3 = guiDrawPhaseController.getInfoButtonDown3p3();
            ImageView godImageDown3p3 = guiDrawPhaseController.getGodImageDown3p3();

            godButtonDown3p3.setOnDragDetected(event -> {
                Dragboard db = godButtonDown3p3.startDragAndDrop(TransferMode.ANY);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                db.setDragView(godImageDown3p3.snapshot(snapshotParameters, null));
                ClipboardContent content = new ClipboardContent();
                content.putString(guiDrawPhaseController.getPlayerName3p3().getText());
                db.setContent(content);
                event.consume();
            });
        }
    }

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

    public void showTwoCardsToSelect() {
        guiDrawPhaseController.getUpPane().setVisible(true);
        guiDrawPhaseController.getUpTwoCardsPane().setVisible(true);
        guiDrawPhaseController.getUpTwoCardsPane().setDisable(false);
    }

    public void showThreeCardsToSelect() {
        guiDrawPhaseController.getUpPane().setVisible(true);
        guiDrawPhaseController.getUpThreeCardsPane().setVisible(true);
        guiDrawPhaseController.getUpThreeCardsPane().setDisable(false);
    }

}

