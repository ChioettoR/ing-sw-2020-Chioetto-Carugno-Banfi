package it.polimi.ingsw.Model;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.Server.ActionObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ActionManager extends ActionObservable{
    PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    private AvailableActions availableActions;
    private int index;
    private ActionType lastAction;
    private final ArrayList<Worker> savedWorkers = new ArrayList<>();
    private final ArrayList<Tile> savedTiles = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> savedLevels = new ArrayList<>();

    private class AvailableActions {
        MoveAction moveAction;
        int moveActionIndex;
        BuildAction buildAction;
        int buildActionIndex;
        private final ArrayList<ActionType> availableActionsNames = new ArrayList<>();

        public AvailableActions() {
            this.moveActionIndex = -1;
            this.buildActionIndex = -1;
        }

        public int getMoveActionIndex() {
            return moveActionIndex;
        }

        public int getBuildActionIndex() {
            return buildActionIndex;
        }

        public ArrayList<ActionType> getAvailableActionsNames() {
            return availableActionsNames;
        }

        public MoveAction getMoveAction() {
            return moveAction;
        }

        public void addAvailableActionName(ActionType availableActionName) {
            availableActionsNames.add(availableActionName);
        }

        public void addAvailableAction(UserAction availableAction) {

            if(availableAction instanceof MoveAction) {
                moveAction = (MoveAction) availableAction;
                moveActionIndex = index;
                availableActionsNames.add(ActionType.MOVE);
            }

            else if(availableAction instanceof BuildAction) {
                buildAction = (BuildAction) availableAction;
                buildActionIndex = index;
                availableActionsNames.add(ActionType.BUILD);
            }
        }

        public BuildAction getBuildAction() {
            return buildAction;
        }
    }

    public ActionManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void move(int playerID, int x, int y) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTING))
            return;

        if(!availableActions.getAvailableActionsNames().contains(ActionType.MOVE)) {
            notifyError(new ErrorEvent("Invalid action!", playersManager.getCurrentPlayer().getID()));
            return;
        }

        MoveAction moveAction = availableActions.getMoveAction();
        Worker worker = playersManager.getCurrentWorker();
        Tile tile = Grid.getGrid().getTile(x, y);

        if(!moveAction.canMove(worker, tile))
            notifyError(new ErrorEvent("You can't move in this tile", playersManager.getCurrentPlayer().getID()));

        else {
            ArrayList<Tile> oldGrid = saveOldGrid();
            moveAction.move(worker, tile);
            //TODO: rendere disponibile UNDO
            lastAction = ActionType.MOVE;
            sendChange(oldGrid);
            if(checkWin())
                return;
            index = availableActions.getMoveActionIndex() + 1;
            stateManager.setGameState(GameState.ACTIONSELECTING);
            sendActions();
        }
    }

    public void build(int playerID, int x, int y, int buildLevel) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTING))
            return;

        BuildAction buildAction = availableActions.getBuildAction();
        Worker worker = playersManager.getCurrentWorker();
        Tile tile = Grid.getGrid().getTile(x, y);

        if(buildLevel==-1) {
            if(!buildAction.canBuild(worker, tile))
                notifyError(new ErrorEvent("You can't build in this tile", playersManager.getCurrentPlayer().getID()));
            else {
                ArrayList<Tile> oldGrid = saveOldGrid();
                buildAction.build(worker, tile);
                lastAction = ActionType.BUILD;
                sendChange(oldGrid);
                if(checkWin())
                    return;
                index = availableActions.getBuildActionIndex() + 1;
                stateManager.setGameState(GameState.ACTIONSELECTING);
                sendActions();
            }
        }

        else {
            if(!buildAction.canBuild(worker, tile, buildLevel))
                notifyError(new ErrorEvent("You can't build in this tile", playersManager.getCurrentPlayer().getID()));
            else {
                ArrayList<Tile> oldGrid = saveOldGrid();
                buildAction.build(worker, tile, buildLevel);
                //TODO: rendere disponibile UNDO
                lastAction = ActionType.BUILD;
                sendChange(oldGrid);
                if(checkWin())
                    return;
                index = availableActions.getBuildActionIndex() + 1;
                stateManager.setGameState(GameState.ACTIONSELECTING);
                sendActions();
            }
        }
    }

    public void actionSelect(int playerID, String action) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTIONSELECTING))
            return;

        if(!availableActions.getAvailableActionsNames().contains(ActionType.valueOf(action))) {
            notifyError(new ErrorEvent("Invalid action!", playersManager.getCurrentPlayer().getID()));
            return;
        }

        switch (ActionType.valueOf(action)) {
            case MOVE: {
                ArrayList<Tile> availableTiles = availableActions.getMoveAction().getAvailableTilesForAction(playersManager.getCurrentWorker());
                stateManager.setGameState(GameState.ACTING);
                notify(new AvailableTilesEvent((ArrayList<TileSimplified>)availableTiles.stream().map(Tile::simplify).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                notifyRequest(new RequestEvent("Where do you want to move?", playersManager.getCurrentPlayer().getID()));
                break;
            }
            case BUILD: {
                ArrayList<Tile> availableTiles = availableActions.getBuildAction().getAvailableTilesForAction(playersManager.getCurrentWorker());
                stateManager.setGameState(GameState.ACTING);
                notify(new AvailableTilesEvent((ArrayList<TileSimplified>)availableTiles.stream().map(Tile::simplify).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                notifyRequest(new RequestEvent("Where do you want to build?", playersManager.getCurrentPlayer().getID()));
                break;
            }
            case ENDROUND: {
                //playersManager.nextPlayerAndStartRound();
                //stateManager.setGameState(GameState.SELECTING);
                //index = 0;
                index++;
                sendActions();
                //notifyRequest(new RequestEvent("Choose your worker", playersManager.getCurrentPlayer().getID()));
                break;
            }
            case UNDO: {
                int moveIndex = availableActions.getMoveActionIndex();
                int buildIndex = availableActions.getBuildActionIndex();
                if(moveIndex==-1) index = buildIndex;
                else if(buildIndex==-1) index = moveIndex;
                else index = Math.min(moveIndex, buildIndex);
                ArrayList<Tile> oldGrid = Grid.getGrid().getTiles();
                if(lastAction==ActionType.MOVE)
                    availableActions.getMoveAction().undo();
                else if(lastAction==ActionType.BUILD)
                    availableActions.getBuildAction().undo();
                sendChange(oldGrid);
                stateManager.setGameState(GameState.ACTIONSELECTING);
                break;
            }
        }
    }

    private void sendActions() throws IOException {
        availableActions = new AvailableActions();
        ArrayList<Action> actionList = playersManager.getActionOrder();
        if(actionList.size()==0) {
            System.out.println("Size = 0");
            return;
        }

        if((actionList.size()<=index)) {
            playersManager.nextPlayerAndStartRound();
            index = 0;
            stateManager.setGameState(GameState.SELECTING);
            notifyRequest(new RequestEvent("Choose your worker", playersManager.getCurrentPlayer().getID()));
            return;
        }

        Action currentAction = actionList.get(index);

        if(currentAction instanceof RoundAction) {
            ((RoundAction) currentAction).doAction();
            index++;
            if((actionList.size()<=index)) {
                playersManager.nextPlayerAndStartRound();
                index = 0;
                stateManager.setGameState(GameState.SELECTING);
                notifyRequest(new RequestEvent("Choose your worker", playersManager.getCurrentPlayer().getID()));
            }
            else sendActions();
        }

        else if(currentAction instanceof UserAction) {

            if(!((UserAction) currentAction).isOptional()) {
                ArrayList<Tile> availableTiles = ((UserAction) currentAction).getAvailableTilesForAction(playersManager.getCurrentWorker());
                if(availableTiles.size()==0) {
                    if(currentAction.isActionLock()) {
                        index++;
                        sendActions();
                        return;
                    }
                    playersManager.getCurrentWorker().setAvailable(false);
                    index = 0;
                    stateManager.setGameState(GameState.SELECTING);
                    ArrayList<Tile> oldGrid = saveOldGrid();
                    resetGrid();
                    sendChange(oldGrid);
                    notifyMessage(new MessageEvent("Your worker can't play", playersManager.getCurrentPlayer().getID()));
                    notifyRequest(new RequestEvent("Choose another worker", playersManager.getCurrentPlayer().getID()));
                }
                else {
                    availableActions.addAvailableAction((UserAction) currentAction);
                    stateManager.setGameState(GameState.ACTIONSELECTING);
                    index++;
                    notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                }
            }

            else {
                ArrayList<Tile> availableTiles = ((UserAction) currentAction).getAvailableTilesForAction(playersManager.getCurrentWorker());

                if(availableTiles.size()!=0)
                    availableActions.addAvailableAction((UserAction) currentAction);

                index++;

                if(actionList.size()<=index) {
                    availableActions.addAvailableActionName(ActionType.ENDROUND);
                    stateManager.setGameState(GameState.ACTIONSELECTING);
                    notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                }
                else {
                    currentAction = actionList.get(index);

                    if(currentAction instanceof RoundAction) {
                        availableActions.addAvailableActionName(ActionType.ENDROUND);
                        stateManager.setGameState(GameState.ACTIONSELECTING);
                        notify(new ActionEvent((ArrayList<String>) availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                    }

                    else if(currentAction instanceof UserAction) {
                        if((currentAction instanceof MoveAction && availableActions.getMoveActionIndex()!=-1) || (currentAction instanceof BuildAction && availableActions.getBuildActionIndex()!=-1))
                            System.out.println("FATAL ERROR: Adding two actions of the same type to the available actions");
                        else {
                            if(availableTiles.size()!=0)
                                availableActions.addAvailableAction((UserAction) currentAction);
                            stateManager.setGameState(GameState.ACTIONSELECTING);
                            index++;
                            notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                        }
                    }
                }
            }
        }
    }

    public void transition() throws IOException {
        saveGrid();
        sendActions();
    }

    private ArrayList<Tile> saveOldGrid() {
        ArrayList<Tile> oldGrid = new ArrayList<>();
        for(Tile t : Grid.getGrid().getTiles()) {
            Tile newT = new Tile(t.getX(), t.getY());
            newT.setWorker(t.getWorker());
            ArrayList<Integer> newLevels = new ArrayList<>(t.getLevels());
            newT.setLevels(newLevels);
            oldGrid.add(newT);
        }
        return oldGrid;
    }

    private void saveGrid() {

        savedWorkers.clear();
        savedTiles.clear();
        savedLevels.clear();

        for(Player p : playersManager.getPlayers())
            for (Worker w : p.getWorkers()) {
                savedWorkers.add(w);
                savedTiles.add(w.getPosition());
            }

        for(Tile tile : Grid.getGrid().getTiles())
            savedLevels.add(tile.getLevels());
    }

    private void resetGrid() {

        for(int i=0; i<savedWorkers.size(); i++) {

            Worker worker = savedWorkers.get(i);
            Tile tile = savedTiles.get(i);

            if(worker!=null)
                worker.setPosition(tile);

            if(tile!=null)
                tile.setWorker(worker);
        }

        for(int i=0; i<savedLevels.size(); i++)
            Grid.getGrid().getTiles().get(i).setLevels(savedLevels.get(i));
    }

    private void sendChange(ArrayList<Tile> oldGrid) throws IOException {
        ArrayList<Tile> newGrid = Grid.getGrid().getTiles();
        ArrayList<TileSimplified> modifiedTiles = new ArrayList<>();

        for (Tile newTile : newGrid) {
            for (Tile oldTile : oldGrid) {
                if(newTile.getX()==oldTile.getX()&&newTile.getY()==oldTile.getY()) {
                    if(newTile.getWorker()!=oldTile.getWorker()||newTile.getLevel()!=oldTile.getLevel())
                        modifiedTiles.add(newTile.simplify());
                }
            }
        }
        notify(new ChangeEvent(modifiedTiles));
    }

    private boolean checkWin() throws IOException {
        int winnerID = playersManager.getPlayerWinnerID();
        if(winnerID!=-1) {
            notifyWin(new WinEvent("You win", playersManager.getCurrentPlayer().getID()));
            for(Player p : playersManager.getNextPlayers())
                notifyLose(new LoseEvent("You lose", p.getID()));
            stateManager.setGameState(GameState.END);
            return true;
        }
        return false;
    }
}