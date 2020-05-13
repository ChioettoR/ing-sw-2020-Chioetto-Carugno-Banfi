package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.EventsCommunication;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Model.CardSimplified;
import it.polimi.ingsw.Model.TileSimplified;

import java.util.ArrayList;

public class CLIEventsCommunication implements EventsCommunication {

    CLIStdinReader cliStdinReader;
    MessagesReader messagesReader = new MessagesReader(new CLIMessagesHandler());

    public CLIEventsCommunication(CLIStdinReader cliStdinReader) {
        this.cliStdinReader = cliStdinReader;
    }

    @Override
    public void lobbyInfo(String lobbyName, int lobbySize) { messagesReader.lobbyInfo(lobbyName, lobbySize); }

    @Override
    public void waiting(boolean isWaiting) { cliStdinReader.setWaiting(isWaiting); }

    @Override
    public void endLogin() { cliStdinReader.setLogin(false); }

    @Override
    public void message(int messageID) { messagesReader.read(messageID); }

    @Override
    public void deck(ArrayList<CardSimplified> cards) { cards.forEach(cardSimplified -> System.out.println(cardSimplified.getName())); }

    @Override
    public void card(CardSimplified card) { System.out.println(card.getName()); }

    @Override
    public void action(ArrayList<String> actions) { System.out.println("Deck:"); actions.forEach(System.out::println); }

    @Override
    public void availableTiles(ArrayList<TileSimplified> tiles) { for(TileSimplified tileSimplified : tiles) { System.out.println("x "+ tileSimplified.getX()); System.out.println("y " + tileSimplified.getY()); } }

    @Override
    public void change(ArrayList<TileSimplified> tiles) { for(TileSimplified tileSimplified : tiles) { System.out.println("x "+ tileSimplified.getX()); System.out.println("y " + tileSimplified.getY()); } }

    @Override
    public void win(boolean youWin, String winnerName) { if(youWin) System.out.println("YOU WIN!"); else System.out.println((winnerName + "Wins")); }

    @Override
    public void lose() { System.out.println("YOU LOSE!"); }
}
