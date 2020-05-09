package it.polimi.ingsw.Model;

public class Builder {

    public void build(){
        Grid.getGrid().reset();
        PlayersManager.getPlayersManager().reset();
        Deck.getDeck().reset();
        Deck.getDeck().createCards();
        Grid.getGrid().createGrid(5,5);
    }
}
