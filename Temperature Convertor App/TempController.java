package tempConvertApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TempController implements Initializable {

    @FXML
    public Label welcomeLabel;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public TextField userInput;
    @FXML
    public Button convertBtn;

    private boolean isCtoF = true;
    private static final String CtoF = "Celsius to Fahrenheit";
    private static final String FtoC = "Fahrenheit to Celsius";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().add(CtoF);
        choiceBox.getItems().add(FtoC);
        choiceBox.setValue(CtoF);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals(CtoF)) {
                isCtoF = true;
            } else {
                isCtoF = false;
            }
        });

        convertBtn.setOnAction(actionEvent -> {
            convert();
        });
    }

    private void convert() {
        String input = userInput.getText();
        float enteredTemp = 0.0f;
        try {
            enteredTemp = Float.parseFloat(input);
        } catch (Exception exp) {
            warnUser();
            return;
        }
        float newTemp;

        if(isCtoF) {
            newTemp = (enteredTemp * 9/5)  + 32;
        } else {
            newTemp = (enteredTemp - 32) * 5/9;
        }

        display(newTemp);
    }

    private void warnUser() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning !!!");
        alert.setHeaderText("Invalid Text Entered");
        alert.setContentText("Plz Enter a valid Temperature");
        alert.show();
    }

    private void display(float newTemp) {
        String unit = isCtoF?  " F" : " C";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setContentText("Converted Temperature is  "+ newTemp + unit);
        alert.show();
    }
}
