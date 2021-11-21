package com.pratik.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;                         // By Pratik Das
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static final int COLUMN = 7;
    private static final int ROW = 6;
    private static final int CIRCLE_DIAMETER = 80;
    private static final String diskColor_1 = "#24303E";        // final values
    private static final String diskColor_2 = "#4CAA88";

    private static String playerOne;
    private static String playerTwo;            // player names

    private static boolean isPlayerOneTurn = true;

    private final Disk[][] insertedDiskArray = new Disk[ROW][COLUMN];

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiskPane;
    @FXML
    public Label PlayerNameLabel;
    @FXML
    public TextField playerOneTextField, playerTwoTextField;
    @FXML
    public Button setNamesButton;
    @FXML
    public Label Turn;

    private boolean isAllowedToInsert = true;

    public void createPlayground() {

        setNamesButton.setOnAction(actionEvent -> {
            playerOne = playerOneTextField.getText();
            playerTwo = playerTwoTextField.getText();               // setting names
            PlayerNameLabel.setText(playerOne);
        });

        Shape rectangleWithHoles = gameStructure();
        rootGridPane.add(rectangleWithHoles,0,1);

        List<Rectangle> rectangleList = createClickableColumn();

        for (Rectangle rectangle:rectangleList){                        //rootGrid pane
            rootGridPane.add(rectangle,0,1);
        }



    }

    private Shape gameStructure() {
        Shape rectangleWithHoles = new Rectangle((COLUMN + 1)* CIRCLE_DIAMETER,(ROW + 1)* CIRCLE_DIAMETER);

        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COLUMN; col ++) {
                Circle circle = new Circle();
                circle.setRadius(40);
                circle.setCenterX(40);               // creating circles
                circle.setCenterY(40);
                circle.setSmooth(true);

                circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + 20);
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + 20);          // adjusting position of circles

                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }

        rectangleWithHoles.setFill(Color.WHITE);                    // hollow circles

        return  rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumn() {

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col = 0; col < COLUMN; col++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROW + 1)* CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + 20);

            rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));       //allowing player to insert disk
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));

            final int column = col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if (isAllowedToInsert) {
                    isAllowedToInsert = false;                              // make sure only one disk is allowed at a time
                    insertDisk(new Disk(isPlayerOneTurn), column);
                }
            });

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisk(Disk disk, int column) {
        int row = ROW - 1;
        while (row >= 0) {
            if (getDiskIfPresent(row, column) == null) {        // code for inserting disk
                break;
            }
            row--;
        }

        if (row < 0) {
            return;
        }

        insertedDiskArray[row][column] = disk;
        insertedDiskPane.getChildren().add(disk);

        disk.setTranslateX(column * (CIRCLE_DIAMETER + 5) + 60);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disk);          //adjusting the inserted disk
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + 60);                                           //adding animation while inserting disk

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

        return checkCombinations(verticalPoints) || checkCombinations(horizontalPoints) || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
    }

    private boolean checkCombinations(List<Point2D> points) {

        int chain =0;

        for (Point2D point : points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();
                                                                                                 // checking possible combinations
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

        if (row >= ROW || row < 0 || column >= COLUMN || column < 0) {
            return null;
        }
        return insertedDiskArray[row][column];
    }

    private void gameOver() {
        String winner = isPlayerOneTurn? playerOne:playerTwo;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect4 - GameOver");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to play again ???");             //endgame gameOver code

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

        for (Disk[] disks : insertedDiskArray) {
            // reset and new game code
            Arrays.fill(disks, null);
        }

        isPlayerOneTurn = true;
        PlayerNameLabel.setText("Player One");

        createPlayground();
    }

    private static class Disk extends Circle {
        private final boolean isPlayerOneMove;

        public Disk(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(40);
            setFill(isPlayerOneMove? Color.valueOf(diskColor_1): Color.valueOf(diskColor_2));       // disk class
            setTranslateX(40);
            setTranslateY(40);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


