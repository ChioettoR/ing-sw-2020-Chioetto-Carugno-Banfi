package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;

public class CLIPlayersManager {

    private final ArrayList<CLIPlayer> cliPlayers = new ArrayList<>();

    /**
     * Adds the player and colors it
     * @param name name of the player
     */
    public void addPlayer(String name) {
        cliPlayers.add(new CLIPlayer(name));
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for(CLIPlayer p : cliPlayers)
            names.add(p.getName());
        return names;
    }

    public ArrayList<String> getDisplayStrings() {
        ArrayList<String> displayStrings = new ArrayList<>();
        for(CLIPlayer p : cliPlayers) {
            String name = p.getName();
            String card = p.getCardName();
            String displayString;
            if(card!=null) displayString = name + " ──> " + card;
            else displayString = name;
            displayStrings.add(displayString);
        }
        return displayStrings;
    }

    public ArrayList<Color> getColors() {
        ArrayList<Color> colors = new ArrayList<>();
        for(CLIPlayer p : cliPlayers) colors.add(p.getColor());
        return colors;
    }

    public CLIPlayer getPlayer(String name) {
        for(CLIPlayer p : cliPlayers)
            if(p.getName().equals(name)) return p;
        return null;
    }

    /**
     * Colors the string received
     * @param name name to color
     * @return returns the string colored
     */
    public Color color(String name) {
        for(CLIPlayer cliPlayer : cliPlayers)
            if(cliPlayer.getName().equals(name)) return cliPlayer.getColor();
        return null;
    }

    /**
     * Removed the player from the match
     * @param name name of the player
     */
    public void deleteName(String name) {
        cliPlayers.removeIf(p -> p.getName().equals(name));
    }
}
