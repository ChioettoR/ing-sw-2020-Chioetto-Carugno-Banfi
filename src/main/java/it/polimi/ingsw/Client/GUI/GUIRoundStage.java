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

    private final double rotationSpeed = 100;
    private final double zoomSpeed = 10;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private final DoubleProperty rotationVelocity = new SimpleDoubleProperty();
    private final DoubleProperty zoomVelocity = new SimpleDoubleProperty();
    private final LongProperty lastUpdateTime = new SimpleLongProperty();
    private final GUIColorDecoder guiColorDecoder = new GUIColorDecoder();
    private GUIRoundController guiRoundController;
    private ActionType selectedActionType;
    private GUIStagesManager stagesManager;
    private GUIPlayersManager guiPlayersManager;
    private GUICards guiCards;
    private final HashMap<ActionType, String> buttonsStyle = new HashMap<>();
    private boolean buttonTranslated = false;
    private Scene scene;

    private final int maxFOV = 40;
    private final int minFOV = 25;
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    private GUIGridManager guiGridManager;

    public void setSelectedActionType(ActionType selectedActionType) {
        this.selectedActionType = selectedActionType;
    }

    public GUIGridManager getGuiGridManager() {
        return guiGridManager;
    }

    public GUIStagesManager getStagesManager() {
        return stagesManager;
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
        guiRoundController.getMessageText().setVisible(true);
        guiRoundController.getMessageText().setText(message);
        guiRoundController.getErrorText().setVisible(false);
    }

    public void readError(String error) {
        guiRoundController.getErrorText().setVisible(true);
        guiRoundController.getErrorText().setText(error);
        guiRoundController.getMessageText().setVisible(false);
        timerToCancel();
    }

    /**
     * Images for the buildings
     * @param subScene subscene of the grid
     * @throws IOException when socket closes
     */
    private void buildingsImages(SubScene subScene) throws IOException {

        initializeButtonsStyle();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/buildings.fxml"));
        Parent root = loader.load();
        guiRoundController = loader.getController();

        Rectangle rectangle = new Rectangle(90, 20, Color.FORESTGREEN);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        ImageView baseBlock = guiRoundController.getBaseBlock();
        ImageView mediumBlock = guiRoundController.getMediumBlock();
        ImageView highBlock = guiRoundController.getHighBlock();
        ImageView dome = guiRoundController.getDome();
        BorderPane pane = guiRoundController.getPane();
        pane.setCenter(subScene);
        subScene.widthProperty().bind(pane.widthProperty());
        subScene.heightProperty().bind(pane.heightProperty());
        subScene.toBack();

        StackPane stackPane = guiRoundController.getStackPane();
        stackPane.toFront();

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
     * @param group Main group
     * @param camera Main camera
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
        guiRoundController.getLeftButton().setDisable(true);
        guiRoundController.getLeftButton().setVisible(false);
        guiRoundController.getRightButton().setDisable(true);
        guiRoundController.getRightButton().setVisible(false);
        guiRoundController.getCenterButton().setDisable(true);
        guiRoundController.getCenterButton().setVisible(false);
    }

    private void activeButtonCenter(ActionType actionType, boolean onlyCenter) {
        if(onlyCenter) {
            guiRoundController.getLeftButton().setDisable(true);
            guiRoundController.getLeftButton().setVisible(false);
            guiRoundController.getRightButton().setDisable(true);
            guiRoundController.getRightButton().setVisible(false);
        }
        guiRoundController.getCenterButton().setDisable(false);
        guiRoundController.getCenterButton().setVisible(true);
        guiRoundController.getCenterButton().getStylesheets().clear();
        guiRoundController.getCenterButton().getStylesheets().add(getStyle(actionType));
        guiRoundController.getCenterButton().setOnAction(event -> sendActionToClient(actionType.toString()));
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
        guiRoundController.getLeftButton().setDisable(false);
        guiRoundController.getLeftButton().setVisible(true);
        guiRoundController.getRightButton().setDisable(false);
        guiRoundController.getRightButton().setVisible(true);
        guiRoundController.getRightButton().getStylesheets().clear();
        guiRoundController.getRightButton().getStylesheets().add(getStyle(actionTypeRight));
        guiRoundController.getRightButton().setOnAction(event -> sendActionToClient(actionTypeRight.toString()));
        guiRoundController.getLeftButton().getStylesheets().clear();
        guiRoundController.getLeftButton().getStylesheets().add(getStyle(actionTypeLeft));
        guiRoundController.getLeftButton().setOnAction(event -> sendActionToClient(actionTypeLeft.toString()));
        if(onlyBorders) {
            guiRoundController.getCenterButton().setDisable(true);
            guiRoundController.getCenterButton().setVisible(false);
            if(!buttonTranslated) {
                guiRoundController.getLeftButton().setTranslateX(buttonTranslation);
                guiRoundController.getRightButton().setTranslateX(-buttonTranslation);
                buttonTranslated = true;
            }
        }
        else {
            if(buttonTranslated) {
                guiRoundController.getLeftButton().setTranslateX(-buttonTranslation);
                guiRoundController.getRightButton().setTranslateX(+buttonTranslation);
                buttonTranslated = false;
            }
        }
    }

    public void sendActionToClient(String action) {
        guiRoundController.getCenterButton().setDisable(true);
        guiRoundController.getCenterButton().setVisible(false);
        guiRoundController.getLeftButton().setDisable(true);
        guiRoundController.getLeftButton().setVisible(false);
        guiRoundController.getRightButton().setDisable(true);
        guiRoundController.getRightButton().setVisible(false);
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
            guiRoundController.getCloseInfoButton().setOnAction(event -> closeInfo());
            player1 = guiPlayersManager.getPlayer(playerNames.get(0));
            guiRoundController.getImageRound1().setImage(guiCards.getFullImage(player1.getCardName()));
            guiRoundController.getNameText1().setText(player1.getName());
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            guiRoundController.getButtonInfo1().setOnAction(event -> setInfo(player1.getCardName()));
            player2 = guiPlayersManager.getPlayer(playerNames.get(1));
            guiRoundController.getImageRound2().setImage(guiCards.getFullImage(player2.getCardName()));
            guiRoundController.getNameText2().setText(player2.getName());
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            guiRoundController.getButtonInfo2().setOnAction(event -> setInfo(player2.getCardName()));
            player3 = guiPlayersManager.getPlayer(playerNames.get(2));
            guiRoundController.getImageRound3().setImage(guiCards.getFullImage(player3.getCardName()));
            guiRoundController.getNameText3().setText(player3.getName());
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player3.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            guiRoundController.getButtonInfo3().setOnAction(event -> setInfo(player3.getCardName()));
        }
        else {
            setVisibleTwo();
            guiRoundController.getCloseInfoButton().setOnAction(event -> closeInfo());
            player1 = guiPlayersManager.getPlayer(playerNames.get(0));
            guiRoundController.getImageRound1().setImage(guiCards.getFullImage(player1.getCardName()));
            guiRoundController.getNameText1().setText(player1.getName());
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player1.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            guiRoundController.getButtonInfo1().setOnAction(event -> setInfo(player1.getCardName()));
            player2 = guiPlayersManager.getPlayer(playerNames.get(1));
            guiRoundController.getImageRound2().setImage(guiCards.getFullImage(player2.getCardName()));
            guiRoundController.getNameText2().setText(player2.getName());
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Light-brown")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrown.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Silver")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilver.png")));
            if(player2.getColor().equals(guiColorDecoder.getColor(guiColorDecoder.getPlayerColor("Dark-brown")))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrown.png")));
            guiRoundController.getButtonInfo2().setOnAction(event -> setInfo(player2.getCardName()));
        }
    }

    private void setVisibleAll() {
        setVisibleTwo();
        guiRoundController.getImageRound3().setVisible(true);
        guiRoundController.getNameBar3().setVisible(true);
        guiRoundController.getNameText3().setVisible(true);
        guiRoundController.getFrame3().setVisible(true);
        guiRoundController.getButtonInfo3().setVisible(true);
    }

    private void setVisibleTwo() {
        guiRoundController.getRightPane().setVisible(true);
        guiRoundController.getBorderPaneCards().setVisible(true);
        guiRoundController.getBorderPaneCards().setPickOnBounds(false);

        guiRoundController.getImageRound1().setVisible(true);
        guiRoundController.getNameBar1().setVisible(true);
        guiRoundController.getNameText1().setVisible(true);
        guiRoundController.getFrame1().setVisible(true);
        guiRoundController.getButtonInfo1().setVisible(true);

        guiRoundController.getImageRound2().setVisible(true);
        guiRoundController.getNameBar2().setVisible(true);
        guiRoundController.getNameText2().setVisible(true);
        guiRoundController.getFrame2().setVisible(true);
        guiRoundController.getButtonInfo2().setVisible(true);
    }

    private void setInfo(String cardName) {
        guiRoundController.getRightPane().setVisible(false);
        guiRoundController.getRightPane().setDisable(true);
        guiRoundController.getInfoStackPane().setVisible(true);
        guiRoundController.getInfoStackPaneOpen().setVisible(true);
        guiRoundController.getInfoGodImage().setImage(guiCards.getSmallImage(cardName));
        guiRoundController.getInfoGodName().setText(cardName);
        guiRoundController.getInfoDescription().setText(guiCards.getDescription(cardName));
    }

    private void closeInfo() {
        guiRoundController.getRightPane().setVisible(true);
        guiRoundController.getRightPane().setDisable(false);
        guiRoundController.getInfoStackPane().setVisible(false);
    }

    public void setUp(Stage stage, GUIStagesManager stagesManager) {
        this.stagesManager = stagesManager;

        Group group = new Group();
        guiGridManager = new GUIGridManager(group, this);

        SubScene subScene = new SubScene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        stage.sizeToScene();
        PerspectiveCamera camera = createCamera();
        subScene.setCamera(camera);

        guiGridManager.createGrid();
        gridAnimations(group, camera);

        eventHandler(stage);
        guiCards = stagesManager.getGuiPickCardStage().getGuiCards();
        try { buildingsImages(subScene); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Time to cancel the action
     */
    public void timerToCancel() {
        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> CountDown()));
        int timer = 3;
        animation.setCycleCount(timer +1);
        animation.setOnFinished(event -> {
            Platform.runLater(() -> guiRoundController.getErrorText().setText(""));
            Platform.runLater(() -> guiRoundController.getMessageText().setVisible(true));
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
        guiRoundController.getBorderPaneCards().setPickOnBounds(true);
        if(colorsName.size() == 3){
            guiRoundController.getThreeWorkersPane().setVisible(true);
            guiRoundController.getWorkerImage3p1().setVisible(true);
            guiRoundController.getButtonWorker3p1().setOnAction(event -> sendToStagesManager("Light-brown"));
            guiRoundController.getWorkerImage3p2().setVisible(true);
            guiRoundController.getButtonWorker3p2().setOnAction(event -> sendToStagesManager("Silver"));
            guiRoundController.getWorkerImage3p3().setVisible(true);
            guiRoundController.getButtonWorker3p3().setOnAction(event -> sendToStagesManager("Dark-brown"));
        }
        else if(colorsName.size() == 2) {
            guiRoundController.getTwoWorkersPane().setVisible(true);
            if(colorsName.get(0).equalsIgnoreCase("Light-brown")) {
                guiRoundController.getWorkerImage2p1().setVisible(true);
                guiRoundController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/lightBrownWorker.png")));
                guiRoundController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            else if(colorsName.get(0).equalsIgnoreCase("Silver")) {
                guiRoundController.getWorkerImage2p1().setVisible(true);
                guiRoundController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/silverWorker.png")));
                guiRoundController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            else if(colorsName.get(0).equalsIgnoreCase("Dark-brown")) {
                guiRoundController.getWorkerImage2p1().setVisible(true);
                guiRoundController.getWorkerImage2p1().setImage(new Image(getClass().getResourceAsStream("/darkBrownWorker.png")));
                guiRoundController.getButtonWorker2p1().setOnAction(event -> sendToStagesManager(colorsName.get(0)));
            }
            if(colorsName.get(1).equalsIgnoreCase("Light-brown")) {
                guiRoundController.getWorkerImage2p2().setVisible(true);
                guiRoundController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/lightBrownWorker.png")));
                guiRoundController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }
            else if(colorsName.get(1).equalsIgnoreCase("Silver")) {
                guiRoundController.getWorkerImage2p2().setVisible(true);
                guiRoundController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/silverWorker.png")));
                guiRoundController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }
            else if(colorsName.get(1).equalsIgnoreCase("Dark-brown")) {
                guiRoundController.getWorkerImage2p2().setVisible(true);
                guiRoundController.getWorkerImage2p2().setImage(new Image(getClass().getResourceAsStream("/darkBrownWorker.png")));
                guiRoundController.getButtonWorker2p2().setOnAction(event -> sendToStagesManager(colorsName.get(1)));
            }

        }
    }

    public void sendToStagesManager(String colorName) {
        stagesManager.send(new PickColorEvent(guiColorDecoder.getPlayerColor(colorName)));
        guiRoundController.getThreeWorkersPane().setVisible(false);
        guiRoundController.getTwoWorkersPane().setVisible(false);
    }

    public void setBounds() {
        guiRoundController.getTwoWorkersPane().setPickOnBounds(false);
        guiRoundController.getThreeWorkersPane().setPickOnBounds(false);
        guiRoundController.getInfoStackPane().setPickOnBounds(false);
        guiRoundController.getBorderPaneCards().setPickOnBounds(false);
        playersCardShow();
    }

    /**
     * Changes the frame of the loser
     * @param loserName name of the loser
     */
    public void setNewFrame(String loserName) {
        if(guiRoundController.getNameText1().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) guiRoundController.getFrame1().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));
        }
        else if(guiRoundController.getNameText2().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) guiRoundController.getFrame2().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));        }
        else if(guiRoundController.getNameText3().getText().equals(loserName)) {
            //silver
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("8a9f9f"))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameSilverWithX.png")));
            //light-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("cfb39c"))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameLightBrownWithX.png")));
            //dark-brown
            if(guiPlayersManager.getPlayer(loserName).getColor().equals(javafx.scene.paint.Color.web("7c5536"))) guiRoundController.getFrame3().setImage(new Image(getClass().getResourceAsStream("/frameDarkBrownWithX.png")));
        }
    }

}