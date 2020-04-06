package it.polimi.ingsw.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CardsBuilder {
    static MoveActionStandard moveActionStandard = new MoveActionStandard();
    static BuildActionStandard buildActionStandard = new BuildActionStandard();

    public void createCards() {
        Deck deck = Deck.getDeck();

        Card card  = new Card("Apollo");
        deck.addCard(card);

        Card card1 = new Card("Artemis");
        deck.addCard(card1);

        Card card2 = new Card("Athena");
        deck.addCard(card2);

        Card card3 = new Card("Atlas");
        deck.addCard(card3);

        Card card4 = new Card("Demeter");
        deck.addCard(card4);

        Card card5 = new Card("Hephaestus");
        deck.addCard(card5);

        Card card6 = new Card("Minotaur");
        deck.addCard(card6);

        Card card7 = new Card("Pan");
        deck.addCard(card7);

        Card card8 = new Card("Prometheus");
        deck.addCard(card8);

        Card card9 = new Card("Chronus", true);
        deck.addCard(card9);

        Card card10 = new Card("Hestia");
        deck.addCard(card10);

        Card card11 = new Card("Poseidon");
        deck.addCard(card11);

        Card card12 = new Card("Triton");
        deck.addCard(card12);

        Card card13 = new Card("Zeus");
        deck.addCard(card13);
    }

    public void createAction(Card card) {

        if(card.getName().equals("Apollo")) {
            card.setActionOrder(createApollo());
        }
        if(card.getName().equals("Artemis")) {
            card.setActionOrder(createArtemis());
        }
        if(card.getName().equals("Athena")) {
            card.setActionOrder(createAthena());
        }
        if(card.getName().equals("Atlas")) {
            card.setActionOrder(createAtlas());
        }
        if(card.getName().equals("Demeter")) {
            card.setActionOrder(createDemeter());
        }
        if(card.getName().equals("Hephaestus")) {
            card.setActionOrder(createHephaestus());
        }
        if(card.getName().equals("Minotaur")) {
            card.setActionOrder(createMinotaur());
        }
        if(card.getName().equals("Pan")) {
            card.setActionOrder(createPan());
        }
        if(card.getName().equals("Prometheus")) {
            card.setActionOrder(createPrometheus());
        }
    }

    public ArrayList<Action> createApollo() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    Worker enemyWorker = tileWhereMove.getWorker();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if(enemyWorker==null)
                        return;
                    moveActionStandard.getLastActionSave().saveAdditionalWorker(enemyWorker);
                    enemyWorker.setPosition(currentPosition);
                    currentPosition.setWorker(enemyWorker);
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                if(tileWhereMove.isEmpty())
                    return moveActionStandard.canMove(worker, tileWhereMove);
                else {
                    Tile currentTile = worker.getPosition();
                    int enemyID = tileWhereMove.getWorker().getPlayerID();
                    if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && moveActionStandard.isCantMoveUp())
                        return false;
                    else
                        return (tileWhereMove.getLevel() - currentTile.getLevel() <= 1 && moveActionStandard.correctTile(currentTile, tileWhereMove) && enemyID != worker.getPlayerID());
                }
            }

            @Override
            public void setCantMoveUp(boolean cantMoveUp) {
                moveActionStandard.setCantMoveUp(cantMoveUp);
            }

            @Override
            public boolean isCantMoveUp() {
                return moveActionStandard.isCantMoveUp();
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(false);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    public ArrayList<Action> createArtemis() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove))
                    moveActionStandard.standardMove(worker, tileWhereMove);
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return (moveActionStandard.canMove(worker, tileWhereMove) && tileWhereMove != moveActionStandard.getLastActionSave().getSavedTile());
            }

            @Override
            public void setCantMoveUp(boolean cantMoveUp) {
                moveActionStandard.setCantMoveUp(cantMoveUp);
            }

            @Override
            public boolean isCantMoveUp() {
                return moveActionStandard.isCantMoveUp();
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionStandard.setOptional(false);
        moveActionDecorator.setOptional(true);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionStandard);
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    public ArrayList<Action> createAthena() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentTile = worker.getPosition();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if (tileWhereMove.getLevel() - currentTile.getLevel() == 1) {
                        ArrayList<Player> players = PlayersManager.getPlayersManager().getNextPlayers();

                        //Sets that opponents workers cannot move up
                        for (Player player : players) {
                            ArrayList<MoveAction> moveActions = player.getCard().getMoveActions();
                            for (MoveAction moveAction : moveActions) {
                                moveAction.setCantMoveUp(true);
                            }
                        }
                    }
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return moveActionStandard.canMove(worker, tileWhereMove);
            }

            @Override
            public void setCantMoveUp(boolean cantMoveUp) {
                moveActionStandard.setCantMoveUp(cantMoveUp);
            }

            @Override
            public boolean isCantMoveUp() {
                return moveActionStandard.isCantMoveUp();
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    public ArrayList<Action> createAtlas() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(worker, tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                buildActionStandard.build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) || ((buildActionStandard.correctTile(worker.getPosition(), tileWhereBuild)) && newLevel==4 && tileWhereBuild.getLevel()==0));
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                buildActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionStandard.setOptional(false);
        buildActionDecorator.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }

    public ArrayList<Action> createDemeter() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(worker, tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && tileWhereBuild != buildActionStandard.getLastActionSave().getSavedTile());
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                buildActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionStandard.setOptional(false);
        buildActionStandard.setOptional(false);
        buildActionDecorator.setOptional(true);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }

    public ArrayList<Action> createHephaestus() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(worker, tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && buildActionStandard.getLastActionSave().getSavedTile() == tileWhereBuild && newLevel!=4);
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                buildActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionStandard.setOptional(false);
        buildActionStandard.setOptional(false);
        buildActionDecorator.setOptional(true);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }

    public ArrayList<Action> createMinotaur() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if (canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    Worker enemyWorker = tileWhereMove.getWorker();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if (enemyWorker == null)
                        return;
                    moveActionStandard.getLastActionSave().saveAdditionalWorker(enemyWorker);
                    Tile oppositeTile = Grid.getGrid().getOppositeTile(currentPosition, tileWhereMove);
                    enemyWorker.setPosition(oppositeTile);
                    oppositeTile.setWorker(enemyWorker);
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                if(tileWhereMove.isEmpty())
                    return moveActionStandard.canMove(worker, tileWhereMove);
                else {
                    Tile currentTile = worker.getPosition();
                    int enemyID = tileWhereMove.getWorker().getPlayerID();
                    if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && moveActionStandard.isCantMoveUp())
                        return false;
                    else if(tileWhereMove.getLevel() - currentTile.getLevel() <= 1 && moveActionStandard.correctTile(currentTile, tileWhereMove) && enemyID != worker.getPlayerID()) {
                        Tile oppositeTile = Grid.getGrid().getOppositeTile(worker.getPosition(), tileWhereMove);
                        return oppositeTile != null && oppositeTile.isEmpty() && oppositeTile.getLevel() != 4;
                    }
                    return false;
                }
            }

            @Override
            public void setCantMoveUp(boolean cantMoveUp) {
                moveActionStandard.setCantMoveUp(cantMoveUp);
            }

            @Override
            public boolean isCantMoveUp() {
                return moveActionStandard.isCantMoveUp();
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker, this);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    public ArrayList<Action> createPan() {
        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = new MoveActionStandard();

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if(currentPosition.getLevel() - tileWhereMove.getLevel() >=2)
                        new WinManager().winCurrentPlayer();
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return moveActionStandard.canMove(worker, tileWhereMove);
            }

            @Override
            public void setCantMoveUp(boolean cantMoveUp) {
                moveActionStandard.setCantMoveUp(cantMoveUp);
            }

            @Override
            public boolean isCantMoveUp() {
                return moveActionStandard.isCantMoveUp();
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return moveActionStandard.getAvailableTilesForAction(worker);
            }

            @Override
            public boolean isOptional() {
                return moveActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                moveActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                moveActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    public ArrayList<Action> createPrometheus() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel)) {
                    buildActionStandard.standardBuild(worker, tileWhereBuild, newLevel);
                    moveActionStandard.setCantMoveUp(true);
                    buildActionStandard.setActionLock(true);
                }
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return buildActionStandard.canBuild(worker, tileWhereBuild, newLevel);
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker);
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean isOptional) {
                buildActionStandard.setOptional(isOptional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                moveActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return moveActionStandard.isActionLock();
            }
        };

        buildActionDecorator.setOptional(true);
        moveActionStandard.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<Action>();
        actions.add(buildActionDecorator);
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        return actions;
    }
}
