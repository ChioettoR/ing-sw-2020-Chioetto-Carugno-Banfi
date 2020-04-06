package it.polimi.ingsw.Model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {

    Deck deck = Deck.getDeck();
    Card card = new Card("Marcello");
    Card card1 = new Card("Alberto");
    Card card2 = new Card("Federico");
    ArrayList<String> names = new ArrayList<String>();

    @Test
    void addAndRandomPickTest() {
        addAndRandomPick();
    }

    @AfterEach
    void tearDown() {
        deck.deleteAllCards();
    }

    void addAndRandomPick() {
        getCardByNameFromEmptyDeck();
        addCards();
        pickCards();
        getCardByName();
    }

    /**
     * Adds cards to the deck
     */
    public void addCards() {
        System.out.println("TEST: I'm adding cards");
        deck.addCard(card);
        assertEquals(deck.getCardsList().size(), 1);

        deck.addCard(card1);
        assertEquals(deck.getCardsList().size(), 2);

        deck.addCard(card2);
        assertEquals(deck.getCardsList().size(), 3);

        deck.addCard(card1);
        assertEquals(deck.getCardsList().size(), 3);

        deck.addCard(null);

        names.add(card.getName());
        names.add(card1.getName());
        names.add(card2.getName());
    }

    /**
     * Picks Cards from the deck, asserts the changing of alreadyPicked and if the card is null when the deck is empty
     */
    public void pickCards() {
        ArrayList<String> namesCopy = new ArrayList<String>(names);

        System.out.println("TEST: I'm picking cards");

        //Iterates over the deck size
        for (int i = 0; i < deck.getCardsList().size() + 2; i++) {
            Card pickedCard = deck.pickCard();

            if (i >= deck.getCardsList().size())
                assertNull(pickedCard);

            else {
                assert (namesCopy.contains(pickedCard.getName()));
                namesCopy.remove(pickedCard.getName());
                assertTrue(pickedCard.getAlreadyPicked());
            }
        }
    }

    /**
     * Gets a card from the empty deck using its name
     */
    public void getCardByNameFromEmptyDeck() {
        System.out.println("TEST: I'm getting cards using their names from the empty deck");
        Card selectedCard = deck.getCardByName("Carlo");
        assertNull(selectedCard);

        selectedCard = deck.pickCard();
        assertNull(selectedCard);

        selectedCard = deck.getCardByName("Marcello");
        assertNull(selectedCard);
    }

    /**
     * Gets a card from the deck using its name
     */
    public void getCardByName() {
        System.out.println("TEST: I'm getting card using their names from the deck");

        //I'm trying to get a card not in the deck
        Card selectedCard = deck.getCardByName("Carlo");
        assertNull(selectedCard);

        selectedCard = deck.getCardByName("Marcello");
        assertEquals(card, selectedCard);

        selectedCard = deck.getCardByName("Alberto");
        assertEquals(card1, selectedCard);

        selectedCard = deck.getCardByName("Federico");
        assertEquals(card2, selectedCard);

        selectedCard = deck.getCardByName(null);
        assertNull(selectedCard);

    }
}