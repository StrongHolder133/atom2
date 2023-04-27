package com.example.demo3;

import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.Group;


public class CoordinatSystem {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;

    public Group draw() {

        Group csGroup = new Group();

        Line xAxis = new Line(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT / 2);
        xAxis.setStrokeWidth(3);
        xAxis.setStroke(Color.RED);

        for (int i = 0; i < HEIGHT / 2; i += 50) {
            Line line = new Line(WIDTH / 2 - 3, i, WIDTH / 2 + 3, i);
            line.setFill(Color.BLUE);
            csGroup.getChildren().add(line);
        }

        Line yAxis = new Line(WIDTH / 2, HEIGHT / 2, WIDTH / 2, -WIDTH / 2);
        yAxis.setStrokeWidth(3);
        yAxis.setStroke(Color.BLUE);

        final Box zAxis = new Box(2, 2, WIDTH);
        zAxis.setLayoutX(WIDTH / 2);
        zAxis.setLayoutY(HEIGHT / 2);
        zAxis.setTranslateZ(-WIDTH / 2);

        csGroup.getChildren().addAll(xAxis, yAxis, zAxis);

        return csGroup;
    }

}
