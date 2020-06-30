package it.polimi.ingsw.Model;

import it.polimi.ingsw.CountdownInterface;
import it.polimi.ingsw.CountdownTask;
import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.ActionObservable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class ActionManager extends ActionObservable implements CountdownInterface {
    private final PlayersManager playersManager = PlayersManager.getPlayersManager();
    private final StateManager stateManager;
    private AvailableActions availableActions;
    private int index;
    private ActionType lastAction;
    private ArrayList<Tile> savedTiles = new ArrayList<>();
    private Timer undoTimer;

    /**
     * Invoked when countdown ends
     * @throws IOException when socket closes
     */
    @Override
    public void countdownEnded() throws IOException {
        if(undoTimer!=null) undoTimer.cancel();
        availableActions.removeAvailableActionName(ActionType.UNDO);
        availableActions.removeAvailableActionName(ActionType.CONFIRM);
        notifyMessage(new MessageEvent(306, playersManager.getCurrentPlayer().getID()));
        sendActions();
    }

    private class AvailableActions {
        MoveAction moveAction;
        int moveActionIndex;
        BuildAction buildAction;

        int buildActionIndex;
        private final ArrayList<ActionType> availableActionsNames = new ArrayList<>();

        /**
         * Initialization of Available Actions
         */
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

        /**
         * Used to add in tail the name of an availableActionName
         * @param availableActionName is the name of action we are adding to the list
         */
        public void addAvailableActionName(ActionType availableActionName) {
            availableActionsNames.add(availableActionName);
        }
        /**
         * Used to remove the name of an availableAction
         * @param availableActionName is the name of action we are adding to the list
         */
        public void removeAvailableActionName(ActionType availableActionName) {
            availableActionsNames.remove(availableActionName);
        }

        /**
         * Used to add in tail the availableAction
         * @param availableAction is the action we are adding the list
         */
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

    /**
     * Receives the coordinates(x, y), checks if is the playerID turn, then moves the worker in that position
     * @param playerID indicates the player in that turn
     * @param x X axis position
     * @param y Y axis position
     * @throws IOException when socket closes
     */
    public void move(int playerID, int x, int y) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTING))
            return;

        if(!availableActions.getAvailableActionsNames().contains(ActionType.MOVE)) {
            notifyMessage(new MessageEvent(402, playersManager.getCurrentPlayer().getID()));
            return;
        }

        MoveAction moveAction = availableActions.getMoveAction();
        Worker worker = playersManager.getCurrentWorker();
        Tile tile = Grid.getGrid().getTile(x, y);

        if(!moveAction.canMove(worker, tile))   notifyMessage(new MessageEvent(403, playersManager.getCurrentPlayer().getID()));
        else moveMethod(moveAction, worker, tile);
    }

    /**
     * Receives the coordinates(x, y), checks if is the playerID turn, then builds in that position
     * @param playerID indicates the player in that turn
     * @param x X axis position
     * @param y Y axis position
     * @param buildLevel level of new building in the tile(x, y)
     * @throws IOException when socket closes
     */
    public void build(int playerID, int x, int y, int buildLevel) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTING))
            return;

        if(!availableActions.getAvailableActionsNames().contains(ActionType.BUILD)) {
            notifyMessage(new MessageEvent(402, playersManager.getCurrentPlayer().getID()));
            return;
        }

        BuildAction buildAction = availableActions.getBuildAction();
        Worker worker = playersManager.getCurrentWorker();
        Tile tile = Grid.getGrid().getTile(x, y);

        if(buildLevel==-1) {
            buildWithoutLevelMethod(buildAction, worker, tile);
        }

        else {
            if(!buildAction.canBuild(worker, tile, buildLevel)) notifyMessage(new MessageEvent(404, playersManager.getCurrentPlayer().getID()));
            else buildWithLevelMethod(buildAction, worker, tile, buildLevel);
        }
    }

    /**
     * Checks if the received action is one from the available actions in the list, then he proceeds to do it
     * @param playerID indicates the player in that turn
     * @param action the action checked
     * @throws IOException when socket closes
     */
    public void actionSelect(int playerID, String action) throws IOException {

        if(!stateManager.checkPlayerID(playerID))
            return;

        if(!stateManager.checkState(GameState.ACTIONSELECTING))
            return;

        if(!availableActions.getAvailableActionsNames().contains(ActionType.valueOf(action))) {
            notifyMessage(new MessageEvent(402, playersManager.getCurrentPlayer().getID()));
            return;
        }

        switch (ActionType.valueOf(action)) {
            case MOVE: {
                classicMove();
                break;
            }
            case BUILD: {
                classicBuild();
                break;
            }
            case ENDROUND: {
                classicEndRound();
                break;
            }
            case UNDO: {
                classicUndo();
                break;
            }
            case CONFIRM: {
                classicConfirm();
                break;
            }
        }
    }

    /**
     * Sends the available actions
     * @throws IOException when socket closes
     */
    public void sendActions() throws IOException {
        availableActions = new AvailableActions();
        ArrayList<Action> actionList = playersManager.getActionOrder();
        if(actionList.size()==0) {
            System.out.println("Size = 0");
            return;
        }

        if(checkSize(actionList))
            return;

        Action currentAction = actionList.get(index);

        if(currentAction instanceof RoundAction) {
            roundActionMethod(currentAction, actionList);
        }

        else if(currentAction instanceof UserAction) {

            if(!((UserAction) currentAction).isOptional()) {
                userActionNotOptional(currentAction);
            }
            else {
                userActionOptional(currentAction, actionList);
            }
        }
    }

    /**
     * Is a transition method, saves the grid and sends the available actions
     * @throws IOException when socket closes
     */
    public void transition() throws IOException {
        saveGrid();
        sendActions();
    }

    /**
     * Saves the all grid in an ArrayList<Tile> before doing an action and returns it
     * @return the grid saved
     */
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

    /**
     * Saves the grid in the initial phase of the turn, used in case of multiple actions to delete
     */
    private void saveGrid() {
        savedTiles = new ArrayList<>(saveOldGrid());
    }

    /**
     * Reset the grid to the original state
     */
    private void resetGrid() {

        for(Tile t : Grid.getGrid().getTiles()) {
            for(Tile newT : savedTiles) {
                if(t.getX() == newT.getX() && t.getY() == newT.getY()) {
                    t.setLevels(newT.getLevels());
                    if(newT.getWorker()!=null) {
                        Worker worker = playersManager.getWorkerWithID(newT.getWorker().getPlayerID(), newT.getWorker().getLocalID());
                        t.setWorker(worker);
                        worker.setPosition(t);
                    }
                    else t.setWorker(null);
                }
            }
        }
    }

    /**
     * Checks all the changes from the current grid to the oldGrid received and notifies all of them
     * @param oldGrid is the grid saved before doing the action
     * @throws IOException when socket closes
     */
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

    /**
     * Used to check the winner
     * @return true when a winner is selected, otherwise false
     * @throws IOException when socket closes
     */
    private boolean checkWin() throws IOException {
        int winnerID = playersManager.getPlayerWinnerID();
        if(winnerID!=-1) {
            String winnerName = playersManager.getPlayerWithID(winnerID).getName();
            notifyWin(new WinEvent(winnerName, -1));
            stateManager.setGameState(GameState.END);
            return true;
        }
        return false;
    }

    /**
     * Used to check the loser
     * @return true when a loser is selected, otherwise false
     * @throws IOException when socket closes
     */
    private boolean checkLose() throws IOException {
        ArrayList<Worker> workers = playersManager.getCurrentPlayer().getWorkers();
        if(workers.stream().noneMatch(Worker::isAvailable)) {
            for(Player player : playersManager.getPlayers()) notifyLose(new LoseEvent(playersManager.getCurrentPlayer().getID()==player.getID(), playersManager.getCurrentPlayer().getName(), player.getID()));
            ArrayList<Tile> oldGrid = saveOldGrid();
            playersManager.deleteCurrentPlayer();
            sendChange(oldGrid);
            playersManager.nextPlayerAndStartRound();
            return true;
        }
        return false;
    }

    /**
     * Receives the moveAction, the worker's id and the tile, moves the worker into that tile and changes the grid
     * @param moveAction action from the playerID's list of actions
     * @param worker name of the worker moved
     * @param tile tile where worker moves
     * @throws IOException when socket closes
     */
    private void moveMethod(MoveAction moveAction, Worker worker, Tile tile) throws IOException {
        ArrayList<Tile> oldGrid = saveOldGrid();
        moveAction.move(worker, tile);
        lastAction = ActionType.MOVE;
        sendChange(oldGrid);
        if(checkWin()) return;
        index = availableActions.getMoveActionIndex() + 1;
        stateManager.setGameState(GameState.ACTIONSELECTING);
        undoCountdown();
        //sendActions();
    }

    /**
     * This method implements a move without the request of a buildLevel, checks if the the buildAction is available and then does it
     * @param buildAction action for the playedID's list of actions
     * @param worker name of the worker that builds
     * @param tile tile where worker moves
     * @throws IOException when socket closes
     */
    private void buildWithoutLevelMethod(BuildAction buildAction, Worker worker, Tile tile) throws IOException {
        if(!buildAction.canBuild(worker, tile)) notifyMessage(new MessageEvent(404, playersManager.getCurrentPlayer().getID()));
        else {
            ArrayList<Tile> oldGrid = saveOldGrid();
            buildAction.build(worker, tile);
            lastAction = ActionType.BUILD;
            sendBuildChanges(oldGrid);
            undoCountdown();
        }
    }

    /**
     * Method used when correctly built, sends the change done to the grid
     * @param oldGrid the grid saved before the build
     * @throws IOException when socket closes
     */
    private void sendBuildChanges(ArrayList<Tile> oldGrid) throws IOException {
        sendChange(oldGrid);
        if(checkWin()) return;
        index = availableActions.getBuildActionIndex() + 1;
        stateManager.setGameState(GameState.ACTIONSELECTING);
        //sendActions();
    }

    /**
     * Receives the buildAction, the worker's id and the tile, build  into that tile with the worker and changes the grid,
     * @param buildAction action from the playerID's list of actions
     * @param worker name of the worker that build
     * @param tile tile where build
     * @param buildLevel new level of the building on the tile
     * @throws IOException when socket closes
     */
    private void buildWithLevelMethod(BuildAction buildAction, Worker worker, Tile tile, int buildLevel) throws IOException {
        ArrayList<Tile> oldGrid = saveOldGrid();
        buildAction.build(worker, tile, buildLevel);
        lastAction = ActionType.BUILD;
        sendBuildChanges(oldGrid);
        undoCountdown();
    }

    /**
     * This countdown is used after the select of an action and permits the player to UNDO that action during this countdown
     * @throws IOException when socket closes
     */
    private void undoCountdown() throws IOException {
        availableActions.getAvailableActionsNames().clear();
        availableActions.addAvailableActionName(ActionType.UNDO);
        availableActions.addAvailableActionName(ActionType.CONFIRM);
        notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
        undoTimer = new Timer();
        TimerTask undoTask = new CountdownTask(5, this);
        undoTimer.schedule(undoTask, 0, 1000);
    }

    /**
     * This is the standard move action
     * @throws IOException when socket closes
     */
    private void classicMove() throws IOException {
        ArrayList<Tile> availableTiles = availableActions.getMoveAction().getAvailableTilesForAction(playersManager.getCurrentWorker());
        stateManager.setGameState(GameState.ACTING);
        notify(new AvailableTilesEvent((ArrayList<TileSimplified>)availableTiles.stream().map(Tile::simplify).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID(), ActionType.MOVE));
        notifyMessage(new MessageEvent(101, playersManager.getCurrentPlayer().getID()));
    }

    /**
     * This is the standard build action
     * @throws IOException when socket closes
     */
    private void classicBuild() throws IOException {
        ArrayList<Tile> availableTiles = availableActions.getBuildAction().getAvailableTilesForAction(playersManager.getCurrentWorker());
        stateManager.setGameState(GameState.ACTING);
        notify(new AvailableTilesEvent((ArrayList<TileSimplified>)availableTiles.stream().map(Tile::simplify).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID(), ActionType.BUILD));
        notifyMessage(new MessageEvent(102, playersManager.getCurrentPlayer().getID()));
    }

    /**
     * This is the standard end round without the implementation of god powers
     * @throws IOException when socket closes
     */
    private void classicEndRound() throws IOException {
        index++;
        sendActions();
    }

    /**
     * This is the standard UNDO used to cancel an action previously committed to the server
     * @throws IOException when socket closes
     */
    private void classicUndo() throws IOException {
        if(undoTimer!=null) undoTimer.cancel();
        int moveIndex = availableActions.getMoveActionIndex();
        int buildIndex = availableActions.getBuildActionIndex();
        if(moveIndex==-1) index = buildIndex;
        else if(buildIndex==-1) index = moveIndex;
        else index = Math.min(moveIndex, buildIndex);
        ArrayList<Tile> oldGrid = saveOldGrid();
        if(lastAction==ActionType.MOVE)
            availableActions.getMoveAction().undo();
        else if(lastAction==ActionType.BUILD)
            availableActions.getBuildAction().undo();
        sendChange(oldGrid);
        sendActions();
    }

    /**
     * This is the classic confirm action used to confirm an action without waiting the UNDO Countdown
     * @throws IOException when socket closes
     */
    private void classicConfirm() throws IOException {
        countdownEnded();
    }

    /**
     * Used to check if the player has or not new available actions to do
     * @param actionList the list of actions of the player in that turn
     * @return true if the player can do some actions, false otherwise
     * @throws IOException when socket closes
     */
    private boolean checkSize(ArrayList<Action> actionList) throws IOException {
        if((actionList.size()<=index)) {
            playersManager.nextPlayerAndStartRound();
            for(Player p : playersManager.getNextPlayers())
                notifyMessage(new MessageEvent(115, p.getID()));
            index = 0;
            stateManager.setGameState(GameState.SELECTING);
            notifyMessage(new MessageEvent(103, playersManager.getCurrentPlayer().getID()));
            return true;
        }
        return false;
    }

    /**
     * Automatic actions done between players round
     * @throws IOException when socket closes
     */
    private void roundActionMethod(Action currentAction, ArrayList<Action> actionList) throws IOException {
        ArrayList<Tile> oldGrid = saveOldGrid();
        ((RoundAction) currentAction).doAction();
        sendChange(oldGrid);
        if(checkWin())
            return;
        index++;
        if((actionList.size()<=index)) {
            playersManager.nextPlayerAndStartRound();
            for(Player p : playersManager.getNextPlayers())
                notifyMessage(new MessageEvent(115, p.getID()));
            index = 0;
            stateManager.setGameState(GameState.SELECTING);
            notifyMessage(new MessageEvent(103, playersManager.getCurrentPlayer().getID()));
        }
        else sendActions();
    }

    /**
     *If found a notOptional action, forces the player to do it.
     * @param currentAction the action need to be done by the player
     * @throws IOException when socket closes
     */
    private void userActionNotOptional(Action currentAction) throws IOException {
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
            notifyMessage(new MessageEvent(301, playersManager.getCurrentPlayer().getID()));
            checkWinLose();
        }
        else {
            availableActions.addAvailableAction((UserAction) currentAction);
            stateManager.setGameState(GameState.ACTIONSELECTING);
            index++;
            notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
        }
    }

    /**
     * Checks if the player has lost and if there is a winner
     * @throws IOException when socket closes
     */
    public void checkWinLose() throws IOException {
        if(!checkLose()) {
            notifyMessage(new MessageEvent(104, playersManager.getCurrentPlayer().getID()));
            stateManager.setGameState(GameState.SELECTING);
        }
        else {
            if(!checkWin()) {
                notifyMessage(new MessageEvent(103, playersManager.getCurrentPlayer().getID()));
            }
        }
    }

    /**
     * Ends the round
     * @throws IOException when socket closes
     */
    private void endRound() throws IOException {
        availableActions.addAvailableActionName(ActionType.ENDROUND);
        stateManager.setGameState(GameState.ACTIONSELECTING);
        notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
    }

    /**
     * Selects the Optional and non optional actions available for the player in that moment
     * @param currentAction the current action done by the player
     * @param actionList list of actions of the player
     * @throws IOException when socket closes
     */
    private void userActionOptional(Action currentAction, ArrayList<Action> actionList) throws IOException {
        ArrayList<Tile> availableTiles = ((UserAction) currentAction).getAvailableTilesForAction(playersManager.getCurrentWorker());

        if(availableTiles.size()!=0)
            availableActions.addAvailableAction((UserAction) currentAction);
        else {
            index++;
            sendActions();
            return;
        }

        index++;

        if(actionList.size()<=index) {
            endRound();
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
                    System.err.println("FATAL ERROR: Adding two actions of the same type to the available actions");
                else {
                    availableTiles = ((UserAction) currentAction).getAvailableTilesForAction(playersManager.getCurrentWorker());
                    if(availableTiles.size()!=0) availableActions.addAvailableAction((UserAction) currentAction);
                    stateManager.setGameState(GameState.ACTIONSELECTING);
                    index++;
                    notify(new ActionEvent((ArrayList<String>)availableActions.getAvailableActionsNames().stream().map(Enum::toString).collect(Collectors.toList()), playersManager.getCurrentPlayer().getID()));
                }
            }
        }
    }
}