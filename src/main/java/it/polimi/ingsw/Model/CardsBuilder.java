package it.polimi.ingsw.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CardsBuilder {
    static MoveActionStandard moveActionStandard = new MoveActionStandard();
    static BuildActionStandard buildActionStandard = new BuildActionStandard();

    public ArrayList<Card> createCards() {
        Card card  = new Card("Apollo", true, false);
        Card card1 = new Card("Artemis", false, false);
        Card card2 = new Card("Athena", false, false);
        Card card3 = new Card("Atlas", false ,false);
        Card card4 = new Card("Demeter", false ,false);
        Card card5 = new Card("Hephaestus", false, false);
        Card card6 = new Card("Minotaur", true, false);
        Card card7 = new Card("Pan", false, false);
        Card card8 = new Card("Prometheus", false, false);
        Card card9 = new Card("Chronus", false , true);
        Card card10 = new Card("Hestia", false, false);
        Card card11 = new Card("Poseidon", false, false);
        Card card12 = new Card("Triton", false, false);
        Card card13 = new Card("Zeus", false , false);
        return (ArrayList<Card>) Arrays.asList(card, card1, card2, card3);
    }

    public void createAction(Card card) {
        ActionOrder actionOrder = new ActionOrder();
        if(card.getName().equals("Apollo")) {
            actionOrder.setActions(createApollo());
        }
        if(card.getName().equals("Artemis")) {
            actionOrder.setActions(createArtemis());
        }
        if(card.getName().equals("Athena")) {
            actionOrder.setActions(createAthena());
        }
        if(card.getName().equals("Atlas")) {
            actionOrder.setActions(createAtlas());
        }
        if(card.getName().equals("Demeter")) {
            actionOrder.setActions(createDemeter());
        }


        card.setActionOrder(actionOrder);
    }

    public ArrayList<Action> createApollo() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    Worker enemyWorker = tileWhereMove.getWorker();
                    moveActionStandard.move(worker, tileWhereMove,this);
                    if(enemyWorker==null)
                        return;
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
                    if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && !moveActionStandard.isCanMoveUp())
                        return false;
                    else
                        return (tileWhereMove.getLevel() - currentTile.getLevel() <= 1 && moveActionStandard.correctTile(currentTile, tileWhereMove) && enemyID != worker.getPlayerID());
                }
            }

            @Override
            public void setCanMoveUp(boolean canMoveUp) {
                moveActionStandard.setCanMoveUp(canMoveUp);
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
                moveActionStandard.move(worker, tileWhereMove);
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return (moveActionStandard.canMove(worker, tileWhereMove) && tileWhereMove!=moveActionStandard.getLastActionSave().getCurrentTile());
            }

            @Override
            public void setCanMoveUp(boolean canMoveUp) {
                moveActionStandard.setCanMoveUp(canMoveUp);
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
        };

        moveActionStandard.setOptional(false);
        moveActionDecorator.setOptional(true);
        buildActionStandard.setOptional(false);

        return (ArrayList<Action>) Arrays.asList(moveActionStandard, moveActionDecorator, buildActionStandard);
    }

    public ArrayList<Action> createAthena() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            MoveActionStandard moveActionStandard = CardsBuilder.moveActionStandard;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                moveActionStandard.move(worker, tileWhereMove);
                if(tileWhereMove.getLevel() - worker.getPosition().getLevel() == 1) {
                    ArrayList<Player> players = PlayersManager.getPlayersManager().getNextPlayers();

                    //Sets that opponents workers cannot move up
                    for(Player player : players) {
                        ArrayList<MoveAction> moveActions = player.getCard().getMoveActions();
                        for(MoveAction moveAction : moveActions) {
                            moveAction.setCanMoveUp(false);
                        }
                    }
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return moveActionStandard.canMove(worker, tileWhereMove);
            }

            @Override
            public void setCanMoveUp(boolean canMoveUp) {
                moveActionStandard.setCanMoveUp(canMoveUp);
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
        };

        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);

        return (ArrayList<Action>) Arrays.asList(moveActionDecorator, buildActionStandard);
    }

    public ArrayList<Action> createAtlas() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                buildActionStandard.build(worker,tileWhereBuild,newLevel, this);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                buildActionStandard.build(worker, tileWhereBuild, this);
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
        };

        moveActionStandard.setOptional(false);
        buildActionDecorator.setOptional(false);

        return (ArrayList<Action>) Arrays.asList(moveActionStandard, buildActionDecorator);
    }

    public ArrayList<Action> createDemeter() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            BuildActionStandard buildActionStandard = CardsBuilder.buildActionStandard;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                buildActionStandard.build(worker, tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                buildActionStandard.build(worker, tileWhereBuild);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && tileWhereBuild != buildActionStandard.getLastActionSave().getCurrentTile());
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
        };

        moveActionStandard.setOptional(false);
        buildActionStandard.setOptional(false);
        buildActionDecorator.setOptional(true);

        return (ArrayList<Action>) Arrays.asList(moveActionStandard, buildActionStandard, buildActionDecorator);
    }


}
