package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class MiniDeckSimplified implements Serializable {
    private final ArrayList<CardSimplified> miniDeck;

    public MiniDeckSimplified(ArrayList<CardSimplified> miniDeck) {
        this.miniDeck = miniDeck;
    }

    public ArrayList<CardSimplified> getMiniDeck() {
        return miniDeck;
    }
}
