package it.polimi.ingsw.Client.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

public class GUIBlock {

    private final int level;
    private final MeshView blockMesh;
    private final float workerHeight;
    private final float domeHeight;
    private final float domeXTranslation;
    private final float domeZTranslation;
    private final float nextBlockHeight;
    private final float nextBlockXTranslation;
    private final float domeScale;
    private final float nextBlockZTranslation;

    /**
     * Method used for the generation of all the blocks in the game
     * Every Case of the switch is a different building in the grid
     * @param level level of the block
     * @param tile position of the block
     * @param guiGridManager reference to the GridManager
     */
    public GUIBlock(int level, GUITile tile, GUIGridManager guiGridManager) {

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.web("f0f0f0"));
        int scaleSize = 6;
        this.level = level;

        ObjModelImporter objModelImporter = new ObjModelImporter();
        switch (level) {

            case 1 : {
                objModelImporter.read(getClass().getResource("/baseBlock.obj"));
                workerHeight = 26;
                nextBlockHeight = 21;
                domeHeight = 18;
                domeXTranslation = 0;
                domeZTranslation = -1;
                domeScale = 5f;
                nextBlockXTranslation = 0;
                nextBlockZTranslation = 0.8f;
                break;
            }

            case 2 : {
                objModelImporter.read(getClass().getResource("/mediumBlock.obj"));
                workerHeight = 26;
                nextBlockHeight = 18;
                domeHeight = 18;
                domeXTranslation = 0;
                domeZTranslation = -1;
                domeScale = 5.5f;
                nextBlockXTranslation = 0;
                nextBlockZTranslation = -1.5f;
                break;
            }

            case 3 : {
                objModelImporter.read(getClass().getResource("/highBlock.obj"));
                workerHeight = 22;
                nextBlockHeight = 0;
                domeHeight = 15;
                domeXTranslation = 0;
                domeZTranslation = 0;
                domeScale = scaleSize;
                nextBlockXTranslation = 0;
                nextBlockZTranslation = 0;
                break;
            }

            case 4 : {
                objModelImporter.read(getClass().getResource("/dome.obj"));
                material.setDiffuseColor(Color.web("0d63bf"));
            }

            default: {
                workerHeight = 0;
                nextBlockHeight = 0;
                nextBlockXTranslation = 0;
                nextBlockZTranslation = 0;
                domeHeight = 0;
                domeScale = scaleSize;
                domeXTranslation = 0;
                domeZTranslation = 0;
                break;
            }
        }

        MeshView[] mesh = objModelImporter.getImport();
        blockMesh = mesh[0];
        blockMesh.setMaterial(material);
        blockMesh.setOnMousePressed(mouseEvent -> guiGridManager.gridPosition(tile.getX(), tile.getY()));

        blockMesh.setOnMouseEntered(mouseEvent -> guiGridManager.getGuiRoundStage().changeGridCursor());
        blockMesh.setOnMouseExited(mouseEvent -> guiGridManager.getGuiRoundStage().resetCursor());

        blockMesh.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        blockMesh.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                guiGridManager.gridBuild(tile.getX(), tile.getY(), Integer.parseInt(db.getString()));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        blockMesh.setScaleX(scaleSize);
        blockMesh.setScaleY(scaleSize);
        blockMesh.setScaleZ(scaleSize);
    }

    public int getLevel() {
        return level;
    }

    public float getWorkerHeight() {
        return workerHeight;
    }

    public float getDomeXTranslation() {
        return domeXTranslation;
    }

    public float getDomeZTranslation() {
        return domeZTranslation;
    }

    public float getNextBlockHeight() {
        return nextBlockHeight;
    }

    public float getDomeHeight() {
        return domeHeight;
    }

    public float getDomeScale() {
        return domeScale;
    }

    public float getNextBlockXTranslation() {
        return nextBlockXTranslation;
    }

    public float getNextBlockZTranslation() {
        return nextBlockZTranslation;
    }

    public MeshView getBlockMesh() {
        return blockMesh;
    }
}
