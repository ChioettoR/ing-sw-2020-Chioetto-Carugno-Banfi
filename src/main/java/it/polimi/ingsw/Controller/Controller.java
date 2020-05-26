package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Communication.Communication;
import it.polimi.ingsw.Events.Client.*;

import java.io.IOException;

public class Controller {

    private final Communication communication;

    public Controller(Communication communication) {
        this.communication = communication;
    }

    public void send(ClientEvent event) throws IOException {

        if (event instanceof AllPlayersCardsEvent)
            communication.allPlayersCards(event.getPlayerID(), ((AllPlayersCardsEvent) event).getCards());

        else if (event instanceof PickCardEvent)
            communication.pick(event.getPlayerID(), ((PickCardEvent) event).getCardName());

        else if (event instanceof PositioningEvent)
            communication.positioning(event.getPlayerID(), ((PositioningEvent) event).getX(), ((PositioningEvent) event).getY());

        else if (event instanceof SelectionEvent)
            communication.selection(event.getPlayerID(), ((SelectionEvent) event).getWorkerID());

        else if (event instanceof ActionSelectEvent)
            communication.actionSelect(event.getPlayerID(), ((ActionSelectEvent) event).getAction());

        else if (event instanceof MoveDecisionEvent)
            communication.move(event.getPlayerID(), ((MoveDecisionEvent) event).getX(), ((MoveDecisionEvent) event).getY());

        else if (event instanceof BuildDecisionEvent) {
            communication.build(event.getPlayerID(), ((BuildDecisionEvent) event).getX(), ((BuildDecisionEvent) event).getY(), ((BuildDecisionEvent) event).getBuildLevel());
        }
    }
}
