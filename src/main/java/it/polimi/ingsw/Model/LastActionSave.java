package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class LastActionSave {
    public enum ActionType {MOVE, BUILD};
    private ActionType lastActionType;
    private ArrayList<Worker> workers;
    private ArrayList<Tile> tiles;
    private boolean saveAllMovements;
    private Worker currentWorker;
    private Tile currentTile;
    private Tile nextTile;

    Deck deck = Deck.getDeck();
    PlayersManager playersManager = PlayersManager.getPlayersManager();

    public void saveBeforeBuild(Tile nextTile) {
        lastActionType = ActionType.BUILD;
        this.nextTile = nextTile;
    }

    public void saveBeforeMove(Worker currentWorker, Tile nextTile) {

        lastActionType = ActionType.MOVE;
        Player currentPlayer = playersManager.getPlayerWithID(currentWorker.getPlayerID());
        Card card = currentPlayer.getCard();

        if(card.isSaveEverythingBeforeMove()) {
            saveAllMovements();
            return;
        }
        saveAllMovements = false;
        this.nextTile = nextTile;
        this.currentWorker = currentWorker;
        this.currentTile = currentWorker.getPosition();
    }

    public void undo() {
        if(lastActionType.equals(ActionType.MOVE))
            undoMovement();
        else if(lastActionType.equals(ActionType.BUILD))
            undoBuild();
        else
            System.out.println("Unknown action type");
    }

    private void saveAllMovements() {
        saveAllMovements = true;
        ArrayList<Player> players = playersManager.getPlayers();
        tiles = new ArrayList<Tile>();
        workers = new ArrayList<Worker>();
        for(Player p : players)
            for(Worker w : p.getWorkers()) {
                workers.add(w);
                tiles.add(w.getPosition());
            }
    }

    //TODO: NON IN QUESTA CLASSE! Per Athena bisogna settare il canMoveUp false solo dopo la fine del turno o del timer dell'undo
    private void undoMovement() {
        if(saveAllMovements) {
            for(int i=0; i<workers.size(); i++) {
                undoPosition(workers.get(i), tiles.get(i));
            }
        }
        else
            undoPosition(currentWorker, currentTile);
    }

    private void undoPosition(Worker worker, Tile position) {
        worker.getPosition().setEmpty(true);
        worker.setPosition(position);
        position.setWorker(worker);
    }

    private void undoBuild() {
        nextTile.removeLastLevel();
        Grid grid = Grid.getGrid();
    }
}
