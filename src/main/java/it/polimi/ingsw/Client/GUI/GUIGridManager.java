package it.polimi.ingsw.Client.GUI;

import javafx.application.Platform;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.PointLight;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GUIGridManager {

    private GUIGrid grid;
    private final Group group;
    private final ArrayList<GUIWorker> workers = new ArrayList<>();
    GUIRoundStage guiRoundStage;

    public GUIGridManager(Group group, GUIRoundStage guiRoundStage) {
        this.group = group;
        this.guiRoundStage = guiRoundStage;
    }

    public void gridPosition(int x, int y) {
        guiRoundStage.gridPosition(x, y);
    }

    public GUIRoundStage getGuiRoundStage() {
        return guiRoundStage;
    }

    public void gridBuild(int x, int y, int buildLevel) { guiRoundStage.gridBuild(x, y, buildLevel); }

    public GUIGrid getGrid() {
        return grid;
    }

    public void setGrid(GUIGrid grid) {
        this.grid = grid;
    }

    public GUIWorker createWorker(String playerName, int workerID, int x, int y, Color color) {
        GUITile tile = grid.getTile(x, y);
        GUIWorker guiWorker = new GUIWorker(playerName, workerID, color, guiRoundStage);
        workers.add(guiWorker);
        guiWorker.setGuiTile(tile);
        tile.setGUIWorker(guiWorker);
        group.getChildren().add(guiWorker.getWorkerMesh());
        return guiWorker;
    }

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

    public void deleteWorker(GUIWorker g) {
        g.getGuiTile().setGUIWorker(null);
        group.getChildren().remove(g.getWorkerMesh());
    }

    public void setWorkerNull(int x, int y) {
        GUITile tile = grid.getTile(x, y);
        tile.setGUIWorker(null);
    }

    private GUIWorker getWorker(String playerName, int workerID) {
        for(GUIWorker w : workers) if(w.getPlayerName().equals(playerName) && w.getWorkerID()==workerID) return w;
        return null;
    }

    public void highLight(int x, int y) {
        grid.getTile(x,y).highLight();
    }

    public void deColor() {
        for(GUITile t : grid.getTiles())
            t.deColor();
    }

    public void createGrid() {
        float gridSize = 280;
        int tileHeight = 6;

        setupLight();

        grid = new GUIGrid(gridSize, tileHeight, this);
        group.getChildren().add(grid.getGridInvisible());
        group.getChildren().addAll(grid.getMeshes());
        group.getChildren().add(grid.getSea());
        group.getChildren().addAll(grid.getTiles().stream().map(GUITile::getTileMesh).collect(Collectors.toList()));
        group.getChildren().addAll(grid.getTiles().stream().map(GUITile::getTileMeshAvailable).collect(Collectors.toList()));
    }

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
