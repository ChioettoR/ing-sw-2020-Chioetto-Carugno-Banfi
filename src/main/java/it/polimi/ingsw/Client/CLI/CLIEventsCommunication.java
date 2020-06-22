package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.PlayerColor;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class CLIEventsCommunication implements EventsCommunication {

    private final CLIStdinReader cliStdinReader;
    private final MessagesReader messagesReader = new MessagesReader(new CLIMessagesHandler());
    final CLIDeck cliDeck = new CLIDeck();
    final CLICardBuilder cliCardBuilder = new CLICardBuilder();
    final CLIActionPrinter cliActionPrinter = new CLIActionPrinter();
    final CLIPlayersManager cliPlayersManager = new CLIPlayersManager();
    final CLIGridManager cliGridManager = new CLIGridManager(cliPlayersManager);
    final CLIColorDecoder cliColorDecoder = new CLIColorDecoder();
    final int maxEffectLength = 85;

    public CLIEventsCommunication(CLIStdinReader cliStdinReader) {
        this.cliStdinReader = cliStdinReader;
    }

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) { messagesReader.lobbyInfo(lobbyName, lobbySize); }

    @Override
    public void waiting(boolean isWaiting) { cliStdinReader.setWaiting(isWaiting); }

    @Override
    public void message(int messageID) { messagesReader.read(messageID); }

    @Override
    public void endLogin(ArrayList<String> names) {
        cliStdinReader.setLogin(false);
        for(String name : names) cliPlayersManager.addPlayer(name);
    }

    /**
     * Invokes the CLI methods to build the deck and the cards
     * @param cards name of the cards needed
     */
    @Override
    public void deck(ArrayList<CardSimplified> cards) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> effects = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for(CardSimplified c : cards) {
            names.add(c.getName());
            effects.add(c.getEffectName());
            descriptions.add(c.getDescription());
        }

        ArrayList<StringBuilder> stringBuilders = cliCardBuilder.createCards(names, effects, descriptions);
        cliDeck.createDeck(stringBuilders);
    }

    @Override
    public void playerChosenCard(String playerName, String cardName) {
        cliPlayersManager.getPlayer(playerName).setCardName(cardName);
    }

    /**
     * Prints the available actions
     * @param actions list of actions
     */
    @Override
    public void action(ArrayList<String> actions) {
        messagesReader.read(111);
        cliActionPrinter.printAction(actions);
    }

    /**
     * Colors the available tiles on the grid
     * @param tiles list of available tiles
     * @param actionType type of the action
     */
    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {
        cliStdinReader.setSelectedActionType(actionType);
        for(TileSimplified t : tiles) cliGridManager.borderColorTile(t.getX(), t.getY());
        cliGridManager.printGrid(cliPlayersManager.getDisplayStrings(), cliPlayersManager.getColors());
        for(TileSimplified t : tiles) cliGridManager.resetColorTile(t.getX(), t.getY());
    }

    /**
     * Method invoked when a change is done on the tiles
     * @param tiles list of changed tiles
     */
    @Override
    public void change(ArrayList<TileSimplified> tiles) {
        cliGridManager.changeGrid(tiles);
        cliGridManager.printGrid(cliPlayersManager.getDisplayStrings(), cliPlayersManager.getColors());
    }

    /**
     * Print method for the victory
     * @param winnerName name of the winner
     */
    @Override
    public void win(String winnerName) {
        System.out.println((winnerName + " WINS!"));
        cliStdinReader.getClient().closeConnection();
    }

    /**
     * Lose method, if a player loses, that player will be a spectator for the current match
      * @param loserName name of the loser
     * @param youLose true if that player lose, false otherwise
     */
    @Override
    public void lose(String loserName, boolean youLose) {
        if(youLose) {
            cliStdinReader.setSpectator(true);
            messagesReader.read(308);
        }
        cliPlayersManager.deleteName(loserName);
    }

    /**
     * Prints the effect of the card requested
     * @param cardName name of the card
     */
    @Override
    public void infoEffect(String cardName) {
        String[] effectLines;
        String effect = cliCardBuilder.getDescription(cardName);

        if(effect==null) messagesReader.read(407);

        else if(effect.length() < maxEffectLength) System.out.println(effect);

        else {
            effectLines = effect.split("@", 0);
            for(String s : effectLines) System.out.println(s);
        }
    }

    /**
     * Prints the whole deck
     * @param cards  list of cards
     */
    @Override
    public void fullDeck(ArrayList<CardSimplified> cards) {
        for(int i = 0; i<cards.size(); i++) {
            cliCardBuilder.addDescription(cards.get(i).getName(), cards.get(i).getDescription());
            System.out.print(cards.get(i).getName() + "  ");
            if (i == cards.size()/2)
                System.out.println();
        }
        System.out.println();
    }

    /**
     * Handles the selection of the first player
     * @param names list of names of the players
     */
    @Override
    public void firstPlayerSelection(ArrayList<String> names) {
        messagesReader.read(116);
        cliActionPrinter.printAction(names);
    }

    /**
     * Shows the available colors
     * @param colors list of colors
     */
    @Override
    public void colorsAvailable(ArrayList<PlayerColor> colors) {
        for(PlayerColor playerColor : colors) {
            System.out.print(cliColorDecoder.getColor(playerColor).escape() + cliColorDecoder.getColorName(playerColor) + Color.RESET);
            if(colors.indexOf(playerColor)!=colors.size()-1) System.out.print("  ");
        }
        System.out.println();
    }

    /**
     * Prints the color chosen for the player
     * @param name name of the player
     * @param color color chosen
     */
    @Override
    public void playerChosenColor(String name, PlayerColor color) {
        cliPlayersManager.getPlayer(name).setColor(cliColorDecoder.getColor(color));
        cliGridManager.printGrid(cliPlayersManager.getDisplayStrings(), cliPlayersManager.getColors());
    }
}