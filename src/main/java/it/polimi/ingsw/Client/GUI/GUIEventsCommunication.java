package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIEventsCommunication implements EventsCommunication {

    GUIStagesManager stagesManager;
    GUIRoundStage guiRoundStage;
    GUILoginStage guiLoginStage;
    GUIDrawStage guiDrawStage;
    private MessagesReader messagesReader;
    GUIColorDecoder guiColorDecoder = new GUIColorDecoder();

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
        messagesReader = new MessagesReader(new GUIMessagesHandler(stagesManager));
    }

    public void setGuiLoginStage(GUILoginStage guiLoginStage) {
        this.guiLoginStage = guiLoginStage;
    }

    public void setGuiDrawStage(GUIDrawStage guiDrawStage) {
        this.guiDrawStage = guiDrawStage;
    }

    public void setGuiRoundStage(GUIRoundStage guiRoundStage) {
        this.guiRoundStage = guiRoundStage;
    }

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) {
        messagesReader.lobbyInfo(lobbyName, lobbySize);
    }

    @Override
    public void waiting(boolean isWaiting) {
        if(!isWaiting) guiLoginStage.waitWake();
        else guiLoginStage.waitError();
    }

    @Override
    public void endLogin(ArrayList<String> names) {
        stagesManager.endLogin(names);
    }

    @Override
    public void message(int messageID) {
        stagesManager.messagesToDirect(messageID);
        messagesReader.read(messageID);
    }

    @Override
    public void deck(ArrayList<CardSimplified> cards) {
        Platform.runLater(() -> guiDrawStage.sendDeck(cards));
    }

    @Override
    public void playerChosenCard(String playerName, String cardName) {
        stagesManager.playerChosenCard(playerName, cardName);
    }

    @Override
    public void action(ArrayList<String> actions) {
        Platform.runLater( () -> guiRoundStage.showActions(actions));
    }

    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {
        guiRoundStage.setSelectedActionType(actionType);
        for(TileSimplified t : tiles) {
            int x = t.getX();
            int y = t.getY();
            Platform.runLater(() -> guiRoundStage.getGuiGridManager().highLight(x,y));
        }
    }

    @Override
    public void change(ArrayList<TileSimplified> tiles) {
        Platform.runLater( () -> {
            guiRoundStage.getGuiGridManager().deColor();

            for(TileSimplified t : tiles) {
                int level = t.getBuildLevel();
                int x = t.getX();
                int y = t.getY();
                guiRoundStage.getGuiGridManager().build(level, x, y);
            }

            ArrayList<TileSimplified> tileWithWorkerMaybeDeleted = new ArrayList<>();
            ArrayList<GUIWorker> workersMaybeDeleted = new ArrayList<>();

            ArrayList<TileSimplified> tilesWithWorkerNull = (ArrayList<TileSimplified>) tiles.stream().filter(tileSimplified -> tileSimplified.getWorkerSimplified() == null).collect(Collectors.toList());
            for (TileSimplified t : tilesWithWorkerNull) {
                GUITile tile = guiRoundStage.guiGridManager.getGrid().getTile(t.getX(), t.getY());
                if (tile.getGUIWorker() != null) {
                    workersMaybeDeleted.add(tile.getGUIWorker());
                    tileWithWorkerMaybeDeleted.add(t);
                }
            }

            ArrayList<TileSimplified> tilesWithWorkerNotDeleted = new ArrayList<>();
            ArrayList<GUIWorker> workerNotDeleted = new ArrayList<>();

            for (GUIWorker w : workersMaybeDeleted) {
                for (TileSimplified t : tiles) {
                    WorkerSimplified workerSimplified = t.getWorkerSimplified();
                    if (workerSimplified != null && workerSimplified.getLocalID() == w.getWorkerID() && workerSimplified.getPlayerName().equals(w.getPlayerName())) {
                        workerNotDeleted.add(w);
                        tilesWithWorkerNotDeleted.add(t);
                    }
                }
            }

            ArrayList<TileSimplified> tileWithWorkerDeleted = new ArrayList<>(tileWithWorkerMaybeDeleted);
            tileWithWorkerDeleted.removeAll(tilesWithWorkerNotDeleted);
            ArrayList<TileSimplified> finalTiles = new ArrayList<>(tiles);
            finalTiles.removeAll(tileWithWorkerDeleted);

            ArrayList<GUIWorker> workerDeleted = new ArrayList<>(workersMaybeDeleted);
            workerDeleted.removeAll(workerNotDeleted);

            for (GUIWorker g : workerDeleted) {
                guiRoundStage.getGuiGridManager().deleteWorker(g);
            }

            changeEffect(finalTiles);
        });
    }

    public void changeEffect(ArrayList<TileSimplified> tiles) {
        for (TileSimplified t : tiles) {
            int x = t.getX();
            int y = t.getY();
            WorkerSimplified w = t.getWorkerSimplified();
            if (w == null) guiRoundStage.getGuiGridManager().setWorkerNull(x, y);
            else guiRoundStage.getGuiGridManager().setWorker(w.getPlayerName(), w.getLocalID(), x, y);
        }
    }

    @Override
    public void win(String winnerName) {
        Platform.runLater(() -> stagesManager.win(winnerName));
    }

    @Override
    public void lose() {
        Platform.runLater(() -> messagesReader.read(308));
    }

    @Override
    public void infoEffect(String cardName) { }

    @Override
    public void fullDeck(ArrayList<CardSimplified> cards) { Platform.runLater(() -> guiDrawStage.sendFullDeck(cards)); }

    @Override
    public void firstPlayerSelection(ArrayList<String> names) {
        Platform.runLater(() -> messagesReader.read(116));
        Platform.runLater(() -> stagesManager.selectFirstPlayer(names));
    }

    @Override
    public void colorsAvailable(ArrayList<PlayerColor> colors) {
        messagesReader.read(119);
        ArrayList<String> colorsName = new ArrayList<>();
        for(PlayerColor playerColor : colors) {
            colorsName.add(guiColorDecoder.getColorName(playerColor));
        }
        //TODO : show colors
    }

    @Override
    public void playerChosenColor(String name, PlayerColor color) {
        stagesManager.getGuiPlayersManager().getPlayer(name).setColor(guiColorDecoder.getColor(color));
    }

    public void disconnection() {
        Platform.runLater(() -> {
            try { stagesManager.setDisconnectedScene(); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }
}