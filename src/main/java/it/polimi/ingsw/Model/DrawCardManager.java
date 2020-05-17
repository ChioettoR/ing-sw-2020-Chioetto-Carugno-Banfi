package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.CardEvent;
import it.polimi.ingsw.Events.Server.DeckEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Observer.Server.CardObservable;

import java.io.IOException;
import java.util.ArrayList;

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
            notifyMessage(new MessageEvent(405, playersManager.getCurrentPlayer().getID()));
            return;
        }
        boolean rightCard = playerPicksTheCard(cardName);
        if(rightCard){
            return;
        }

        checkWrongCard(cardName);
    }

    public void transition() throws IOException {
        notifyMessage(new MessageEvent(305, -1));
        notifyMessage(new MessageEvent(105, PlayersManager.getPlayersManager().nextPlayer().getID()));
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
        notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        stateManager.setGameState(GameState.PICKING);
    }

    private boolean playerPicksTheCard(String cardName) throws IOException {
        for(CardSimplified cardSimplified : remainingCards) {

            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {
                pickCard(cardSimplified);
                if(remainingCards.size()!=1) {
                    ArrayList<CardSimplified> cardsSimplifiedCopy = new ArrayList<>(remainingCards);
                    for(Player p : playersManager.getNextPlayers()) notifyDeck(new DeckEvent(new MiniDeckSimplified(cardsSimplifiedCopy), p.getID()));
                    playersManager.nextPlayer();
                    notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
                }
                else {
                    playersManager.nextPlayer();
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
                notifyMessage(new MessageEvent(406, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
                notifyMessage(new MessageEvent(107, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
                return;
            }
        }
        notifyMessage(new MessageEvent(407, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        notifyMessage(new MessageEvent(107, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    private void pickCard(CardSimplified cardSimplified) throws IOException {
        remainingCards.remove(cardSimplified);
        pickedCards.add(cardSimplified);
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(cardSimplified.getName()));
        notifyCard(new CardEvent(cardSimplified, playersManager.getCurrentPlayer().getID()));
    }

    private void nextPhase() throws IOException {
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(remainingCards.get(0).getName()));
        notifyCard(new CardEvent(remainingCards.get(0), playersManager.getCurrentPlayer().getID()));
        playersManager.nextPlayer();
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        stateManager.setGameState(GameState.POSITIONING);
    }
}
