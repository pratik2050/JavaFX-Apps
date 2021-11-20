package com.pratik.connect4;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final int Column = 7;
    private static final int Row = 6;
    private static final int Circle_Diameter = 80;
    private static final String diskColor_1 = "#24303E";
    private static final String diskColor_2 = "#4CAA88";

    private static String playerOne = "Player One";
    private static String playerTwo = "Player Two";

    private static final boolean isPlayerOneTurn = true;

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiskPane;
    @FXML
    public Label PlayerNameLabel;
    @FXML
    public Label Turn;

    public void createRectangle() {

        Shape rectangleWithHoles = gameStructure();
        rootGridPane.add(rectangleWithHoles,0,1);

        List<Rectangle> rectangleList = createClickableColumn();

        for (Rectangle rectangle:rectangleList){
            rootGridPane.add(rectangle,0,1);
        }

    }

    private Shape gameStructure() {
        Shape rectangleWithHoles = new Rectangle((Column + 1)*Circle_Diameter,(Row + 1)*Circle_Diameter);

        for (int row = 0; row <Row; row++) {
            for (int col = 0; col < Column; col ++) {
                Circle circle = new Circle();
                circle.setRadius(Circle_Diameter / 2);
                circle.setCenterX(Circle_Diameter/2);
                circle.setCenterY(Circle_Diameter/2);

                circle.setTranslateX(col * (Circle_Diameter + 5) + Circle_Diameter/4);
                circle.setTranslateY(row * (Circle_Diameter + 5) + Circle_Diameter/4);

                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }

        rectangleWithHoles.setFill(Color.WHITE);

        return  rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumn() {

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col = 0; col < Column; col++) {
            Rectangle rectangle = new Rectangle(Circle_Diameter,(Row + 1)*Circle_Diameter);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (Circle_Diameter + 5) + Circle_Diameter/4);

            rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));

            final int column = col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                insertDisk(new Disk(isPlayerOneTurn), column);
            });

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private static void insertDisk(Disk disk, int column) {

    }

    private static class Disk extends Circle {
        private final boolean isPlayerOneMove;

        public Disk(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(Circle_Diameter/2);
            setFill(isPlayerOneMove? Color.valueOf(diskColor_1): Color.valueOf(diskColor_2));
            setTranslateX(Circle_Diameter/2);
            setTranslateY(Circle_Diameter/2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
