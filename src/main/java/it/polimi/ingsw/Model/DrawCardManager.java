package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.Server.CardObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DrawCardManager extends CardObservable{
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    ArrayList<CardSimplified> pickedCards;
    ArrayList<CardSimplified> remainingCards;

    public DrawCardManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void draw(int playerID) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.DRAWING))
            return;

        ArrayList<CardSimplified> cardsSimplified = new ArrayList<>();
        for (int i=0; i<playersManager.getPlayersNumber(); i++)
            cardsSimplified.add(Deck.getDeck().pickCard().simplify());

        MiniDeckSimplified miniDeckSimplified = new MiniDeckSimplified(cardsSimplified);
        pickedCards = new ArrayList<>();
        remainingCards = miniDeckSimplified.getMiniDeck();
        notifyDeck(new DeckEvent(miniDeckSimplified));
        playersManager.nextPlayer();
        notifyRequest(new RequestEvent("Pick your card", playersManager.getCurrentPlayer().getID()));
        stateManager.setGameState(GameState.PICKING);
    }

    public void pick(int playerID, String cardName) throws IOException {
        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.PICKING))
            return;

        CardSimplified chosenCardSimplified;

        if(cardName.equals("")) {
            notifyError(new ErrorEvent("Please, specify the name of the card you want to pick", playersManager.getCurrentPlayer().getID()));
            return;
        }

        for(CardSimplified cardSimplified : remainingCards) {

            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {
                chosenCardSimplified = cardSimplified;
                remainingCards.remove(chosenCardSimplified);
                pickedCards.add(chosenCardSimplified);
                playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(cardSimplified.getName()));
                notifyCard(new CardEvent(chosenCardSimplified, playersManager.getCurrentPlayer().getID()));
                ArrayList<CardSimplified> cardsSimplifiedCopy = new ArrayList<>(remainingCards);
                notifyDeck(new DeckEvent(new MiniDeckSimplified(cardsSimplifiedCopy)));
                playersManager.nextPlayer();

                if(remainingCards.size()!=1)
                    notifyRequest(new RequestEvent("Pick your card", playersManager.getCurrentPlayer().getID()));

                else {
                    playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(remainingCards.get(0).getName()));
                    notifyCard(new CardEvent(remainingCards.get(0), playersManager.getCurrentPlayer().getID()));
                    playersManager.nextPlayer();
                    notifyRequest(new RequestEvent("Position your first worker", playersManager.getCurrentPlayer().getID()));
                    stateManager.setGameState(GameState.POSITIONING);
                }
                return;
            }
        }

        for(CardSimplified cardSimplified : pickedCards) {
            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {
                notifyError(new ErrorEvent("Card already picked by another player", playersManager.getCurrentPlayer().getID()));
                notifyRequest(new RequestEvent("Pick another card", playersManager.getCurrentPlayer().getID()));
                return;
            }
        }

        notifyError(new ErrorEvent("Invalid card", playersManager.getCurrentPlayer().getID()));
        notifyRequest(new RequestEvent("Pick another card", playersManager.getCurrentPlayer().getID()));
    }

    public void transition() throws IOException {
        notifySuccess(new SuccessEvent("All players joined the lobby", -1));
        notifyAllMessage(new AllMessageEvent("The game started"));
        notifyRequest(new RequestEvent("Please, draw " + playersManager.getPlayersNumber() + " cards", PlayersManager.getPlayersManager().nextPlayer().getID()));
    }
}
