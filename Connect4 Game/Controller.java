package com.pratik.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static final int Column = 7;
    private static final int Row = 6;
    private static final int Circle_Diameter = 80;
    private static final String diskColor_1 = "#24303E";
    private static final String diskColor_2 = "#4CAA88";

    private static String playerOne = "Player One";
    private static String playerTwo = "Player Two";

    private static boolean isPlayerOneTurn = true;

    private Disk[][] insertedDiskArray = new Disk[Row][Column];

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiskPane;
    @FXML
    public Label PlayerNameLabel;

    private boolean isAllowedToInsert = true;

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
                circle.setSmooth(true);

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
                if (isAllowedToInsert) {
                    isAllowedToInsert = false;
                    insertDisk(new Disk(isPlayerOneTurn), column);
                }
            });

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisk(Disk disk, int column) {
        int row = Row - 1;
        while (row >= 0) {
            if (getDiskIfPresent(row, column) == null) {
                break;
            }
            row--;
        }

        if (row < 0) {
            return;
        }

        insertedDiskArray[row][column] = disk;
        insertedDiskPane.getChildren().add(disk);

        disk.setTranslateX(column * (Circle_Diameter + 5) + 3*(Circle_Diameter / 4));

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disk);
        translateTransition.setToY(row * (Circle_Diameter + 5) + 3*(Circle_Diameter / 4));

        int currentRow = row;
        translateTransition.setOnFinished(actionEvent -> {

            isAllowedToInsert = true;
            if(gameEnded(currentRow,column)) {
                gameOver();
                return;
            }

            isPlayerOneTurn = !isPlayerOneTurn;
            PlayerNameLabel.setText(isPlayerOneTurn? playerOne:playerTwo);
        });

        translateTransition.play();
    }

    private boolean gameEnded(int row, int column) {

        //vertical & horizontal points
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3).mapToObj(r ->new Point2D(r, column)).collect(Collectors.toList());
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3).mapToObj(col ->new Point2D(row, col)).collect(Collectors.toList());

        //diagonal points
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6).mapToObj(i -> startPoint1.add(i,-i)).collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6).mapToObj(i -> startPoint2.add(i, i)).collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints) || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {

        int chain =0;

        for (Point2D point : points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disk disk = getDiskIfPresent(rowIndexForArray, columnIndexForArray);

            if (disk != null && disk.isPlayerOneMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }

        }
        return false;
    }

    private Disk getDiskIfPresent(int row, int column) {

        if (row >= Row || row < 0 || column >= Column || column < 0) {
            return null;
        }
        return insertedDiskArray[row][column];
    }

    private void gameOver() {
        String winner = isPlayerOneTurn? playerOne:playerTwo;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect4 - GameOver");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to play again ???");

        ButtonType yesBtn = new ButtonType("Yes");
        ButtonType noBtn = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);

        Platform.runLater(() -> {
            Optional<ButtonType> btnClicked = alert.showAndWait();

            if(btnClicked.isPresent() && btnClicked.get() == yesBtn) {
                resetGame();
            } else {
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public void resetGame() {
        insertedDiskPane.getChildren().clear();

        for (int row = 0; row < insertedDiskArray.length; row++) {
            for (int col = 0; col < insertedDiskArray[row].length; col++) {
                insertedDiskArray[row][col] = null;
            }
        }

        isPlayerOneTurn = true;
        PlayerNameLabel.setText("Player One");

        createRectangle();
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
