    package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.BuildDecisionEvent;
import it.polimi.ingsw.Events.Client.MoveDecisionEvent;
import it.polimi.ingsw.Events.Client.PositioningEvent;
import it.polimi.ingsw.Model.ActionType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIRoundStage {

    final double rotationSpeed = 100;
    final double zoomSpeed = 10;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    final DoubleProperty rotationVelocity = new SimpleDoubleProperty();
    final DoubleProperty zoomVelocity = new SimpleDoubleProperty();
    final LongProperty lastUpdateTime = new SimpleLongProperty();
    BuildingsController buildingsController;
    ActionType selectedActionType;
    GUIStagesManager stagesManager;

    private final int maxFOV = 40;
    private final int minFOV = 25;
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    GUIGridManager guiGridManager;

    public void setSelectedActionType(ActionType selectedActionType) {
        this.selectedActionType = selectedActionType;
    }

    public GUIGridManager getGuiGridManager() {
        return guiGridManager;
    }

    public GUIStagesManager getStagesManager() {
        return stagesManager;
    }

    public void start(Stage stage, GUIStagesManager stagesManager) {

        this.stagesManager = stagesManager;

        SmartGroup group = new SmartGroup();
        guiGridManager = new GUIGridManager(group, this);

        SubScene subScene = new SubScene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = createCamera();
        subScene.setCamera(camera);
        stage.setResizable(true);

        guiGridManager.createGrid();
        gridAnimations(group, camera);
        eventHandler(stage);
        try { buildingsImages(stage, subScene); }
        catch (IOException e) { e.printStackTrace(); }
        stage.show();
    }

    public void showActions() {

    }

    public void gridBuild(int x, int y, int buildLevel) {
        stagesManager.send(new BuildDecisionEvent(x, y, buildLevel));
    }

    public void gridPosition(int x, int y) {
        if(selectedActionType==null) {
            stagesManager.send(new PositioningEvent(x, y));
            return;
        }
        switch (selectedActionType) {
            case MOVE: {
                stagesManager.send(new MoveDecisionEvent(x, y));
                break;
            }
            case BUILD: {
                stagesManager.send(new BuildDecisionEvent(x, y));
            }
        }
    }

    public void readMessage(String message) {
        System.out.println(message);
    }

    public void readError(String error) {
        System.out.println(error);
    }

    private void buildingsImages(Stage stage, SubScene subScene) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/buildings.fxml"));
        Parent root = loader.load();
        buildingsController = loader.getController();

        Rectangle rectangle = new Rectangle(90, 20, Color.FORESTGREEN);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        ImageView baseBlock = buildingsController.getBaseBlock();
        ImageView mediumBlock = buildingsController.getMediumBlock();
        ImageView highBlock = buildingsController.getHighBlock();
        ImageView dome = buildingsController.getDome();
        BorderPane pane = buildingsController.getPane();
        pane.setCenter(subScene);
        subScene.widthProperty().bind(pane.widthProperty());
        subScene.heightProperty().bind(pane.heightProperty());

        baseBlock.setOnDragDetected(event -> {
            Dragboard db = baseBlock.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(new Color(0.13, 0.55, 0.13, 1));
            db.setDragView(baseBlock.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString("1");
            db.setContent(content);
            event.consume();
        });

        mediumBlock.setOnDragDetected(event -> {
            Dragboard db = baseBlock.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(new Color(0.13, 0.55, 0.13, 1));
            db.setDragView(mediumBlock.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString("2");
            db.setContent(content);
            event.consume();
        });

        highBlock.setOnDragDetected(event -> {
            Dragboard db = baseBlock.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(new Color(0.13, 0.55, 0.13, 1));
            db.setDragView(highBlock.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString("3");
            db.setContent(content);
            event.consume();
        });

        dome.setOnDragDetected(event -> {
            Dragboard db = baseBlock.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(new Color(0.13, 0.55, 0.13, 1));
            db.setDragView(dome.snapshot(snapshotParameters, null));
            ClipboardContent content = new ClipboardContent();
            content.putString("4");
            db.setContent(content);
            event.consume();
        });
        stage.setScene(new Scene(root,WIDTH, HEIGHT));
        stage.setResizable(true);
    }

    private PerspectiveCamera createCamera() {
        int cameraHeight = -500;
        int cameraDepth = -700;
        int cameraRotation = -35;

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateYProperty().set(cameraHeight);
        camera.translateZProperty().set(cameraDepth);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(cameraRotation);
        return camera;
    }

    private void gridAnimations(Group group, PerspectiveCamera camera) {

        final AnimationTimer gridRotation = new AnimationTimer() {

            @Override
            public void handle(long timestamp) {

                if (lastUpdateTime.get() > 0) {
                    final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
                    final double deltaRotation = elapsedSeconds * rotationVelocity.get();
                    final double oldRotation = group.getRotate();
                    final double newRotation = oldRotation + deltaRotation;
                    final double deltaZoom = elapsedSeconds * zoomVelocity.get();
                    final double oldZoom = camera.getFieldOfView();
                    final double newZoom = Math.max(minFOV, Math.min(maxFOV, oldZoom + deltaZoom));
                    group.setRotationAxis(Rotate.Y_AXIS);
                    group.setRotate(newRotation);
                    camera.setFieldOfView(newZoom);
                }
                lastUpdateTime.set(timestamp);
            }
        };
        gridRotation.start();
    }

    private void eventHandler(Stage stage) {

        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W: { if(!sPressed) zoomVelocity.set(-zoomSpeed); wPressed = true; break;}
                case A: { if(!dPressed) rotationVelocity.set(rotationSpeed); aPressed = true; break; }
                case D: { if(!aPressed) rotationVelocity.set(-rotationSpeed); dPressed = true; break; }
                case S: { if(!wPressed) zoomVelocity.set(zoomSpeed); sPressed = true; break;} }
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W: { wPressed = false; zoomVelocity.set(0); break; }
                case S: { sPressed = false; zoomVelocity.set(0); break;}
                case A: { aPressed = false;  rotationVelocity.set(0); break; }
                case D: { dPressed = false; rotationVelocity.set(0); break; }
            }
        });
    }
}
