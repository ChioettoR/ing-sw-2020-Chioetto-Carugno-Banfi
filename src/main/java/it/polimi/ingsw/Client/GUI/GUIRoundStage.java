package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Events.Client.*;
import it.polimi.ingsw.Model.ActionType;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    GUIColorDecoder guiColorDecoder = new GUIColorDecoder();
    BuildingsController buildingsController;
    ActionType selectedActionType;
    GUIStagesManager stagesManager;
    GUIPlayersManager guiPlayersManager;
    GUICards guiCards;
    HashMap<ActionType, String> buttonsStyle = new HashMap<>();
    boolean buttonTranslated = false;
    Scene scene;
    int timer = 3;

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

    public ActionType getSelectedActionType() {
        return selectedActionType;
    }

    public void start(Stage stage) {

        stage.setWidth(675);
        stage.setHeight(675);
        stage.setMinWidth(675);
        stage.setMinHeight(675);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public void changeGridCursor() {
        scene.setCursor(Cursor.HAND);
    }

    public void changeWorkerCursor() {
        scene.setCursor(Cursor.CROSSHAIR);
    }

    public void resetCursor() {
        scene.setCursor(Cursor.DEFAULT);
    }

    public void gridBuild(int x, int y, int buildLevel) {
        stagesManager.send(new BuildDecisionEvent(x, y, buildLevel));
    }

    /**
     * Handles the selected action to the correct event for that action
     * @param x param. x of the grid
     * @param y param. y of the grid
     */
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
        buildingsController.getMessageText().setVisible(true);
        buildingsController.getMessageText().setText(message);
        buildingsController.getErrorText().setVisible(false);
    }

    public void readError(String error) {
        buildingsController.getErrorText().setVisible(true);
        buildingsController.getErrorText().setText(error);
        buildingsController.getMessageText().setVisible(false);
        timerToCancel();
    }

    /**
     * Images for the buildings
     * @param stage stage of the match
     * @param subScene subscene of the grid
     * @throws IOException when socket closes
     */
    private void buildingsImages(Stage stage, SubScene subScene) throws IOException {

        initializeButtonsStyle();

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
        subScene.toBack();

        StackPane stackPane = buildingsController.getStackPane();
        stackPane.toFront();

        //playersCardShow();

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
        scene = new Scene(root, WIDTH, HEIGHT);
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

    /**
     * Handles the animations of the grid (Zoom, Rotations)
     * @param group
     * @param camera
     */
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

    public void resetButtons() {
        buildingsController.getLeftButton().setDisable(true);
        buildingsController.getLeftButton().setVisible(false);
        buildingsController.getRightButton().setDisable(true);
        buildingsController.getRightButton().setVisible(false);
        buildingsController.getCenterButton().setDisable(true);
        buildingsController.getCenterButton().setVisible(false);
    }

    private void activeButtonCenter(ActionType actionType, boolean onlyCenter) {
        if(onlyCenter) {
            buildingsController.getLeftButton().setDisable(true);
            buildingsController.getLeftButton().setVisible(false);
            buildingsController.getRightButton().setDisable(true);
            buildingsController.getRightButton().setVisible(false);
        }
        buildingsController.getCenterButton().setDisable(false);
        buildingsController.getCenterButton().setVisible(true);
        buildingsController.getCenterButton().getStylesheets().clear();
        buildingsController.getCenterButton().getStylesheets().add(getStyle(actionType));
        buildingsController.getCenterButton().setOnAction(event -> sendActionToClient(actionType.toString()));
    }

    public void showActions(ArrayList<String> actions) {
//        buildingsController.getMessageText().setText("");
//        buildingsController.getErrorText().setText("");
        ArrayList<ActionType> actionTypes = new ArrayList<>();
        for(String action : actions) actionTypes.add(ActionType.valueOf(action));
        if(actionTypes.size()==1) activeButtonCenter(actionTypes.get(0), true);
        else if(actionTypes.size()==2) activeBorderButtons(actionTypes.get(0), actionTypes.get(1), true);
        else if(actionTypes.size()==3) {
            activeBorderButtons(actionTypes.get(0), actionTypes.get(2), false);
            activeButtonCenter(actionTypes.get(1), false);
        }
    }

    private void activeBorderButtons(ActionType actionTypeLeft, ActionType actionTypeRight, boolean onlyBorders) {
        int buttonTranslation = 50;
        buildingsController.getLeftButton().setDisable(false);
        buildingsController.getLeftButton().setVisible(true);
        buildingsController.getRightButton().setDisable(false);
        buildingsController.getRightButton().setVisible(true);
        buildingsController.getRightButton().getStylesheets().clear();
        buildingsController.getRightButton().getStylesheets().add(getStyle(actionTypeRight));
        buildingsController.getRightButton().setOnAction(event -> sendActionToClient(actionTypeRight.toString()));
        buildingsController.getLeftButton().getStylesheets().clear();
        buildingsController.getLeftButton().getStylesheets().add(getStyle(actionTypeLeft));
        buildingsController.getLeftButton().setOnAction(event -> sendActionToClient(actionTypeLeft.toString()));
        if(onlyBorders) {
            buildingsController.getCenterButton().setDisable(true);
            buildingsController.getCenterButton().setVisible(false);
            if(!buttonTranslated) {
                buildingsController.getLeftButton().setTranslateX(buttonTranslation);
                buildingsController.getRightButton().setTranslateX(-buttonTranslation);
                buttonTranslated = true;
            }
        }
        else {
            if(buttonTranslated) {
                buildingsController.getLeftButton().setTranslateX(-buttonTranslation);
                buildingsController.getRightButton().setTranslateX(+buttonTranslation);
                buttonTranslated = false;
            }
        }
    }

    public void sendActionToClient(String action) {
        buildingsController.getCenterButton().setDisable(true);
        buildingsController.getCenterButton().setVisible(false);
        buildingsController.getLeftButton().setDisable(true);
        buildingsController.getLeftButton().setVisible(false);
        buildingsController.getRightButton().setDisable(true);
        buildingsController.getRightButton().setVisible(false);
        stagesManager.send(new ActionSelectEvent(action));
    }

    private void initializeButtonsStyle() {
        buttonsStyle.put(ActionType.MOVE,"styleButtonMove.css");
        buttonsStyle.put(ActionType.BUILD,"styleButtonBuild.css");
        buttonsStyle.put(ActionType.CONFIRM,"styleButtonConfirm.css");
        buttonsStyle.put(ActionType.UNDO,"styleButtonUndo.css");
        buttonsStyle.put(ActionType.ENDROUND,"styleButtonEndRound.css");
    }

    private String getStyle(ActionType action) {
        return buttonsStyle.get(action);
    }

    /**
     * Shows the correct card to his owner with the color chosen
     */
    private void playersCardShow() {
        ArrayList<String> playerNames;
        GUIPlayer player1;
        GUIPlayer player2;
        GUIPlayer player3;
        guiPlayersManager = stagesManager.getGuiPlayersManager();
        playerNames = guiPlayersManager.getNames();
        if(playerNames.size() == 3) {
            setVisibleAll();
            buildingsController.getCloseInfoButton().setOnAction(event -> closeInfo());
            player1 = guiPlayersManager.getPlayer(playerNames.get(0));
            buildingsController.getImageRound1().setImage(guiCards.getFullImage(player1.getCardName()));
            buildingsController.getNameText1().setText(player1.getName());
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            buildingsController.getButtonInfo1().setOnAction(event -> setInfo(player1.getCardName()));
            player2 = guiPlayersManager.getPlayer(playerNames.get(1));
            buildingsController.getImageRound2().setImage(guiCards.getFullImage(player2.getCardName()));
            buildingsController.getNameText2().setText(player2.getName());
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            buildingsController.getButtonInfo2().setOnAction(event -> setInfo(player2.getCardName()));
            player3 = guiPlayersManager.getPlayer(playerNames.get(2));
            buildingsController.getImageRound3().setImage(guiCards.getFullImage(player3.getCardName()));
            buildingsController.getNameText3().setText(player3.getName());
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            buildingsController.getButtonInfo3().setOnAction(event -> setInfo(player3.getCardName()));
        }
        else {
            setVisibleTwo();
            buildingsController.getCloseInfoButton().setOnAction(event -> closeInfo());
            player1 = guiPlayersManager.getPlayer(playerNames.get(0));
            buildingsController.getImageRound1().setImage(guiCards.getFullImage(player1.getCardName()));
            buildingsController.getNameText1().setText(player1.getName());
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            buildingsController.getButtonInfo1().setOnAction(event -> setInfo(player1.getCardName()));
            player2 = guiPlayersManager.getPlayer(playerNames.get(1));
            buildingsController.getImageRound2().setImage(guiCards.getFullImage(player2.getCardName()));
            buildingsController.getNameText2().setText(player2.getName());
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            buildingsController.getButtonInfo2().setOnAction(event -> setInfo(player2.getCardName()));
        }
    }

    private void setVisibleAll() {
        setVisibleTwo();
        buildingsController.getImageRound3().setVisible(true);
        buildingsController.getNameBar3().setVisible(true);
        buildingsController.getNameText3().setVisible(true);
        buildingsController.getFrame3().setVisible(true);
        buildingsController.getButtonInfo3().setVisible(true);
    }

    private void setVisibleTwo() {
        buildingsController.getRightPane().setVisible(true);
        buildingsController.getBorderPaneCards().setVisible(true);
        buildingsController.getBorderPaneCards().setPickOnBounds(false);

        buildingsController.getImageRound1().setVisible(true);
        buildingsController.getNameBar1().setVisible(true);
        buildingsController.getNameText1().setVisible(true);
        buildingsController.getFrame1().setVisible(true);
        buildingsController.getButtonInfo1().setVisible(true);

        buildingsController.getImageRound2().setVisible(true);
        buildingsController.getNameBar2().setVisible(true);
        buildingsController.getNameText2().setVisible(true);
        buildingsController.getFrame2().setVisible(true);
        buildingsController.getButtonInfo2().setVisible(true);
    }

    private void setInfo(String cardName) {
        buildingsController.getRightPane().setVisible(false);
        buildingsController.getRightPane().setDisable(true);
        buildingsController.getInfoStackPane().setVisible(true);
        buildingsController.getInfoStackPaneOpen().setVisible(true);
        buildingsController.getInfoGodImage().setImage(guiCards.getSmallImage(cardName));
        buildingsController.getInfoGodName().setText(cardName);
        buildingsController.getInfoDescription().setText(guiCards.getDescription(cardName));
    }

    private void closeInfo() {
        buildingsController.getRightPane().setVisible(true);
        buildingsController.getRightPane().setDisable(false);
        buildingsController.getInfoStackPane().setVisible(false);
    }

    public void setUp(Stage stage, GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;

        SmartGroup group = new SmartGroup();
        guiGridManager = new GUIGridManager(group, this);

        SubScene subScene = new SubScene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        stage.sizeToScene();
        PerspectiveCamera camera = createCamera();
        subScene.setCamera(camera);

        guiGridManager.createGrid();
        gridAnimations(group, camera);

        eventHandler(stage);
        guiCards = stagesManager.getGuiDrawStage().getGuiCards();
        try { buildingsImages(stage, subScene); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Time to cancel the action
     */
    public void timerToCancel() {
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> CountDown()));
        animation.setCycleCount(timer+1);
        animation.setOnFinished(event -> {
            Platform.runLater(() -> buildingsController.getErrorText().setText(""));
            Platform.runLater(() -> buildingsController.getMessageText().setVisible(true));
        });
        animation.play();
    }

    private void CountDown() {
    }

    /**
     * Shows the different colors to choose
     * @param colorsName list of the colors
     */
    public void showColors(ArrayList<String> colorsName) {
        buildingsController.getBorderPaneCards().setPickOnBounds(true);
        if(colorsName.size() == 3){
            buildingsController.getThreeWorkersPane().setVisible(true);
            buildingsController.getWorkerImage3p1().setVisible(true);
            buildingsController.getButtonWorker3p1().setOnAction(event -> sendToStagesManager("Light-brown"));
            buildingsController.getWorkerImage3p2().setVisible(true);
            buildingsController.getButtonWorker3p2().setOnAction(event -> sendToStagesManager("Silver"));
            buildingsController.getWorkerImage3p3().setVisible(true);
            buildingsController.getButtonWorker3p3().setOnAction(event -> sendToStagesManager("Dark-brown"));
        }
        else if(colorsName.size() == 2) {
            buildingsController.getTwoWorkersPane().setVisible(true);
            if(colorsName.get(0).equalsIgnoreCase("Light-brown")) {
                buildingsController.getWorkerImage2p1().setVisible(true);
                buildingsController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/lightBrownWorker.png")));
                buildingsController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            else if(colorsName.get(0).equalsIgnoreCase("Silver")) {
                buildingsController.getWorkerImage2p1().setVisible(true);
                buildingsController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/silverWorker.png")));
                buildingsController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            else if(colorsName.get(0).equalsIgnoreCase("Dark-brown")) {
                buildingsController.getWorkerImage2p1().setVisible(true);
                buildingsController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/darkBrownWorker.png")));
                buildingsController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            if(colorsName.get(1).equalsIgnoreCase("Light-brown")) {
                buildingsController.getWorkerImage2p2().setVisible(true);
                buildingsController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/lightBrownWorker.png")));
                buildingsController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }
            else if(colorsName.get(1).equalsIgnoreCase("Silver")) {
                buildingsController.getWorkerImage2p2().setVisible(true);
                buildingsController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/silverWorker.png")));
                buildingsController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }
            else if(colorsName.get(1).equalsIgnoreCase("Dark-brown")) {
                buildingsController.getWorkerImage2p2().setVisible(true);
                buildingsController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/darkBrownWorker.png")));
                buildingsController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }

        }
    }

    public void sendToStagesManager(String colorName) {
        stagesManager.send(new PickColorEvent(guiColorDecoder.getPlayerColor(colorName)));
        buildingsController.getThreeWorkersPane().setVisible(false);
        buildingsController.getTwoWorkersPane().setVisible(false);
    }

    public void setBounds() {
        buildingsController.getTwoWorkersPane().setPickOnBounds(false);
        buildingsController.getThreeWorkersPane().setPickOnBounds(false);
        buildingsController.getInfoStackPane().setPickOnBounds(false);
        buildingsController.getBorderPaneCards().setPickOnBounds(false);
        playersCardShow();
    }

    /**
     * Changes the frame of the loser
     * @param loserName name of the loser
     */
    public void setNewFrame(String loserName) {
        if(buildingsController.getNameText1().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) buildingsController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));
        }
        else if(buildingsController.getNameText2().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) buildingsController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));        }
        else if(buildingsController.getNameText3().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) buildingsController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));
        }
    }

}