package it.polimi.ingsw.Model;

public class WinManager {

    public void win(Player player){
    }

    public void winCurrentPlayer(){
        win(PlayersManager.getPlayersManager().getCurrentPlayer());
    }

}
