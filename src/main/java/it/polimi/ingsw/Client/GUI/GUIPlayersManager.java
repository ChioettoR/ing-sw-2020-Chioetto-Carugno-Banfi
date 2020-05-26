package it.polimi.ingsw.Client.GUI;

import java.util.ArrayList;

public class GUIPlayersManager {

    ArrayList<GUIPlayer> guiPlayers = new ArrayList<>();
    ArrayList<GUIColorPool> guiColorPool;

    public GUIPlayersManager() {
        guiColorPool = new ArrayList<>();
        guiColorPool.add(GUIColorPool.SILVER);
        guiColorPool.add(GUIColorPool.DARKBROWN);
        guiColorPool.add(GUIColorPool.LIGHTBROWN);
    }

    public void addPlayer(String name) {

        GUIColorPool colorEnum = guiColorPool.get(guiPlayers.size());
        javafx.scene.paint.Color color;

        switch (colorEnum) {
            case SILVER: {
                color = javafx.scene.paint.Color.web("8a9f9f");
                break;
            }
            case DARKBROWN: {
                color = javafx.scene.paint.Color.web("cfb39c");
                break;
            }
            case LIGHTBROWN: {
                color = javafx.scene.paint.Color.web("7c5536");
                break;
            }
            default: {
                color = javafx.scene.paint.Color.BLACK;
                break;
            }
        }
        guiPlayers.add(new GUIPlayer(name, color));
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for(GUIPlayer p : guiPlayers) names.add(p.getName());
        return names;
    }

    public ArrayList<javafx.scene.paint.Color> getColors() {
        ArrayList<javafx.scene.paint.Color> colors = new ArrayList<>();
        for(GUIPlayer p : guiPlayers) colors.add(p.getColor());
        return colors;
    }

    public GUIPlayer getPlayer(String name) {
        for(GUIPlayer p : guiPlayers) if(p.getName().equals(name)) return p;
        return null;
    }

    public javafx.scene.paint.Color color(String name) {
        for(GUIPlayer guiPlayer : guiPlayers) if(guiPlayer.getName().equals(name)) return guiPlayer.getColor();
        return null;
    }
}
