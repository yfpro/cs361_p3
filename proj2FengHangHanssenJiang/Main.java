/*
 * File: Main.java
 * Names: Liwei Jiang, Yi Feng, Jackie Hang, Paige Hanssen
 * F18 CS361 Project 2
 * This file creates a window that contains Hello and Goodbye buttons that have features
 * of creating an "input a number" dialog, changing the text of the button to "Yah, sure!",
 * and displaying a text area containing "Sample text"
 * Date: 09/18/2018
 */

package proj2FengHangHanssenJiang;
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

/**
 * This class creates a window that contains Hello and Goodbye buttons
 * that have features of creating an "input a number" dialog, changing
 * the text of the button to "Yah, sure!", and displaying a text area
 * containing "Sample text"
 *
 * @author  Liwei Jiang, Yi Feng, Jackie Hang, Paige Hanssen
 */
public class Main extends Application {
    // hello button specified in Main.fxml
    @FXML Button helloButton;
    // goodbye button specified in Main.fxml
    @FXML Button goodbyeButton;


    /**
     * Create a window that contains Hello and Goodbye buttons
     * that have features of creating an "input a number" dialog,
     * changing the text of the button to "Yah, sure!", and
     * displaying a text area containing "Sample text"
     *
     * @param stage The stage that contains the content
     */
    @Override public void start(Stage stage) throws Exception{
        // load the fxml file and get the root
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/proj2FengHangHanssenJiang/Main.fxml"));
        Parent root = loader.load();

        // initialize a scene and the css file
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add("/proj2FengHangHanssenJiang/Main.css");

        // configure the stage
        stage.setTitle("FengHangHanssenJiang's Project 2");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

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

    /**
     * Main function of Main class
     *
     * @param args command line argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}
