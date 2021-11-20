package com.pratik.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;               // Game Made By Pratik Das
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();

        controller = loader.getController();
        controller.createRectangle();

        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);

        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect4 Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private MenuBar createMenu() {
        //File Menu
        Menu fileMenu = new Menu("File");

        //File Menu Items
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(actionEvent -> resetGame());

        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(actionEvent -> resetGame());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        MenuItem quitGame = new MenuItem("Quit");
        quitGame.setOnAction(actionEvent -> quitGame());


        fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,quitGame);

        //Help Menu
        Menu helpMenu = new Menu("Help");

        //Help Menu Items
        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(actionEvent -> aboutConnect4());

        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(actionEvent -> aboutMe());


        helpMenu.getItems().addAll(aboutGame,separator,aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Made by Pratik Das");
        alert.setContentText("Hi I am Pratik Das, A student of NIT Agartala, my field of interest are programming in Java & Web Development");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect 4");
        alert.setHeaderText("How to Play ???");
        alert.setContentText("First, decide who goes first and what color each player will have. Players must alternate turns, and only one disc can be dropped in each turn. On your turn, drop one of your colored discs from the top into any of the seven slots. The game ends when there is a 4-in-a-row or a stalemate. The starter of the previous game goes second on the next game.");
        alert.show();

    }

    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //will be done
    }


    public static void main(String[] args) {
        launch(args);
    }
}
