package it.polimi.ingsw.Client.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.Events.Client.SelectionEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

public class GUIWorker {

    private final MeshView workerMesh;
    private GUITile guiTile;
    private final String playerName;
    private final int workerID;

    public GUIWorker(String playerName, int workerID, Color color, GUIRoundStage guiRoundStage) {

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        int scaleSize = 16;

        MeshView[] mesh;
        ObjModelImporter importer = new ObjModelImporter();
        if(workerID==1) importer.read(getClass().getResource("/maleWorker.obj"));
        else importer.read(getClass().getResource("/femaleWorker.obj"));
        mesh = importer.getImport();
        workerMesh = mesh[0];
        workerMesh.setMaterial(material);
        workerMesh.setScaleX(scaleSize);
        workerMesh.setScaleY(scaleSize);
        workerMesh.setScaleZ(scaleSize);
        workerMesh.setOnMousePressed(mouseEvent -> guiRoundStage.getStagesManager().send(new SelectionEvent(workerID, playerName)));
        workerMesh.setOnMouseEntered(mouseEvent -> guiRoundStage.changeWorkerCursor());
        workerMesh.setOnMouseExited(mouseDragEvent -> guiRoundStage.resetCursor());

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
