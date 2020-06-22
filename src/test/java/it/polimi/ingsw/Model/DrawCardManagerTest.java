package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DrawCardManagerTest implements ServerObserver {
    private final Grid grid = Grid.getGrid();
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final Deck deck = Deck.getDeck();
    private final Player player = new Player("Alberto");
    private final Player player1 = new Player("Marcello");
    private final Player player2 = new Player("Franco");
    private final StateManager stateManager = new StateManager();
    private final ColorPoolManager colorPoolManager = new ColorPoolManager(stateManager);
    private final FirstPlayerManager firstPlayerManager = new FirstPlayerManager(stateManager, colorPoolManager);
    private final DrawCardManager drawCardManager = new DrawCardManager(stateManager, firstPlayerManager);
    private int updateCounter;
    private String cardName1;
    private String cardName2;
    private String cardName3;

    @BeforeEach
    void setUp() throws IOException {
        new Builder().build();
        playersManager.addPlayer(player);
        playersManager.addPlayer(player1);
        playersManager.addPlayer(player2);
        drawCardManager.addObserver(this);
        stateManager.addObserver(this);
        stateManager.setGameState(GameState.CHOOSING);
        drawCardTest();
    }

    @Test
    void drawCardTest() throws IOException {
        drawCardManager.transition();
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {

        if(serverEvent instanceof MessageEvent) {
            if(((MessageEvent) serverEvent).getMessageID()==106) updateCounter++;
            else return;
        }

        if(updateCounter==0) {
            assertTrue(serverEvent instanceof FullDeckEvent);
            assertEquals(0, serverEvent.getPlayerID());
            int cardsNumber = deck.getCardsList().size();
            assertEquals(cardsNumber, ((FullDeckEvent) serverEvent).getCards().size());
            int firstCard = 0;
            int secondCard = 3;
            int thirdCard = 5;
            cardName1 = ((FullDeckEvent) serverEvent).getCards().get(firstCard).getName();
            cardName2 = ((FullDeckEvent) serverEvent).getCards().get(secondCard).getName();
            cardName3 = ((FullDeckEvent) serverEvent).getCards().get(thirdCard).getName();
            ArrayList<String> cardsNames = new ArrayList<>();
            cardsNames.add(cardName1);
            cardsNames.add(cardName2);
            cardsNames.add(cardName3);
            updateCounter++;
            drawCardManager.allCardsChosen(0, cardsNames);
        }

        else if(updateCounter==1) {
            assertTrue(serverEvent instanceof DeckEvent);
            assertEquals(3, ((DeckEvent) serverEvent).getDeck().getMiniDeck().size());
            assertEquals(-1, serverEvent.getPlayerID());
            ArrayList<String> cardsExpected = new ArrayList<>();
            cardsExpected.add(cardName1);
            cardsExpected.add(cardName2);
            cardsExpected.add(cardName3);
            ArrayList<String> cardReceived = new ArrayList<>();
            for(CardSimplified s : ((DeckEvent) serverEvent).getDeck().getMiniDeck()) {
                cardReceived.add(s.getName());
            }
            assertEquals(cardsExpected.size(), cardReceived.size());
            assertTrue(cardsExpected.containsAll(cardReceived));
            updateCounter++;
            drawCardManager.pick(1, cardName1);
        }

        else if(updateCounter==2) {
            assertTrue(serverEvent instanceof PlayerChosenCardEvent);
            assertEquals(player1.getName(), ((PlayerChosenCardEvent) serverEvent).getPlayerName());
            assertEquals(cardName1, ((PlayerChosenCardEvent) serverEvent).getCardName());
            updateCounter++;
        }

        else if(updateCounter==3) {
            assertTrue(serverEvent instanceof  DeckEvent);
            assertEquals(2, ((DeckEvent) serverEvent).getDeck().getMiniDeck().size());
            assertEquals(0, serverEvent.getPlayerID());
            ArrayList<String> cardsExpected = new ArrayList<>();
            cardsExpected.add(cardName2);
            cardsExpected.add(cardName3);
            ArrayList<String> cardReceived = new ArrayList<>();
            for(CardSimplified s : ((DeckEvent) serverEvent).getDeck().getMiniDeck()) {
                cardReceived.add(s.getName());
            }
            assertEquals(cardsExpected.size(), cardReceived.size());
            assertTrue(cardsExpected.containsAll(cardReceived));
            updateCounter++;
        }

        else if(updateCounter==4) {
            assertTrue(serverEvent instanceof  DeckEvent);
            assertEquals(2, ((DeckEvent) serverEvent).getDeck().getMiniDeck().size());
            assertEquals(2, serverEvent.getPlayerID());
            ArrayList<String> cardsExpected = new ArrayList<>();
            cardsExpected.add(cardName2);
            cardsExpected.add(cardName3);
            ArrayList<String> cardReceived = new ArrayList<>();
            for(CardSimplified s : ((DeckEvent) serverEvent).getDeck().getMiniDeck()) {
                cardReceived.add(s.getName());
            }
            assertEquals(cardsExpected.size(), cardReceived.size());
            assertTrue(cardsExpected.containsAll(cardReceived));
        }

        else if(updateCounter==5) {
            updateCounter++;
            drawCardManager.pick(2, cardName2);
        }

        else if(updateCounter==6) {
            assertTrue(serverEvent instanceof  PlayerChosenCardEvent);
            assertEquals(player2.getName(), ((PlayerChosenCardEvent) serverEvent).getPlayerName());
            assertEquals(cardName2, ((PlayerChosenCardEvent) serverEvent).getCardName());
            updateCounter++;
        }

        else if(updateCounter==7) {
            assertTrue(serverEvent instanceof PlayerChosenCardEvent);
            assertEquals(player.getName(), ((PlayerChosenCardEvent) serverEvent).getPlayerName());
            assertEquals(cardName3, ((PlayerChosenCardEvent) serverEvent).getCardName());
            updateCounter++;
        }
    }
}
