package it.polimi.ingsw.Client.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class GUIGrid {

    private final Box sea;
    private final Box gridInvisible;
    private final MeshView gridBase;
    private ArrayList<GUITile> tiles;
    private final float size;
    private final int tileHeight;
    private final GUIGridManager guiGridManager;

    /**
     * Creates the 3D grid
     * @param size size of the boxes
     * @param tileHeight height of the boxes
     * @param guiGridManager handles all the effects of the grid
     */
    public GUIGrid (float size, int tileHeight, GUIGridManager guiGridManager) {

        this.guiGridManager = guiGridManager;

        PhongMaterial seaMaterial = new PhongMaterial();
        seaMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/sea.jpg")));
        sea = new Box(3500, 3500, 1);
        sea.setRotationAxis(Rotate.X_AXIS);
        sea.setRotate(90);
        sea.setMaterial(seaMaterial);

        int scaleSize = 137;

        PhongMaterial bordersMaterial = new PhongMaterial();
        bordersMaterial.setDiffuseColor(Color.GREEN);
        PhongMaterial invisibleMaterial = new PhongMaterial();
        invisibleMaterial.setDiffuseColor(Color.TRANSPARENT);

        MeshView[] m;

        PhongMaterial baseMaterial = new PhongMaterial();
        baseMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/isle.png")));
        ObjModelImporter objModelImporter = new ObjModelImporter();
        objModelImporter.read(getClass().getResource("/isle.obj"));
        m = objModelImporter.getImport();
        gridBase = m[0];
        gridBase.setMaterial(baseMaterial);
        gridBase.setRotationAxis(Rotate.X_AXIS);
        gridBase.setRotate(0);
        gridBase.setScaleX(scaleSize);
        gridBase.setScaleY(scaleSize);
        gridBase.setScaleZ(scaleSize);

        gridInvisible = new Box(size, size, 0);
        gridInvisible.setRotationAxis(Rotate.X_AXIS);
        gridInvisible.setRotate(90);
        gridInvisible.setMaterial(invisibleMaterial);
        gridInvisible.setTranslateY(3.5);

        this.size = size;
        this.tileHeight = tileHeight;
        createGrid();
    }

    public ArrayList<MeshView> getMeshes() {
        ArrayList<MeshView> meshes = new ArrayList<>();
        meshes.add(gridBase);
        return meshes;
    }

    public Box getSea() {
        return sea;
    }

    public Box getGridInvisible() {
        return gridInvisible;
    }

    public ArrayList<GUITile> getTiles() {
        return tiles;
    }

    /**
     * Creates the whole grid
     */
    private void createGrid() {
        tiles = new ArrayList<>();
        for(int y = 1; y< 5 +1; y++) for(int x = 1; x< 5 +1; x++) createTile(x, y);
    }

    /**
     * Creates the tile in the grid
     * @param x param. x of the grid
     * @param y param. y of the grid
     */
    private void createTile(int x, int y) {

        GUITile guiTile = new GUITile(x,y,size/5, tileHeight, guiGridManager);
        Box tileMesh = guiTile.getTileMesh();
        Box availableTilesMesh = guiTile.getTileMeshAvailable();

        tileMesh.setRotationAxis(Rotate.X_AXIS);
        tileMesh.setRotate(gridInvisible.getRotate());
        tileMesh.setTranslateY(gridInvisible.getTranslateY() - (float) tileHeight/2);
        tileMesh.setTranslateX((x-3)*size/5);
        tileMesh.setTranslateZ((3-y)*size/5);

        availableTilesMesh.setRotationAxis(Rotate.X_AXIS);
        availableTilesMesh.setRotate(gridInvisible.getRotate());
        availableTilesMesh.setTranslateY(gridInvisible.getTranslateY() + 0.43 - (float) tileHeight/2);
        availableTilesMesh.setTranslateX((x-3)*size/5);
        availableTilesMesh.setTranslateZ((3-y)*size/5);

        tileMesh.setOnMousePressed(mouseEvent -> guiGridManager.gridPosition(x, y));

        tileMesh.setOnMouseEntered(mouseEvent -> guiGridManager.getGuiRoundStage().changeGridCursor());

        tileMesh.setOnMouseExited(mouseEvent -> guiGridManager.getGuiRoundStage().resetCursor());

        tileMesh.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        tileMesh.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                guiGridManager.gridBuild(x, y, Integer.parseInt(db.getString()));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        tiles.add(guiTile);
    }

    public GUITile getTile(int x, int y) {
        for (GUITile t : tiles) if(t.getX()==x && t.getY()==y) return t;
        return null;
    }
}
