package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cardsList = new ArrayList<>();
    private static Deck deck;
    private Card completeTowersObserver;
    private final CardsBuilder cardsBuilder = new CardsBuilder();

    private Deck() {
        deck = this;
    }

    /**
     * Resets all the deck
     */
    public void reset() {
        cardsList.clear();
        completeTowersObserver = null;
    }

    /**
     * Calls the cardsBuilder method for the creation of the cards
     */
    public void createCards() {
        ArrayList<Card> cardArrayList = cardsBuilder.createCards();
        for(Card card : cardArrayList)
            addCard(card);
    }

    public Card getCompleteTowersObserver() {
        return completeTowersObserver;
    }

    /**
     * @return The instance of Deck
     */
    public static Deck getDeck(){
        if(deck==null)
            deck = new Deck();
        return deck;
    }

    /** Picks a random card from the deck. The selected card cannot be picked again in the future
     * @return Random card from the deck
     */
    public Card pickCard() {
        if(cardsList.size() == 0){
            return null;
        }

        ArrayList<Card> cardsListCopy = new ArrayList<>(cardsList);
        int randomIndex = (int) (Math.random() * cardsList.size());
        Card selectedCard = cardsListCopy.get(randomIndex);

        while(selectedCard.getAlreadyPicked()) {
            cardsListCopy.remove(randomIndex);
            if(cardsListCopy.size()<=0)
                break;
            randomIndex = (int) (Math.random() * cardsListCopy.size());
            selectedCard = cardsListCopy.get(randomIndex);
        }

        if(cardsListCopy.size()<=0) {
            System.out.println("The deck is empty");
            return null;
        }

        selectedCard.setAlreadyPicked(true);

        if(selectedCard.getGodPower()!=null)
            cardsBuilder.createAction(selectedCard);

        return selectedCard;
    }

    public void createAction(Card card) {
        cardsBuilder.createAction(card);
    }

    /**
     * Deletes all cards from the deck
     */
    public void deleteAllCards() {
        ArrayList<Card> cardsListCopy = new ArrayList<>(cardsList);
        for(Card card : cardsListCopy)
            cardsList.remove(card);
    }

    /**
     *
     * @param name The name of the card to search
     * @return The card with the given name
     */
    public Card getCardByName (String name){

        if(name==null) {
            System.out.println("The name is null");
            return null;
        }

        if(cardsList.size() == 0){
            System.out.println("The deck is empty");
            return null;
        }

        for (Card card : cardsList) {
            if (card.getName().equalsIgnoreCase(name))
                return card;
        }
        System.out.println("The deck doesn't contain the requested card");
        return null;
    }

    public ArrayList<Card> getCardsList() {
        return cardsList;
    }

    /**
     * Adds a card to the deck
     * @param card The card you want to add to the deck
     */
    public void addCard(Card card) {
        if(card == null) {
            System.out.println("The card is null");
        }

        else if (cardsList.contains(card)) {
            System.out.println("The card is already in the deck");
        }

        else {
            if(card.isCompleteTowersObserver())
                if(completeTowersObserver == null)
                    completeTowersObserver = card;
                else
                    System.out.println("There is already a complete towers observer");
            cardsList.add(card);
        }
    }
}
