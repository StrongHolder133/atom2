package com.example.demo3;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;;
import javafx.stage.Stage;


public class Main extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final int[] selectedArea = new int[2];
    private boolean isCube = false, isSphere = false;
    int figureHight = 100;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private void initMouseControl(Group group, Scene scene) {

        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button createCubeButton = new Button("Создать куб");
        Button createSphereButton = new Button("Создать шар");
        Button selectButton = new Button("Выделить");
        Label lowerBoundLabel = new Label("Нижняя граница:");
        TextField lowerBoundTextField = new TextField();
        Label upperBoundTextLabel = new Label("Верхняя граница:");
        TextField upperBoundTextField = new TextField();
        selectedArea[0] = -1;
        selectedArea[1] = -1;

        HBox hBox = new HBox(createCubeButton, createSphereButton, selectButton);
        VBox vBox = new VBox(hBox, lowerBoundLabel, lowerBoundTextField, upperBoundTextLabel, upperBoundTextField);
        Group group = new Group();
        Group mainGroup = new Group();

        final Box box = new Box(figureHight, figureHight, figureHight);
        box.setLayoutX(WIDTH / 2 + figureHight / 2);
        box.setLayoutY(HEIGHT / 2 - figureHight / 2);
        box.setTranslateZ(-figureHight / 2);

        final Sphere sphere = new Sphere(figureHight / 2);
        sphere.setLayoutX(WIDTH / 2 + figureHight / 2);
        sphere.setLayoutY(HEIGHT / 2 - figureHight / 2);
        sphere.setTranslateZ(-figureHight / 2);

        group.getChildren().add(new CoordinatSystem().draw());

        primaryStage.show();
        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(mainGroup, WIDTH, HEIGHT, true);
        initMouseControl(group, scene);
        mainGroup.getChildren().add(vBox);
        mainGroup.getChildren().add(group);

        createCubeButton.setOnAction(event -> {
            isCube = true;
            isSphere = false;
            selectedArea[0] = -1;
            selectedArea[1] = -1;
            group.getChildren().remove(sphere);
            group.getChildren().remove(box);
            group.getChildren().add(box);
        });

        createSphereButton.setOnAction(event -> {
            isCube = false;
            isSphere = true;
            group.getChildren().remove(box);
            group.getChildren().removeAll(sphere);
            group.getChildren().add(sphere);
        });

        selectButton.setOnAction(event -> {
            if (!lowerBoundTextField.getText().isEmpty() && !upperBoundTextField.getText().isEmpty()) {
                int lower = Integer.parseInt(lowerBoundTextField.getText());
                int upper = Integer.parseInt(upperBoundTextField.getText());
                if (lower >= 0 && upper <= 10 && lower < upper) {
                    selectedArea[0] = lower;
                    selectedArea[1] = upper;
                    if (isCube) {
                        group.getChildren().remove(box);
                        int newHeight = figureHight / 10 * (upper - lower);
                        box.setHeight(newHeight);
                        box.setLayoutY(HEIGHT / 2 - newHeight / 2 - lower * 10);
                        group.getChildren().add(box);
                    } else if (isSphere) {

                        Box lowBorder = new Box(100, 1, 100);
                        lowBorder.setLayoutX(WIDTH / 2 + 50);
                        lowBorder.setLayoutY(HEIGHT / 2 - lower * 10);
                        lowBorder.setTranslateZ(-50);
                        Box upperBorder = new Box(100, 1, 100);
                        upperBorder.setLayoutX(WIDTH / 2 + 50);
                        upperBorder.setLayoutY(HEIGHT / 2 - upper * 10);
                        upperBorder.setTranslateZ(-50);
                        group.getChildren().removeAll(upperBorder, lowBorder);
                        group.getChildren().addAll(upperBorder, lowBorder);
                    }
                }
            }
        });

        primaryStage.setScene(scene);
        scene.setFill(Color.WHITE);
        scene.setCamera(camera);
    }
}


