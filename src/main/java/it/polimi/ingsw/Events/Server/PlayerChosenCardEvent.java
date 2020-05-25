package it.polimi.ingsw.Events.Server;

import java.io.Serializable;

public class PlayerChosenCardEvent extends ServerEvent implements Serializable {
    private final String playerName;
    private final String cardName;

    /**
     * Event triggers when a card is chosen by a player
     * @param playerName name of the player
     * @param cardName card name
     */
    public PlayerChosenCardEvent(String playerName, String cardName) {
        this.playerName = playerName;
        this.cardName = cardName;
        super.playerID = -1;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCardName() {
        return cardName;
    }
}
