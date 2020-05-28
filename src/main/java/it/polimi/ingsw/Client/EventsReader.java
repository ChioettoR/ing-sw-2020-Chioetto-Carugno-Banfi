package it.polimi.ingsw.Client;
import it.polimi.ingsw.Events.Server.WaitingEvent;
import it.polimi.ingsw.Events.Server.*;

import java.io.Serializable;

public class EventsReader {

    Client client;
    EventsCommunication eventsCommunication;

    public EventsReader(Client client, EventsCommunication eventsCommunication) {
        this.client = client;
        this.eventsCommunication = eventsCommunication;
    }

    public void read(Serializable object) {

        if(object instanceof LobbyInfoEvent) eventsCommunication.lobbyInfo(((LobbyInfoEvent) object).getLobbyName(), ((LobbyInfoEvent) object).getLobbySize());

        else if(object instanceof PlayerChosenCardEvent) eventsCommunication.playerChosenCard(((PlayerChosenCardEvent) object).getPlayerName(), ((PlayerChosenCardEvent) object).getCardName());

        else if(object instanceof WaitingEvent) eventsCommunication.waiting(((WaitingEvent) object).isWaiting());

        else if(object instanceof SpectatorEvent) eventsCommunication.spectator();

        else if(object instanceof EndLoginEvent) eventsCommunication.endLogin(((EndLoginEvent) object).getNames());

        else if(object instanceof MessageEvent) eventsCommunication.message(((MessageEvent) object).getMessageID());

        else if(object instanceof DeckEvent) eventsCommunication.deck(((DeckEvent) object).getDeck().getMiniDeck());

        else if(object instanceof CardEvent) eventsCommunication.card(((CardEvent) object).getCard());

        else if(object instanceof ActionEvent) eventsCommunication.action(((ActionEvent) object).getActions());

        else if(object instanceof AvailableTilesEvent) eventsCommunication.availableTiles(((AvailableTilesEvent) object).getTiles(), ((AvailableTilesEvent) object).getActionType());

        else if(object instanceof ChangeEvent) eventsCommunication.change(((ChangeEvent) object).getTiles());

        else if(object instanceof  WinEvent) eventsCommunication.win(((WinEvent) object).isYouWin(), ((WinEvent) object).getWinnerName());

        else if(object instanceof LoseEvent) eventsCommunication.lose();

        else if(object instanceof FullDeckEvent) eventsCommunication.fullDeck(((FullDeckEvent) object).getCards());
    }
}
