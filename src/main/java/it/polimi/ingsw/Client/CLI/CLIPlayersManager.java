package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;

public class CLIPlayersManager {

    ArrayList<CLIPlayer> cliPlayers = new ArrayList<>();
    ArrayList<CLIColorPool> cliColorPool;

    public CLIPlayersManager() {
        cliColorPool = new ArrayList<>();
        cliColorPool.add(CLIColorPool.YELLOW);
        cliColorPool.add(CLIColorPool.MAGENTA);
        cliColorPool.add(CLIColorPool.CYAN);
    }

    public void addPlayer(String name) {

        CLIColorPool colorEnum = cliColorPool.get(cliPlayers.size());
        Color color;

        switch (colorEnum) {
            case YELLOW: {
                color = Color.ANSI_YELLOW;
                break;
            }
            case MAGENTA: {
                color = Color.ANSI_MAGENTA;
                break;
            }
            case CYAN: {
                color = Color.ANSI_CYAN;
                break;
            }
            default: {
                color = Color.ANSI_BLACK;
                break;
            }
        }

        cliPlayers.add(new CLIPlayer(name, color));
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

    public Color color(String name) {
        for(CLIPlayer cliPlayer : cliPlayers)
            if(cliPlayer.getName().equals(name)) return cliPlayer.getColor();
        return null;
    }
}
