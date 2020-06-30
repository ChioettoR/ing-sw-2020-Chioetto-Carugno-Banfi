package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIEventsCommunication implements EventsCommunication {

    private GUIStagesManager stagesManager;
    private GUIRoundStage guiRoundStage;
    private GUILoginStage guiLoginStage;
    private GUIPickCardStage guiPickCardStage;
    private MessagesReader messagesReader;
    private final GUIColorDecoder guiColorDecoder = new GUIColorDecoder();

    public void setStagesManager(GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;
        messagesReader = new MessagesReader(new GUIMessagesHandler(stagesManager));
    }

    public void setGuiLoginStage(GUILoginStage guiLoginStage) {
        this.guiLoginStage = guiLoginStage;
    }

    public void setGuiPickCardStage(GUIPickCardStage guiPickCardStage) {
        this.guiPickCardStage = guiPickCardStage;
    }

    public void setGuiRoundStage(GUIRoundStage guiRoundStage) {
        this.guiRoundStage = guiRoundStage;
    }

    /**
     * Communicates the info about the lobby
     * @param lobbyName name of the lobby
     * @param lobbySize size of the current lobby
     */
    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) {
        messagesReader.lobbyInfo(lobbyName, lobbySize);
    }
    /**
     * Puts a player in a wait position if another player is creating the lobby
     * @param isWaiting boolean parameter for the wait
     */
    @Override
    public void waiting(boolean isWaiting) {
        if(!isWaiting) guiLoginStage.waitWake();
        else guiLoginStage.waitError();
    }
    /**
     * Communicates the end of the login phase
     * @param names list of the players' names
     */
    @Override
    public void endLogin(ArrayList<String> names) {
        stagesManager.endLogin(names);
    }
    /**
     * Sends the message to the reader
     * @param messageID id of the current message
     */
    @Override
    public void message(int messageID) {
        stagesManager.messagesToDirect(messageID);
        messagesReader.read(messageID);
    }
    /**
     * Invokes the CLI methods to build the deck and the cards
     * @param cards name of the cards needed
     */
    @Override
    public void deck(ArrayList<CardSimplified> cards) {
        Platform.runLater(() -> guiPickCardStage.sendDeck(cards));
    }
    /**
     * Communicates the chosen card of the correct player
     * @param playerName name of the player
     * @param cardName name of the card
     */
    @Override
    public void playerChosenCard(String playerName, String cardName) {
        stagesManager.playerChosenCard(playerName, cardName);
    }
    /**
     * Prints the available actions
     * @param actions list of actions
     */
    @Override
    public void action(ArrayList<String> actions) {
        Platform.runLater( () -> guiRoundStage.showActions(actions));
    }
    /**
     * Colors the available tiles on the grid
     * @param tiles list of available tiles
     * @param actionType type of the action
     */
    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {
        guiRoundStage.setSelectedActionType(actionType);
        for(TileSimplified t : tiles) {
            int x = t.getX();
            int y = t.getY();
            Platform.runLater(() -> guiRoundStage.getGuiGridManager().highLight(x,y));
        }
    }

    /**
     * Invoked when something changes in the whole grid during the match
     * @param tiles list of tiles of the grid
     */
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
                GUITile tile = guiRoundStage.getGuiGridManager().getGrid().getTile(t.getX(), t.getY());
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

    /**
     * Applies the effects to the 3D grid
     * @param tiles list of tiles to change
     */
    public void changeEffect(ArrayList<TileSimplified> tiles) {
        for (TileSimplified t : tiles) {
            int x = t.getX();
            int y = t.getY();
            WorkerSimplified w = t.getWorkerSimplified();
            if (w == null) guiRoundStage.getGuiGridManager().setWorkerNull(x, y);
            else guiRoundStage.getGuiGridManager().setWorker(w.getPlayerName(), w.getLocalID(), x, y);
        }
    }
    /**
     * Print method for the victory
     * @param winnerName name of the winner
     */
    @Override
    public void win(String winnerName) {
        Platform.runLater(() -> stagesManager.win(winnerName));
    }
    /**
     * Lose method, if a player loses, that player will be a spectator for the current match
     * @param loserName name of the loser
     * @param youLose true if that player lose, false otherwise
     */
    @Override
    public void lose(String loserName, boolean youLose) {
        Platform.runLater( () -> { if(youLose) Platform.runLater(() -> messagesReader.read(308)); });
        Platform.runLater( () -> Platform.runLater(() -> guiRoundStage.setNewFrame(loserName)));
    }
    /**
     * Prints the effect of the card requested
     * @param cardName name of the card
     */
    @Override
    public void infoEffect(String cardName) { }
    /**
     * Prints the whole deck
     * @param cards  list of cards
     */
    @Override
    public void fullDeck(ArrayList<CardSimplified> cards) { Platform.runLater(() -> guiPickCardStage.sendFullDeck(cards)); }
    /**
     * Handles the selection of the first player
     * @param names list of names of the players
     */
    @Override
    public void firstPlayerSelection(ArrayList<String> names) {
        Platform.runLater(() -> messagesReader.read(116));
        Platform.runLater(() -> stagesManager.selectFirstPlayer(names));
    }
    /**
     * Shows the available colors
     * @param colors list of colors
     */
    @Override
    public void colorsAvailable(ArrayList<PlayerColor> colors) {
        //messagesReader.read(119);
        ArrayList<String> colorsName = new ArrayList<>();
        for(PlayerColor playerColor : colors) {
            colorsName.add(guiColorDecoder.getColorName(playerColor));
        }
        Platform.runLater(() -> stagesManager.showColors(colorsName));
    }
    /**
     * Prints the color chosen for the player
     * @param name name of the player
     * @param color color chosen
     */
    @Override
    public void playerChosenColor(String name, PlayerColor color) {
        stagesManager.getGuiPlayersManager().getPlayer(name).setColor(guiColorDecoder.getColor(color));
    }

    /**
     * Shows the disconnection screen when connection is interrupted
     */
    public void disconnection() {
        Platform.runLater(() -> {
            try { stagesManager.setDisconnectedScene(); }
            catch (IOException e) { e.printStackTrace(); }
        });
    }
}