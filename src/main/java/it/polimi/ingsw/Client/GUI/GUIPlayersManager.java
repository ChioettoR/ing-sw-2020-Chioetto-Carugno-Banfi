package it.polimi.ingsw.Client.GUI;

import java.util.ArrayList;

public class GUIPlayersManager {

    final ArrayList<GUIPlayer> guiPlayers = new ArrayList<>();

    public void addPlayer(String name) {
        guiPlayers.add(new GUIPlayer(name));
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for(GUIPlayer p : guiPlayers) names.add(p.getName());
        return names;
    }

    public GUIPlayer getPlayer(String name) {
        for(GUIPlayer p : guiPlayers) if(p.getName().equals(name)) return p;
        return null;
    }
}
