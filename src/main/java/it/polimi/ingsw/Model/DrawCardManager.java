package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.CardEvent;
import it.polimi.ingsw.Events.Server.DeckEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenCardEvent;
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

    /**
     * Draws from the deck a defined number of cards, one card per player, and shows them
     * @param playerID parameter used to check if the player is the one who has to draw the card in that moment
     * @throws IOException when socket closes
     */
    public void draw(int playerID) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.DRAWING))
            return;

        ArrayList<CardSimplified> cardsSimplified = pickCardsFromDeck();
        sendCards(cardsSimplified);
        for(Player p : playersManager.getNextPlayers())
            notifyMessage(new MessageEvent(105, p.getID()));
    }

    /**
     * Associates the card received to the player
     * @param playerID ID of the player
     * @param cardName name of the card to associate
     * @throws IOException when socket closes
     */
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
        notifyMessage(new MessageEvent(501, PlayersManager.getPlayersManager().nextPlayer().getID()));
    }

    /**
     * Method invoked by draw, picks the cards from the deck and returns them
     * @return the cardsSimplified from the deck
     */
    private ArrayList<CardSimplified> pickCardsFromDeck() {
        ArrayList<CardSimplified> cardsSimplified = new ArrayList<>();
        for (int i=0; i<playersManager.getPlayersNumber(); i++)
            cardsSimplified.add(Deck.getDeck().pickCard().simplify());
        return cardsSimplified;
    }

    /**
     * Method invoked by draw, sends the cards
     * @param cardsSimplified the cardsSimplified from the deck
     * @throws IOException when socket closes
     */
    private void sendCards(ArrayList<CardSimplified> cardsSimplified) throws IOException {
        MiniDeckSimplified miniDeckSimplified = new MiniDeckSimplified(cardsSimplified);
        pickedCards = new ArrayList<>();
        remainingCards = miniDeckSimplified.getMiniDeck();
        notifyDeck(new DeckEvent(miniDeckSimplified));
        playersManager.nextPlayer();
        notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        stateManager.setGameState(GameState.PICKING);
    }

    /**
     *Method invoked when a player picks a card, notifies the pick
     * @param cardName name of the card picked
     * @return true if picked, false otherwise
     * @throws IOException when socket closes
     */
    private boolean playerPicksTheCard(String cardName) throws IOException {

        for(CardSimplified cardSimplified : remainingCards) {
            if(cardSimplified.getName().equalsIgnoreCase(cardName)) {

                pickCard(cardSimplified);

                if(remainingCards.size()!=1) {
                    ArrayList<CardSimplified> cardsSimplifiedCopy = new ArrayList<>(remainingCards);
                    for(Player p : playersManager.getNextPlayers()) notifyDeck(new DeckEvent(new MiniDeckSimplified(cardsSimplifiedCopy), p.getID()));
                    playersManager.nextPlayer();
                    for(Player p : playersManager.getNextPlayers())
                        notifyMessage(new MessageEvent(105, p.getID()));
                    notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
                }
                else nextPhase();
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

    /**
     * Invoked after a card pick, removes the card from the remaining ones and adds it to the picked ones
     * @param cardSimplified name of the card picked
     * @throws IOException when socket closes
     */
    private void pickCard(CardSimplified cardSimplified) throws IOException {
        remainingCards.remove(cardSimplified);
        pickedCards.add(cardSimplified);
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(cardSimplified.getName()));
        notifyPower(new PlayerChosenCardEvent(playersManager.getCurrentPlayer().getName(), cardSimplified.getName()));
        notifyCard(new CardEvent(cardSimplified, playersManager.getCurrentPlayer().getID()));
    }

    /**
     * Invoked when a turn is ended, changes the player
     * @throws IOException when socket closes
     */
    private void nextPhase() throws IOException {
        playersManager.nextPlayer();
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(remainingCards.get(0).getName()));
        notifyPower(new PlayerChosenCardEvent(playersManager.getCurrentPlayer().getName(), remainingCards.get(0).getName()));
        notifyCard(new CardEvent(remainingCards.get(0), playersManager.getCurrentPlayer().getID()));
        playersManager.nextPlayer();
        notifyMessage(new MessageEvent(108, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) notifyMessage(new MessageEvent(114, p.getID()));
        stateManager.setGameState(GameState.POSITIONING);
    }
}
