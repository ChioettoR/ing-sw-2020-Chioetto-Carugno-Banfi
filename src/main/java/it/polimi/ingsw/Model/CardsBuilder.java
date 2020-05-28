package it.polimi.ingsw.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CardsBuilder {

    public enum GodPower {
        CanSwitch, CanMoveTwice, CantMoveUp, CanBuildDome, CanBuildTwiceDifferent, CanBuildTwice, CanPush, MoveDownToWin, BuildBeforeMove,
        CompleteTowersObserver, BuildTwiceNoPerimeter, PetrifyOpponents, CanMoveMultipleTimesPerimeter, CanBuildBelow
    }

    /**
     * This method creates the whole cards without the effects from the godCards.xml
     * @return the cards
     */
    public ArrayList<Card> createCards() {

        ArrayList<Card> cards = new ArrayList<>();

        try {
            File fXmlFile = new File("src/main/resources/godCards.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("God");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                    String effect = eElement.getElementsByTagName("Effects").item(0).getTextContent();
                    String displayEffect = eElement.getElementsByTagName("DisplayEffects").item(0).getTextContent();
                    String description = eElement.getElementsByTagName("Description").item(0).getTextContent();
                    Card card;
                    if(GodPower.valueOf(effect).equals(GodPower.CompleteTowersObserver)) card = new Card(name, GodPower.valueOf(effect), true);
                    else card = new Card(name, GodPower.valueOf(effect));
                    card.setEffectName(displayEffect);
                    card.setDescription(description);
                    cards.add(card);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * This method implements the effect of the cards from the godCards.xml
     * @param card card to implement
     */
    public void createAction(Card card) {

        GodPower godPower = card.getGodPower();

        switch (godPower) {
            case CanSwitch: {
                card.setActionOrder(createCanSwitch());
                break;
            }
            case CanMoveTwice: {
                card.setActionOrder(createCanMoveTwice());
                break;
            }
            case CantMoveUp: {
                card.setActionOrder(createCantMoveUp());
                break;
            }
            case CanBuildDome: {
                card.setActionOrder(createCanBuildDome());
                break;
            }
            case CanBuildTwiceDifferent: {
                card.setActionOrder(createCanBuildTwiceDifferent());
                break;
            }

            case CanBuildTwice: {
                card.setActionOrder(createCanBuildTwice());
                break;
            }

            case CanPush: {
                card.setActionOrder(createCanPush());
                break;
            }

            case MoveDownToWin: {
                card.setActionOrder(createMoveDownToWin());
                break;
            }

            case BuildBeforeMove: {
                card.setActionOrder(createBuildBeforeMove());
                break;
            }

            case CompleteTowersObserver: {
                card.setActionOrder(createCompleteTowersObserver());
                break;
            }

            case BuildTwiceNoPerimeter: {
                card.setActionOrder(createBuildTwiceNoPerimeter());
                break;
            }

            case CanMoveMultipleTimesPerimeter: {
                card.setActionOrder(createCanMoveMultipleTimesPerimeter());
                break;
            }

            case CanBuildBelow: {
                card.setActionOrder(createCanBuildBelow());
                break;
            }

            case PetrifyOpponents: {
                card.setActionOrder(createPetrifyOpponents());
                break;
            }
        }

    }

    /**
     * Implements an effect where you can switch position with opponent workers
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanSwitch() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    Worker enemyWorker = tileWhereMove.getWorker();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if(enemyWorker==null) return;
                    moveActionStandard.getLastActionSave().saveAdditionalWorker(enemyWorker);
                    enemyWorker.setPosition(currentPosition);
                    currentPosition.setWorker(enemyWorker);
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {

                if(isActionLock())
                    return false;

                if(tileWhereMove.isEmpty())
                    return moveActionStandard.canMove(worker, tileWhereMove);

                else {
                    Tile currentTile = worker.getPosition();
                    int enemyID = tileWhereMove.getWorker().getPlayerID();
                    if ((tileWhereMove.getLevel() - currentTile.getLevel() == 1) && moveActionStandard.isCantMoveUp()) return false;
                    else return (tileWhereMove.getLevel() - currentTile.getLevel() <= 1 && moveActionStandard.correctTile(currentTile, tileWhereMove) && enemyID != worker.getPlayerID());
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

        BuildActionStandard buildActionStandard = new BuildActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }
    /**
     * Implements an effect where you can move twice, but no turning back
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanMoveTwice() {

        MoveActionStandard firstMoveActionStandard = new MoveActionStandard();

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove))
                    moveActionStandard.standardMove(worker, tileWhereMove);
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return (moveActionStandard.canMove(worker, tileWhereMove) && tileWhereMove != firstMoveActionStandard.getLastActionSave().getSavedTile());
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
            public void setOptional(boolean optional) {
                moveActionStandard.setOptional(optional);
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

        moveActionDecorator.setOptional(true);
        BuildActionStandard buildActionStandard = new BuildActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(firstMoveActionStandard);
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }
    /**
     * Implements an effect where if you move up, the opponents can't move up in the same turn
     * @return actions available for this effect
     */
    public ArrayList<Action> createCantMoveUp() {

        RoundAction roundAction = new RoundAction() {
            boolean actionLock;

            @Override
            public void doAction() {
                if(actionLock) return;
                ArrayList<Player> players = PlayersManager.getPlayersManager().getNextPlayers();
                //Sets that opponents workers cannot move up
                for (Player player : players) {
                    ArrayList<MoveAction> moveActions = player.getCard().getMoveActions();
                    for (MoveAction moveAction : moveActions) {
                        moveAction.setCantMoveUp(true);
                    }
                }
            }

            @Override
            public void setActionLock(boolean actionLock) {
                this.actionLock = actionLock;
            }

            @Override
            public boolean isActionLock() {
                return actionLock;
            }
        };

        RoundAction roundAction1 = new RoundAction() {
            boolean actionLock;

            @Override
            public void setActionLock(boolean actionLock) {
                this.actionLock = actionLock;
            }

            @Override
            public boolean isActionLock() {
                return actionLock;
            }

            @Override
            public void doAction() {
                for(Player p : PlayersManager.getPlayersManager().getNextPlayers())
                    p.resetMoveUp();
            }
        };

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if (canMove(worker, tileWhereMove)) {
                    Tile currentTile = worker.getPosition();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    roundAction.setActionLock(tileWhereMove.getLevel() - currentTile.getLevel() != 1);
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
            public void setOptional(boolean optional) {
                moveActionStandard.setOptional(optional);
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

        BuildActionStandard buildActionStandard = new BuildActionStandard();
        roundAction.setActionLock(true);
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(roundAction1);
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        actions.add(roundAction);
        return actions;
    }
    /**
     * Implements an effect where you can build domes at any level
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanBuildDome() {

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                buildActionStandard.build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(isActionLock()) return false;
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) || ((buildActionStandard.correctTile(worker.getPosition(), tileWhereBuild)) && newLevel==4 && tileWhereBuild.isEmpty()));
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
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
            public void setOptional(boolean optional) {
                buildActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                buildActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return buildActionStandard.isActionLock();
            }
        };

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }

    /**
     * Implements an effect where you can build twice, but not on the same space
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanBuildTwiceDifferent() {

        BuildActionStandard firstBuildActionStandard = new BuildActionStandard();

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && tileWhereBuild != firstBuildActionStandard.getLastActionSave().getSavedTile());
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
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
            public void setOptional(boolean optional) {
                buildActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                buildActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return buildActionStandard.isActionLock();
            }
        };

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        buildActionDecorator.setOptional(true);
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(firstBuildActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }
    /**
     * Implements an effect where you can build twice, but only in the same space (not dome)
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanBuildTwice() {

        BuildActionStandard firstBuildActionStandard = new BuildActionStandard();

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && firstBuildActionStandard.getLastActionSave().getSavedTile() == tileWhereBuild && newLevel!=4);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
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
            public void setOptional(boolean optional) {
                buildActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                buildActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return buildActionStandard.isActionLock();
            }
        };

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        buildActionDecorator.setOptional(true);
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(firstBuildActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }
    /**
     * Implements an effect where your worker may move into an opponent worker's space (using normal movement rules),if the next space in the same direction is unoccupied. Their worker is forced into that space (regardless of its level)
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanPush() {

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();
            boolean isOptional;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if (canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    Worker enemyWorker = tileWhereMove.getWorker();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if (enemyWorker == null) return;
                    moveActionStandard.getLastActionSave().saveAdditionalWorker(enemyWorker);
                    Tile oppositeTile = Grid.getGrid().getOppositeTile(currentPosition, tileWhereMove);
                    enemyWorker.setPosition(oppositeTile);
                    oppositeTile.setWorker(enemyWorker);
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                if(isActionLock())
                    return false;
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
                return isOptional;
            }

            @Override
            public void setOptional(boolean optional) {
                isOptional = optional;
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

        BuildActionStandard buildActionStandard = new BuildActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }

    /**
     * Implements an effect where you also win if your worker moves down two or more levels
     * @return actions available for this effect
     */
    public ArrayList<Action> createMoveDownToWin() {
        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    Tile currentPosition = worker.getPosition();
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if(currentPosition.getLevel() - tileWhereMove.getLevel() >=2)
                        PlayersManager.getPlayersManager().winCurrentPlayer();
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
            public void setOptional(boolean optional) {
                moveActionStandard.setOptional(optional);
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

        BuildActionStandard buildActionStandard = new BuildActionStandard();
        moveActionDecorator.setOptional(false);
        buildActionStandard.setOptional(false);
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionDecorator);
        actions.add(buildActionStandard);
        return actions;
    }
    /**
     * Implements an effect where if you don't move up, you may build both before and after moving
     * @return actions available for this effect
     */
    public ArrayList<Action> createBuildBeforeMove() {

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        BuildActionStandard secondBuildActionStandard = new BuildActionStandard();

        RoundAction roundAction = new RoundAction() {

            private boolean actionLock = false;

            @Override
            public void doAction() throws IOException {
                moveActionStandard.setCantMoveUp(false);
            }

            @Override
            public void setActionLock(boolean actionLock) {
                this.actionLock = actionLock;
            }

            @Override
            public boolean isActionLock() {
                return actionLock;
            }
        };

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();
            boolean isOptional;

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel)) {
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
                    moveActionStandard.setCantMoveUp(true);
                    secondBuildActionStandard.setActionLock(true);
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
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                return buildActionStandard.getAvailableTilesForAction(worker);
            }

            @Override
            public boolean isOptional() {
                return isOptional;
            }

            @Override
            public void setOptional(boolean optional) {
                isOptional = optional;
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
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(roundAction);
        actions.add(buildActionDecorator);
        actions.add(moveActionStandard);
        actions.add(secondBuildActionStandard);
        return actions;
    }
    /**
     * Implements an effect where you can also win with five complete towers
     * @return actions available for this effect
     */
    public ArrayList<Action> createCompleteTowersObserver(){

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        BuildActionStandard buildActionStandard = new BuildActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        return actions;
    }
    /**
     * Implements an effect where you can build twice but the second build cannot be on the perimeter of the game board
     * @return actions available for this effect
     */
    public ArrayList<Action> createBuildTwiceNoPerimeter(){
        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                return (buildActionStandard.canBuild(worker, tileWhereBuild, newLevel) && !Grid.getGrid().isPerimeterTile(tileWhereBuild));
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
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
            public void setOptional(boolean optional) {
                buildActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                buildActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return buildActionStandard.isActionLock();
            }
        };

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        BuildActionStandard buildActionStandard = new BuildActionStandard();
        buildActionDecorator.setOptional(true);
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }
    /**
     * Implements an effect where if you move infinite times onto perimeter, you can move again
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanMoveMultipleTimesPerimeter(){

        ArrayList <Action> actions = new ArrayList<>();

        MoveActionDecorator moveActionDecorator = new MoveActionDecorator() {

            final MoveActionStandard moveActionStandard = new MoveActionStandard();
            int indexBuildAction = 2;
            boolean perimeterMove = false;

            @Override
            public void move(Worker worker, Tile tileWhereMove) {
                if(canMove(worker, tileWhereMove)) {
                    moveActionStandard.standardMove(worker, tileWhereMove);
                    if(Grid.getGrid().isPerimeterTile(tileWhereMove)) {
                       perimeterMove = true;
                       if(indexBuildAction>=actions.size()) indexBuildAction=2;
                       actions.add(indexBuildAction, this);
                       setOptional(true);
                       indexBuildAction++;
                    }
                    else perimeterMove = false;
                }
            }

            @Override
            public boolean canMove(Worker worker, Tile tileWhereMove) {
                return (moveActionStandard.canMove(worker, tileWhereMove));
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
            public void setOptional(boolean optional) {
                moveActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                if(perimeterMove) {
                    if (indexBuildAction == 3) setOptional(false);
                    if (indexBuildAction > 2) {
                        indexBuildAction--;
                        actions.remove(indexBuildAction);
                    }
                }
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

        RoundAction roundAction = new RoundAction() {
            boolean actionLock;

            @Override
            public void doAction() {
                moveActionDecorator.setOptional(false);
                int i=0;
                for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext(); ) {
                    Action actionToDelete = iterator.next();
                    if (actionToDelete.equals(moveActionDecorator) && i!=1)
                        iterator.remove();
                    i++;
                }
            }

            @Override
            public void setActionLock(boolean actionLock) {
                this.actionLock = actionLock;
            }

            @Override
            public boolean isActionLock() {
                return actionLock;
            }
        };

//        BuildActionStandard buildActionStandard = new BuildActionStandard();
//        actions.add(moveActionDecorator);
//        actions.add(buildActionStandard);

        actions.add(roundAction);
        actions.add(moveActionDecorator);
        actions.add(new BuildActionStandard());
        return actions;
    }
    /**
     * Implements an effect where workers can build below themselves
     * @return actions available for this effect
     */
    public ArrayList<Action> createCanBuildBelow(){

        BuildActionDecorator buildActionDecorator = new BuildActionDecorator() {

            final BuildActionStandard buildActionStandard = new BuildActionStandard();

            @Override
            public void build(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(canBuild(worker, tileWhereBuild, newLevel))
                    buildActionStandard.standardBuild(tileWhereBuild, newLevel);
            }

            @Override
            public void build(Worker worker, Tile tileWhereBuild) {
                build(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild, int newLevel) {
                if(isActionLock())
                    return false;
                if(buildActionStandard.canBuild(worker, tileWhereBuild, newLevel))
                    return true;
                return tileWhereBuild == worker.getPosition() && newLevel != 4;
            }

            @Override
            public boolean canBuild(Worker worker, Tile tileWhereBuild) {
                return canBuild(worker, tileWhereBuild, tileWhereBuild.getLevel()+1);
            }

            @Override
            public ArrayList<Tile> getAvailableTilesForAction(Worker worker) {
                ArrayList<Tile> tiles = buildActionStandard.getAvailableTilesForAction(worker, this);
                ArrayList<Tile> tilesWithCurrentTile = new ArrayList<>(tiles);
                tilesWithCurrentTile.add(worker.getPosition());
                return tilesWithCurrentTile;
            }

            @Override
            public boolean isOptional() {
                return buildActionStandard.isOptional();
            }

            @Override
            public void setOptional(boolean optional) {
                buildActionStandard.setOptional(optional);
            }

            @Override
            public void undo() {
                buildActionStandard.undo();
            }

            @Override
            public void setActionLock(boolean actionLock) {
                buildActionStandard.setActionLock(actionLock);
            }

            @Override
            public boolean isActionLock() {
                return buildActionStandard.isActionLock();
            }
        };

        MoveActionStandard moveActionStandard = new MoveActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(buildActionDecorator);
        return actions;
    }
    /**
     * Implements an effect where if any of your opponent's workers occupy lower neighboring spaces, replace them all with blocks and remove them from the game
     * @return actions available for this effect
     */
    public ArrayList<Action> createPetrifyOpponents() {
        RoundAction roundAction = new RoundAction() {
            boolean actionLock = false;

            @Override
            public void setActionLock(boolean actionLock) {
                this.actionLock = actionLock;
            }

            @Override
            public boolean isActionLock() {
                return actionLock;
            }

            @Override
            public void doAction() throws IOException {
                if(actionLock) return;
                PlayersManager playersManager = PlayersManager.getPlayersManager();
                Tile tempTile = playersManager.getCurrentWorker().getPosition();
                ArrayList<Tile> tiles = Grid.getGrid().getNeighbours(tempTile);
                if(tiles == null || tiles.size() == 0)
                    return;
                tiles.removeIf(tile -> tile.getLevel() >= tempTile.getLevel() || tile.isEmpty());
                ArrayList<Tile> tilesCopy = new ArrayList<>(tiles);
                for(Tile t : tiles) {
                    Worker worker = t.getWorker();
                    if(worker.getPlayerID() == playersManager.getCurrentWorker().getPlayerID())
                        tilesCopy.remove(t);
                }
                ArrayList<Worker> workers = (ArrayList<Worker>) tilesCopy.stream().map(Tile::getWorker).collect(Collectors.toList());
                for(Worker w : workers){
                    w.getPosition().setLevel(w.getPosition().getLevel() + 1);
                    w.getPosition().setWorker(null);
                    playersManager.deleteWorker(w);
                }
            }
        };
        MoveActionStandard moveActionStandard = new MoveActionStandard();
        BuildActionStandard buildActionStandard = new BuildActionStandard();
        ArrayList <Action> actions = new ArrayList<>();
        actions.add(moveActionStandard);
        actions.add(buildActionStandard);
        actions.add(roundAction);
        return actions;
    }
}
