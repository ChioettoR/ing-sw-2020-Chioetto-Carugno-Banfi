package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.ColorSelectingEvent;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Events.Server.PlayerChosenColorEvent;
import it.polimi.ingsw.Events.Server.ServerEvent;
import it.polimi.ingsw.Observer.ServerObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorPoolManagerTest implements ServerObserver {

    final Grid grid = Grid.getGrid();
    final PlayersManager playersManager = PlayersManager.getPlayersManager();
    final Deck deck = Deck.getDeck();
    final Player player = new Player("Alberto");
    final Player player1 = new Player("Marcello");
    final Player player2 = new Player("Fabrizio");
    Card card;
    Card card1;
    Card card2;
    final CardsBuilder cardsBuilder = new CardsBuilder();
    final StateManager stateManager = new StateManager();
    final ColorPoolManager colorPoolManager = new ColorPoolManager(stateManager);
    int updateCounter;
    PlayerColor color;
    PlayerColor color1;
    PlayerColor color2;

    @BeforeEach
    void setUp() {
        new Builder().build();
        cardsBuilder.createCards();
        card = deck.getCardByName("Apollo");
        card1 = deck.getCardByName("Artemis");
        card2 = deck.getCardByName("Athena");
        cardsBuilder.createAction(card);
        cardsBuilder.createAction(card1);
        playersManager.addPlayer(player);
        player.setCard(card);
        playersManager.addPlayer(player1);
        player1.setCard(card1);
        playersManager.addPlayer(player2);
        player1.setCard(card2);
        playersManager.setNextPlayerIndex(0);
        colorPoolManager.addObserver(this);
        stateManager.addObserver(this);
        stateManager.setGameState(GameState.COLORSELECTING);
    }

    @Test
    void colorPoolTest() throws IOException {
        assertEquals(0, playersManager.getCurrentPlayer().getID());
        colorPoolManager.transition();
    }

    @AfterEach
    void tearDown() {
        playersManager.reset();
        deck.reset();
        grid.reset();
    }

    @Override
    public void update(ServerEvent serverEvent) throws IOException {

        if(updateCounter==0) {
            assertTrue(serverEvent instanceof MessageEvent);
            if(((MessageEvent) serverEvent).getMessageID()==119) updateCounter++;
        }

        else if(updateCounter==1) {
            assertTrue(serverEvent instanceof ColorSelectingEvent);
            assertEquals(0, serverEvent.getPlayerID());
            int colorsNumber = playersManager.getPlayersNumber();
            assertEquals(colorsNumber, ((ColorSelectingEvent) serverEvent).getColorsAvailable().size());
            color = ((ColorSelectingEvent) serverEvent).getColorsAvailable().get(0);
            updateCounter++;
            colorPoolManager.colorSelection(0, color);
        }

        else if(updateCounter==2) {
            assertTrue(serverEvent instanceof PlayerChosenColorEvent);
            assertEquals(player.getName(), ((PlayerChosenColorEvent) serverEvent).getName());
            assertEquals(color, ((PlayerChosenColorEvent) serverEvent).getPlayerColor());
            updateCounter++;
        }

        else if(updateCounter==3) {
            assertTrue(serverEvent instanceof MessageEvent);
            assertEquals(player1.getID(), playersManager.getCurrentPlayer().getID());
            if(((MessageEvent) serverEvent).getMessageID()==119) updateCounter++;
        }

        else if(updateCounter==4) {
            assertTrue(serverEvent instanceof  ColorSelectingEvent);
            int colorsNumber = playersManager.getPlayersNumber() - 1;
            assertEquals(colorsNumber, ((ColorSelectingEvent) serverEvent).getColorsAvailable().size());
            color1 = ((ColorSelectingEvent) serverEvent).getColorsAvailable().get(0);
            color2 = ((ColorSelectingEvent) serverEvent).getColorsAvailable().get(1);
            updateCounter++;
            colorPoolManager.colorSelection(1, color1);
        }

        else if(updateCounter==5) {
            assertTrue(serverEvent instanceof PlayerChosenColorEvent);
            assertEquals(player1.getName(), ((PlayerChosenColorEvent) serverEvent).getName());
            assertEquals(color1, ((PlayerChosenColorEvent) serverEvent).getPlayerColor());
            updateCounter++;
        }

        else if(updateCounter==6) {
            assertTrue(serverEvent instanceof PlayerChosenColorEvent);
            assertEquals(player2.getName(), ((PlayerChosenColorEvent) serverEvent).getName());
            assertEquals(color2, ((PlayerChosenColorEvent) serverEvent).getPlayerColor());
            updateCounter++;
        }
    }
}
