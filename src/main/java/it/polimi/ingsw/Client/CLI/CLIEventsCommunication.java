package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.ActionType;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class CLIEventsCommunication implements EventsCommunication {

    private final CLIStdinReader cliStdinReader;
    private final MessagesReader messagesReader = new MessagesReader(new CLIMessagesHandler());
    CLIGridManager cliGridManager = new CLIGridManager();
    CLIDeck cliDeck = new CLIDeck();
    CLICardBuilder cliCardBuilder = new CLICardBuilder();
    int maxEffectLenght = 85;

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
        cliGridManager.addPlayers(names);
    }

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
    public void card(CardSimplified card) {
        System.out.println(card.getName());
        cliGridManager.printGrid();
    }

    @Override
    public void playerChosenCard(String playerName, String cardName) {
        System.out.println("player " + playerName);
        System.out.println("card" + cardName);
    }

    @Override
    public void action(ArrayList<String> actions) {
        messagesReader.read(111);
        actions.forEach(System.out::println);
    }

    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles, ActionType actionType) {
        cliStdinReader.setSelectedActionType(actionType);
        for(TileSimplified t : tiles) cliGridManager.borderColorTile(t.getX(), t.getY());
        cliGridManager.printGrid();
        for(TileSimplified t : tiles) cliGridManager.resetColorTile(t.getX(), t.getY());
    }

    @Override
    public void change(ArrayList<TileSimplified> tiles) {
        cliGridManager.changeGrid(tiles);
        cliGridManager.printGrid();
    }

    @Override
    public void win(boolean youWin, String winnerName) { if(youWin) System.out.println("YOU WIN!"); else System.out.println((winnerName + "Wins")); }

    @Override
    public void lose() { System.out.println("YOU LOSE!"); }

    @Override
    public void infoEffect(String cardName) {
        String[] effectLines;
        String effect = cliCardBuilder.getDescription(cardName);
        if(effect.length() < maxEffectLenght){
            if(effect==null) messagesReader.read(407);
            else System.out.println(effect);
        }else {
            effectLines = effect.split("@", 0);
            for(String s : effectLines)
                System.out.println(s);
        }
    }
}
