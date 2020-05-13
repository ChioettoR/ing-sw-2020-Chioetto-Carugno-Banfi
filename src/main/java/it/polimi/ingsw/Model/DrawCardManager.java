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

        ArrayList<CardSimplified> cardsSimplified = pickCardsFromDeck();

        sendCards(cardsSimplified);
    }

    public void pick(int playerID, String cardName) throws IOException {
        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.PICKING))
            return;

        if(cardName.equals("")) {
            notifyError(new ErrorEvent("Please, specify the name of the card you want to pick", playersManager.getCurrentPlayer().getID())); //4-05
            return;
        }
        boolean rightCard = playerPicksTheCard(cardName);
        if(rightCard){
            return;
        }

        checkWrongCard(cardName);
    }

    public void transition() throws IOException {
        notifySuccess(new SuccessEvent("All players joined the lobby", -1)); //2-01
        notifyAllMessage(new AllMessageEvent("The game started"));
        notifyRequest(new RequestEvent("Please, draw " + playersManager.getPlayersNumber() + " cards", PlayersManager.getPlayersManager().nextPlayer().getID())); //1-05
    }

    private ArrayList<CardSimplified> pickCardsFromDeck() {
        ArrayList<CardSimplified> cardsSimplified = new ArrayList<>();
        for (int i=0; i<playersManager.getPlayersNumber(); i++)
            cardsSimplified.add(Deck.getDeck().pickCard().simplify());
        return cardsSimplified;
    }

    private void sendCards(ArrayList<CardSimplified> cardsSimplified) throws IOException {
        MiniDeckSimplified miniDeckSimplified = new MiniDeckSimplified(cardsSimplified);
        pickedCards = new ArrayList<>();
        remainingCards = miniDeckSimplified.getMiniDeck();
        notifyDeck(new DeckEvent(miniDeckSimplified));
        playersManager.nextPlayer();
        notifyRequest(new RequestEvent("Pick your card", playersManager.getCurrentPlayer().getID())); //1-06
        stateManager.setGameState(GameState.PICKING);
    }

    private boolean playerPicksTheCard(String cardName) throws IOException {
        for(CardSimplified cardSimplified : remainingCards) {

            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {

                pickCard(cardSimplified);

                if(remainingCards.size()!=1)
                    notifyRequest(new RequestEvent("Pick your card", playersManager.getCurrentPlayer().getID()));  //1-06

                else {
                    nextPhase();
                }
                return true;
            }
        }
        return false;
    }

    private void checkWrongCard(String cardName) throws IOException {
        for(CardSimplified cardSimplified : pickedCards) {
            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {
                notifyError(new ErrorEvent("Card already picked by another player", playersManager.getCurrentPlayer().getID())); //4-06
                notifyRequest(new RequestEvent("Pick another card", playersManager.getCurrentPlayer().getID())); //1-07
                return;
            }
        }

        notifyError(new ErrorEvent("Invalid card", playersManager.getCurrentPlayer().getID())); //4-07
        notifyRequest(new RequestEvent("Pick another card", playersManager.getCurrentPlayer().getID()));  //1-07
    }

    private void pickCard(CardSimplified cardSimplified) throws IOException {
        CardSimplified chosenCardSimplified = cardSimplified;
        remainingCards.remove(chosenCardSimplified);
        pickedCards.add(chosenCardSimplified);
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(cardSimplified.getName()));
        notifyCard(new CardEvent(chosenCardSimplified, playersManager.getCurrentPlayer().getID()));
        ArrayList<CardSimplified> cardsSimplifiedCopy = new ArrayList<>(remainingCards);
        notifyDeck(new DeckEvent(new MiniDeckSimplified(cardsSimplifiedCopy)));
        playersManager.nextPlayer();
    }

    private void nextPhase() throws IOException {
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(remainingCards.get(0).getName()));
        notifyCard(new CardEvent(remainingCards.get(0), playersManager.getCurrentPlayer().getID()));
        playersManager.nextPlayer();
        notifyRequest(new RequestEvent("Position your first worker", playersManager.getCurrentPlayer().getID())); //1-08
        stateManager.setGameState(GameState.POSITIONING);
    }
}
