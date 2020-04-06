package it.polimi.ingsw.Model;

public class Builder {

    //TODO: chiamato dal controller all'inizio
    /**
     * Builds the grid and all cards
     */
    public void build(){
        Grid.getGrid().createGrid(5,5);
        new CardsBuilder().createCards();
    }
}
