package it.polimi.ingsw.Client.GUI;

import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

import java.util.ArrayList;

public class GUITile {

    private final int x;
    private final int y;
    private final Box tileMesh;
    private final Box tileMeshAvailable;
    private GUIWorker guiWorker;
    private final float workerHeight;
    private final ArrayList<GUIBlock> levels = new ArrayList<>();
    PhongMaterial transparentMaterial = new PhongMaterial();
    PhongMaterial invisibleMaterial = new PhongMaterial();
    GUIGridManager guiGridManager;

    public GUITile(int x, int y, float size, float height, GUIGridManager guiGridManager) {

        this.guiGridManager = guiGridManager;

        workerHeight = height - 22;
        this.x = x;
        this.y = y;

        tileMesh = new Box(size, size, height);
        invisibleMaterial.setDiffuseColor(Color.TRANSPARENT);
        tileMesh.setMaterial(invisibleMaterial);
        tileMeshAvailable = new Box(size-10, size-10, height-5);
        tileMeshAvailable.setMaterial(invisibleMaterial);
    }

    public Box getTileMesh() {
        return tileMesh;
    }

    public Box getTileMeshAvailable() {
        return tileMeshAvailable;
    }

    public void highLight() {
        transparentMaterial.setDiffuseColor(Color.web("#20d63299"));
        tileMeshAvailable.setMaterial(transparentMaterial);
    }

    public void deColor() {
        transparentMaterial.setDiffuseColor(Color.TRANSPARENT);
        tileMeshAvailable.setMaterial(transparentMaterial);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GUIWorker getGUIWorker() {
        return guiWorker;
    }

    public void moveWorker(GUIWorker worker) {

        if(levels.size()==0) {
            worker.getWorkerMesh().getTransforms().addAll(tileMesh.getTransforms());
            worker.getWorkerMesh().setTranslateX(tileMesh.getTranslateX());
            worker.getWorkerMesh().setTranslateY(tileMesh.getTranslateY());
            worker.getWorkerMesh().setTranslateZ(tileMesh.getTranslateZ());
            worker.getWorkerMesh().setTranslateY(workerHeight);
            return;
        }
        GUIBlock block = levels.get(levels.size()-1);
        MeshView blockMesh = block.getBlockMesh();
        worker.getWorkerMesh().getTransforms().addAll(blockMesh.getTransforms());
        worker.getWorkerMesh().setTranslateX(blockMesh.getTranslateX());
        worker.getWorkerMesh().setTranslateY(blockMesh.getTranslateY());
        worker.getWorkerMesh().setTranslateZ(blockMesh.getTranslateZ());
        worker.getWorkerMesh().setTranslateY(guiWorker.getWorkerMesh().getTranslateY() - block.getWorkerHeight());
    }

    public void setGUIWorker(GUIWorker guiWorker) {

        this.guiWorker = guiWorker;
        if(guiWorker==null) return;
        guiWorker.setGuiTile(this);
        moveWorker(guiWorker);
    }

    public int getLastLevel() {
        if(levels.size()==0) return 0;
        return levels.get(levels.size()-1).getLevel();
    }

    public GUIBlock getLastBlock() {
        if(levels.size()==0) return null;
        return levels.get(levels.size()-1);
    }

    public MeshView build(int level) {

        int baseBlockHeight = 10;

        GUIBlock guiBlock = new GUIBlock(level, this, guiGridManager);

        if(levels.size()==0) {
            guiBlock.getBlockMesh().setTranslateX(tileMesh.getTranslateX());
            guiBlock.getBlockMesh().setTranslateY(tileMesh.getTranslateY()- baseBlockHeight);
            guiBlock.getBlockMesh().setTranslateZ(tileMesh.getTranslateZ());
        }

        else if(level==4) {
            guiBlock.getBlockMesh().setTranslateX(levels.get(levels.size()-1).getBlockMesh().getTranslateX() + levels.get(levels.size()-1).getDomeXTranslation());
            guiBlock.getBlockMesh().setTranslateY(levels.get(levels.size()-1).getBlockMesh().getTranslateY() - levels.get(levels.size()-1).getDomeHeight());
            guiBlock.getBlockMesh().setTranslateZ(levels.get(levels.size()-1).getBlockMesh().getTranslateZ() + levels.get(levels.size()-1).getDomeZTranslation());
            guiBlock.getBlockMesh().setScaleX(levels.get(levels.size()-1).getDomeScale());
            guiBlock.getBlockMesh().setScaleY(levels.get(levels.size()-1).getDomeScale());
            guiBlock.getBlockMesh().setScaleZ(levels.get(levels.size()-1).getDomeScale());
        }

        else {
            guiBlock.getBlockMesh().setTranslateX(levels.get(levels.size()-1).getBlockMesh().getTranslateX() + levels.get(levels.size()-1).getNextBlockXTranslation());
            guiBlock.getBlockMesh().setTranslateY(levels.get(levels.size()-1).getBlockMesh().getTranslateY() - levels.get(levels.size()-1).getNextBlockHeight());
            guiBlock.getBlockMesh().setTranslateZ(levels.get(levels.size()-1).getBlockMesh().getTranslateZ() + levels.get(levels.size()-1).getNextBlockZTranslation());
        }

        levels.add(guiBlock);
        if(guiWorker!=null) moveWorker(guiWorker);
        return guiBlock.getBlockMesh();
    }

    public void deleteLastLevel() {
        levels.remove(levels.size()-1);
        if(guiWorker!=null) moveWorker(guiWorker);
    }
}