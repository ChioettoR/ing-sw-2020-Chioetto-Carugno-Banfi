package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;

import java.util.ArrayList;

public class CLIPlayersManager {

    ArrayList<CLIPlayer> cliPlayers = new ArrayList<>();
    ArrayList<ColorPool> colorPool;

    public CLIPlayersManager() {
        colorPool = new ArrayList<>();
        colorPool.add(ColorPool.YELLOW);
        colorPool.add(ColorPool.MAGENTA);
        colorPool.add(ColorPool.CYAN);
    }

    public void addPlayer(String name) {

        ColorPool colorEnum = colorPool.get(cliPlayers.size());
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

    public Color color(String name) {
        for(CLIPlayer cliPlayer : cliPlayers) {
            if(cliPlayer.getName().equals(name))
                return cliPlayer.getColor();
        }
        return null;
    }
}
