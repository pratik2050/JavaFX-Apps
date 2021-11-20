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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

