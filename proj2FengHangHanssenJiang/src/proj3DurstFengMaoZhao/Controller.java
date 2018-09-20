package proj3DurstFengMaoZhao;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;


public class Controller{


    // hello button specified in Main.fxml
    @FXML Button helloButton;
    // goodbye button specified in Main.fxml
    @FXML Button goodbyeButton;


    /**
     * Exit the program
     *
     * @param event ActionEvent object
     */
    @FXML void handleExitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Create a dialog that takes in an integer between 0 and 255,
     * and set the hello button text to the entered number
     *
     * @param event ActionEvent object
     */
    @FXML void handleHelloButtonAction(ActionEvent event) {
        // set up the number input dialog
        TextInputDialog dialog = new TextInputDialog("60");
        dialog.setTitle("Give me a number");
        dialog.setHeaderText("Give me an integer from 0 to 255:");

        final Optional<String> enterValue = dialog.showAndWait();
        if (enterValue.isPresent()) {
            this.helloButton.setText(enterValue.get());
        }
    }

    /**
     * Sets the text of goodbye button to "Yah, sure!"
     *
     * @param event ActionEvent object
     */
    @FXML void handleGoodbyeButtonAction(ActionEvent event) {
        goodbyeButton.setText("Yah, sure!");
    }

}