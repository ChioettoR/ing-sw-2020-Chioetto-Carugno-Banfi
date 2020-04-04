package it.polimi.ingsw.Model;

public class WinManager {

    public void win(Player player){
        //System.out.println("Win " + player.getName());
    }

    public void winCurrentPlayer(){
        //System.out.println("Win " + PlayersManager.getPlayersManager().getCurrentPlayer().getName());
        win(PlayersManager.getPlayersManager().getCurrentPlayer());
    }

}
