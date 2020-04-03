package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cardsList;
    private static Deck deck;
    Card completeTowersObserver;
    //CardsBuilder cardsBuilder;

    private Deck() {
        cardsList = new ArrayList<Card>();
//        cardsList.addAll(cardsBuilder.createCards());
        deck = this;
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

    /** Pick a random card from a copy of the original deck
     * @return Random card from the deck
     */
    public Card pickCard() {
        if(cardsList.size() == 0){
            return null;
        }

        ArrayList<Card> cardsListCopy = new ArrayList<Card>(cardsList);
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
        return selectedCard;
    }

    public void deleteAllCards() {
        ArrayList<Card> cardsListCopy = new ArrayList<Card>(cardsList);
        for(Card card : cardsListCopy)
            cardsList.remove(card);
    }

    /**
     *
     * @param name of the card to search
     * @return selected card
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
            if (card.getName().compareTo(name) == 0)
                return card;
        }
        System.out.println("The deck doesn't contain the requested card");
        return null;
    }

    public ArrayList<Card> getCardsList() {
        return cardsList;
    }

    /**
     * Add a card to the deck
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
