package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Model.PlayerColor;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.TreeMap;

public class GUIColorDecoder {

    HashMap<PlayerColor, String> colorNames = new HashMap<>();
    TreeMap<String, PlayerColor> colorNamesReverse = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    HashMap<PlayerColor, javafx.scene.paint.Color> colorANSI = new HashMap<>();

    public GUIColorDecoder() {
        colorNames.put(PlayerColor.P1_COLOR, "Silver");
        colorNames.put(PlayerColor.P2_COLOR, "Light-brown");
        colorNames.put(PlayerColor.P3_COLOR, "Dark-brown");
        colorNamesReverse.put("Magenta", PlayerColor.P1_COLOR);
        colorNamesReverse.put("Yellow", PlayerColor.P2_COLOR);
        colorNamesReverse.put("Cyan", PlayerColor.P3_COLOR);
        colorANSI.put(PlayerColor.P1_COLOR, javafx.scene.paint.Color.web("8a9f9f"));
        colorANSI.put(PlayerColor.P2_COLOR, javafx.scene.paint.Color.web("cfb39c"));
        colorANSI.put(PlayerColor.P3_COLOR, javafx.scene.paint.Color.web("7c5536"));
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
