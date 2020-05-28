package it.polimi.ingsw.Client.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.Events.Client.SelectionEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

public class GUIWorker {

    ObjModelImporter importer = new ObjModelImporter();
    private final MeshView workerMesh;
    private GUITile guiTile;
    private final String playerName;
    private final int workerID;
    GUIRoundStage guiRoundStage;

    public GUIWorker(String playerName, int workerID, Color color, GUIRoundStage guiRoundStage) {

        this.guiRoundStage = guiRoundStage;

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        int scaleSize = 16;

        String basePath;
        if(workerID==1) basePath = getClass().getResource("/maleWorker.obj").getPath();
        else basePath = getClass().getResource("/femaleWorker.obj").getPath();

        MeshView[] mesh;
        importer.read("file://" + basePath);
        mesh = importer.getImport();
        workerMesh = mesh[0];
        workerMesh.setMaterial(material);
        workerMesh.setScaleX(scaleSize);
        workerMesh.setScaleY(scaleSize);
        workerMesh.setScaleZ(scaleSize);
        workerMesh.setOnMousePressed(mouseEvent -> guiRoundStage.getStagesManager().send(new SelectionEvent(workerID, playerName)));
        workerMesh.setOnMouseEntered(mouseEvent -> guiRoundStage.changeWorkerCursor());
        workerMesh.setOnMouseDragExited(mouseDragEvent -> guiRoundStage.resetCursor());

        this.playerName = playerName;
        this.workerID = workerID;
    }

    public GUITile getGuiTile() {
        return guiTile;
    }

    public void setGuiTile(GUITile guiTile) {
        this.guiTile = guiTile;
    }

    public MeshView getWorkerMesh() {
        return workerMesh;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWorkerID() {
        return workerID;
    }
}
