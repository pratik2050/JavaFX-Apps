package tempConvertApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;                              // Main file -- created by Pratik Das
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TempConvertApp.fxml"));
        VBox rootNode = loader.load();

        MenuBar menuBar = createMenu();
        rootNode.getChildren().add(0,menuBar);

        Scene scene = new Scene(rootNode);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Temperature Convertor");
        primaryStage.show();
    }

    private MenuBar createMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem newMenu = new MenuItem("New");

        newMenu.setOnAction(actionEvent -> System.out.println("new button pressed"));

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem quitMenu = new MenuItem("quit");

        quitMenu.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });

        fileMenu.getItems().addAll(newMenu,separatorMenuItem,quitMenu);

        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");

        about.setOnAction(actionEvent -> about());


        helpMenu.getItems().add(about);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }

    private void about() {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.setTitle("My First Desktop Application");
        alertDialog.setHeaderText("Learning JavaFX");
        alertDialog.setContentText("Hi I am Pratik Das, creator of this Temperature Convertor tool, I am student at NIT Agartala");

        ButtonType yesBTN = new ButtonType("Yes");
        ButtonType noBTN = new ButtonType("No");

        alertDialog.getButtonTypes().setAll(yesBTN,noBTN);

        Optional<ButtonType> clickedBTN = alertDialog.showAndWait();

        if (clickedBTN.isPresent() && clickedBTN.get() == yesBTN) {
            System.out.println("Yes pressed");
        }
        if (clickedBTN.isPresent() && clickedBTN.get() == noBTN) {
            System.out.println("No pressed");
        }
    }
}
