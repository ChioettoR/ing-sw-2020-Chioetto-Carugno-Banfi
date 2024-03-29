package it.polimi.ingsw.Client.GUI;

import javafx.application.Platform;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIGridManager {

    private GUIGrid grid;
    private final Group group;
    private final ArrayList<GUIWorker> workers = new ArrayList<>();
    private final GUIRoundStage guiRoundStage;

    public GUIGridManager(Group group, GUIRoundStage guiRoundStage) {
        this.group = group;
        this.guiRoundStage = guiRoundStage;
    }

    /**
     * Called when someone selects a tile
     * @param x x coord. of the tile
     * @param y x coord. of the tile
     */
    public void gridPosition(int x, int y) {
        guiRoundStage.gridPosition(x, y);
    }

    public GUIRoundStage getGuiRoundStage() {
        return guiRoundStage;
    }

    /**
     * Called when somebody tries to build into a tile with a certain level
     * @param x x coord. of the tile
     * @param y y coord. of the tile
     * @param buildLevel level of the building
     */
    public void gridBuild(int x, int y, int buildLevel) { guiRoundStage.gridBuild(x, y, buildLevel); }

    public GUIGrid getGrid() {
        return grid;
    }

    public void setGrid(GUIGrid grid) {
        this.grid = grid;
    }

    /**
     * Creates the worker
     * @param playerName name of the worker's owner
     * @param workerID id of the worker
     * @param x param. x of the grid
     * @param y param. x of the grid
     * @param color color of the player's worker
     * @return returns the worker created
     */
    public GUIWorker createWorker(String playerName, int workerID, int x, int y, Color color) {
        GUITile tile = grid.getTile(x, y);
        GUIWorker guiWorker = new GUIWorker(playerName, workerID, color, guiRoundStage);
        workers.add(guiWorker);
        guiWorker.setGuiTile(tile);
        tile.setGUIWorker(guiWorker);
        group.getChildren().add(guiWorker.getWorkerMesh());
        return guiWorker;
    }

    /**
     * Positions a worker into the selected tiles; if the worker doesn't exist, creates a new worker and positions that worker there
     * @param playerName name of the player
     * @param workerID id of the worker
     * @param x x coord. of the tile
     * @param y y coord. of the tile
     */
    public void setWorker(String playerName, int workerID, int x, int y) {
        new Thread( () -> Platform.runLater( () -> {
            GUITile tile = grid.getTile(x, y);
            GUIWorker guiWorker = getWorker(playerName, workerID);
            if (guiWorker == null) {
                Color color = guiRoundStage.getStagesManager().getGuiPlayersManager().getPlayer(playerName).getColor();
                guiWorker = createWorker(playerName, workerID, x, y, color);
            }
            else guiWorker.getGuiTile().setGUIWorker(null);
            tile.setGUIWorker(guiWorker);
        })).start();
    }

    /**
     * Deletes the worker from the game
     * @param g worker
     */
    public void deleteWorker(GUIWorker g) {
        g.getGuiTile().setGUIWorker(null);
        group.getChildren().remove(g.getWorkerMesh());
        workers.remove(g);
    }

    /**
     * Removes the worker from that position, if that worker exists
     * @param x x coord. of the tile
     * @param y y coord. of the tile
     */
    public void setWorkerNull(int x, int y) {
        GUITile tile = grid.getTile(x, y);
        tile.setGUIWorker(null);
    }

    private GUIWorker getWorker(String playerName, int workerID) {
        for(GUIWorker w : workers) if(w.getPlayerName().equals(playerName) && w.getWorkerID()==workerID) return w;
        return null;
    }

    /**
     * Highlights the tile on the grid
     * @param x param. x of the grid
     * @param y param. y of the grid
     */
    public void highLight(int x, int y) {
        grid.getTile(x,y).highLight();
    }

    /**
     * Discolours the tiles on the grid
     */
    public void deColor() {
        for(GUITile t : grid.getTiles())
            t.deColor();
    }

    /**
     * Creates the grid
     */
    public void createGrid() {
        float gridSize = 280;
        int tileHeight = 10;

        setupLight();

        grid = new GUIGrid(gridSize, tileHeight, this);
        group.getChildren().add(grid.getGridInvisible());
        group.getChildren().addAll(grid.getMeshes());
        group.getChildren().add(grid.getSea());
        group.getChildren().addAll(grid.getTiles().stream().map(GUITile::getTileMesh).collect(Collectors.toList()));
        group.getChildren().addAll(grid.getTiles().stream().map(GUITile::getTileMeshAvailable).collect(Collectors.toList()));
    }

    /**
     * Sets up the whole lights in the game
     */
    public void setupLight() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(Color.web("#8a8a8a"));
        PointLight pointLight = new PointLight();
        pointLight.setTranslateY(-1000);
        pointLight.setColor(Color.GRAY);
        pointLight.setTranslateX(40);
        group.getChildren().add(ambientLight);
        group.getChildren().add(pointLight);
    }

    /**
     * Builds the constructions in the tile
     * @param level level of the building
     * @param x param. x of the tile
     * @param y param. y of the tile
     */
    public void build(int level, int x, int y) {
        GUITile tile = grid.getTile(x, y);

        if(tile.getLastLevel()==level) return;
        if(tile.getLastLevel()>level) {
            while (tile.getLastLevel()>level) {
                group.getChildren().remove(tile.getLastBlock().getBlockMesh());
                tile.deleteLastLevel();
            }
            return;
        }
        MeshView block = tile.build(level);
        group.getChildren().add(block);
    }
}
