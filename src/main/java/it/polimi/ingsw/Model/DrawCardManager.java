package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.CardObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DrawCardManager extends CardObservable {
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    private ArrayList<CardSimplified> pickedCards;
    private ArrayList<CardSimplified> remainingCards;
    private final FirstPlayerManager firstPlayerManager;

    public DrawCardManager(StateManager stateManager, FirstPlayerManager firstPlayerManager) {
        this.stateManager = stateManager;
        this.firstPlayerManager = firstPlayerManager;
    }

    /**
     * This method is responsible of the initial phase of choosing cards by the first player
     *
     * @param playerID player's id
     * @param cards    list of cards
     * @throws IOException when socket closes
     */

    public void allCardsChosen(int playerID, ArrayList<String> cards) throws IOException {

        if (!stateManager.checkPlayerID(playerID))
            return;

        if (!stateManager.checkState(GameState.CHOOSING))
            return;

        if (cards.size() != playersManager.getPlayersNumber()) {
            notifyMessage(new MessageEvent(422, playersManager.getCurrentPlayer().getID()));
            return;
        }

        ArrayList<Card> allCards = new ArrayList<>();

        for (String c : cards) {
            Card card = Deck.getDeck().getCardByName(c);
            if (card == null || allCards.contains(card)) {
                notifyMessage(new MessageEvent(407, playersManager.getCurrentPlayer().getID()));
                return;
            }
            allCards.add(card);
        }

        for (Card c : allCards) Deck.getDeck().createAction(c);

        sendCards((ArrayList<CardSimplified>) allCards.stream().map(Card::simplify).collect(Collectors.toList()));
        for (Player p : playersManager.getNextPlayers()) notifyMessage(new MessageEvent(105, p.getID()));
    }

    /**
     * Associates the card received to the player
     *
     * @param playerID ID of the player
     * @param cardName name of the card to associate
     * @throws IOException when socket closes
     */
    public void pick(int playerID, String cardName) throws IOException {
        if (!stateManager.checkPlayerID(playerID))
            return;

        if (!stateManager.checkState(GameState.PICKING))
            return;

        if (cardName.equals("")) {
            notifyMessage(new MessageEvent(405, playersManager.getCurrentPlayer().getID()));
            return;
        }
        boolean rightCard = playerPicksTheCard(cardName);
        if (rightCard) {
            return;
        }

        checkWrongCard(cardName);
    }

    public void transition() throws IOException {
        int messageID = 0;
        if (playersManager.getPlayersNumber() == 3) messageID = 501;
        else if (playersManager.getPlayersNumber() == 2) messageID = 502;
        notifyMessage(new MessageEvent(messageID, PlayersManager.getPlayersManager().nextPlayer().getID()));
        for(Player p : playersManager.getNextPlayers()) {
            notifyMessage(new MessageEvent(120, p.getID()));
        }
        notifyFullDeck(new FullDeckEvent((ArrayList<CardSimplified>) Deck.getDeck().getCardsList().stream().map(Card::simplify).collect(Collectors.toList()), PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    /**
     * Method invoked by draw, sends the cards
     *
     * @param cardsSimplified the cardsSimplified from the deck
     * @throws IOException when socket closes
     */
    private void sendCards(ArrayList<CardSimplified> cardsSimplified) throws IOException {
        MiniDeckSimplified miniDeckSimplified = new MiniDeckSimplified(cardsSimplified);
        pickedCards = new ArrayList<>();
        remainingCards = miniDeckSimplified.getMiniDeck();
        stateManager.setGameState(GameState.PICKING);
        playersManager.nextPlayer();
        notifyDeck(new DeckEvent(miniDeckSimplified));
        notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
    }

    /**
     * Method invoked when a player picks a card, notifies the pick
     *
     * @param cardName name of the card picked
     * @return true if picked, false otherwise
     * @throws IOException when socket closes
     */
    private boolean playerPicksTheCard(String cardName) throws IOException {

        for (CardSimplified cardSimplified : remainingCards) {
            if (cardSimplified.getName().equalsIgnoreCase(cardName)) {

                pickCard(cardSimplified);

                if (remainingCards.size() != 1) {
                    ArrayList<CardSimplified> cardsSimplifiedCopy = new ArrayList<>(remainingCards);
                    for (Player p : playersManager.getNextPlayers()) notifyDeck(new DeckEvent(new MiniDeckSimplified(cardsSimplifiedCopy), p.getID()));
                    playersManager.nextPlayer();
                    for (Player p : playersManager.getNextPlayers()) notifyMessage(new MessageEvent(105, p.getID()));
                    notifyMessage(new MessageEvent(106, PlayersManager.getPlayersManager().getCurrentPlayer().getID()));
                } else nextPhase();
                return true;
            }
        }
        return false;
    }

    private void checkWrongCard(String cardName) throws IOException {
        for (CardSimplified cardSimplified : pickedCards) {
            if (cardSimplified.getName().equalsIgnoreCase(cardName)) {
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
     *
     * @param cardSimplified name of the card picked
     * @throws IOException when socket closes
     */
    private void pickCard(CardSimplified cardSimplified) throws IOException {
        remainingCards.remove(cardSimplified);
        pickedCards.add(cardSimplified);
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(cardSimplified.getName()));
        notifyPower(new PlayerChosenCardEvent(playersManager.getCurrentPlayer().getName(), cardSimplified.getName()));
    }

    /**
     * Invoked when a turn is ended, changes the player
     *
     * @throws IOException when socket closes
     */
    private void nextPhase() throws IOException {
        playersManager.nextPlayer();
        playersManager.getCurrentPlayer().setCard(Deck.getDeck().getCardByName(remainingCards.get(0).getName()));
        notifyPower(new PlayerChosenCardEvent(playersManager.getCurrentPlayer().getName(), remainingCards.get(0).getName()));
        stateManager.setGameState(GameState.FIRSTPLAYERSELECTION);
        firstPlayerManager.transition();
    }
}
