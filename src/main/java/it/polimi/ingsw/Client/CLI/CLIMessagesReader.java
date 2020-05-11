package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.MessagesReader;
import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Model.TileSimplified;

import java.io.Serializable;

public class CLIMessagesReader implements MessagesReader {

    Client client;

    public CLIMessagesReader(Client client) {
        this.client = client;
    }

    @Override
    public void read(Serializable object) {

        if(object instanceof PingEvent)
            client.pingReceived();

        else if (object instanceof RequestEvent)
            System.out.println(((RequestEvent) object).getString());

        else if(object instanceof EndLoginEvent)
            client.setLogin(false);

        else if (object instanceof MessageEvent)
            System.out.println(((MessageEvent) object).getString());

        else if (object instanceof AllMessageEvent)
            System.out.println(((AllMessageEvent) object).getString());

        else if (object instanceof ErrorEvent)
            System.out.println((((ErrorEvent) object).getString()));

        else if (object instanceof SuccessEvent)
            System.out.println((((SuccessEvent) object).getString()));

        else if(object instanceof DeckEvent) {
            System.out.println("Deck:");
            ((DeckEvent) object).getDeck().getMiniDeck().forEach(cardSimplified -> System.out.println(cardSimplified.getName()));
        }

        else if(object instanceof CardEvent)
            System.out.println("Choose: " + ((CardEvent) object).getCard().getName());

        else if(object instanceof ActionEvent)
            ((ActionEvent) object).getActions().forEach(System.out::println);

        else if(object instanceof AvailableTilesEvent) {
            for(TileSimplified tileSimplified : ((AvailableTilesEvent) object).getTiles()) {
                System.out.println("x "+ tileSimplified.getX());
                System.out.println("y " + tileSimplified.getY());
            }
        }

        else if(object instanceof ChangeEvent) {
            System.out.println("Change: ");

            for(TileSimplified tileSimplified : ((ChangeEvent) object).getTiles()) {

                System.out.println("x "+ tileSimplified.getX());
                System.out.println("y " + tileSimplified.getY());
                System.out.println("buildLevel " + tileSimplified.getBuildLevel());

                if(tileSimplified.getWorkerSimplified()!=null) {
                    System.out.println("playerID " + tileSimplified.getWorkerSimplified().getPlayerID());
                    System.out.println("workerID " + tileSimplified.getWorkerSimplified().getLocalID());
                }
            }
        }

        else if(object instanceof WinEvent) {
            if(((WinEvent) object).isYouWin()) System.out.println("YOU WIN!");
            else System.out.println(((WinEvent) object).getWinnerName() + "Wins");
        }

        else if(object instanceof LoseEvent)
            System.out.println("You Lose");
    }
}
