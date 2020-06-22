package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Model.PlayerColor;

import java.util.HashMap;
import java.util.TreeMap;

public class CLIColorDecoder {

    private final HashMap<PlayerColor, String> colorNames = new HashMap<>();
    private final TreeMap<String, PlayerColor> colorNamesReverse = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final HashMap<PlayerColor, Color> colorANSI = new HashMap<>();

    /**
     * Sets the color of the players in game
     */
    public CLIColorDecoder() {
        colorNames.put(PlayerColor.P1_COLOR, "Magenta");
        colorNames.put(PlayerColor.P2_COLOR, "Yellow");
        colorNames.put(PlayerColor.P3_COLOR, "Cyan");
        colorNamesReverse.put("Magenta", PlayerColor.P1_COLOR);
        colorNamesReverse.put("Yellow", PlayerColor.P2_COLOR);
        colorNamesReverse.put("Cyan", PlayerColor.P3_COLOR);
        colorANSI.put(PlayerColor.P1_COLOR, Color.ANSI_MAGENTA);
        colorANSI.put(PlayerColor.P2_COLOR, Color.ANSI_YELLOW);
        colorANSI.put(PlayerColor.P3_COLOR, Color.ANSI_CYAN);
    }

    public Color getColor(PlayerColor playerColor) {
        return colorANSI.get(playerColor);
    }

    public String getColorName(PlayerColor playerColor) {
        return colorNames.get(playerColor);
    }

    public PlayerColor getPlayerColor(String colorName) {
        return colorNamesReverse.get(colorName);
    }
}
