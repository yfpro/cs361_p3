/*
 * File: Controller.java
 * Names: (Proj 1-2) Liwei Jiang, Yi Feng, Jackie Hang, Paige Hanssen
 *        (Proj 3) Yi Feng, Melody Mao, Danqing Zhao, Robert Durst
 * F18 CS361 Project 3
 * This file contains the controller methods that define the functionality
 * for the window elements in Main.fxml.
 * Date: 09/25/2018
 */

package proj3DurstFengMaoZhao;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class Controller{


    // hello button specified in Main.fxml
    @FXML Button helloButton;
    // goodbye button specified in Main.fxml
    @FXML Button goodbyeButton;
    // tab pane containing text areas for open files, specified in Main.fxml
    @FXML TabPane tabPane;


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
     * Create a dialog that shows information about the program
     *
     * @param event ActionEvent object
     */
    @FXML void handleAboutButtonAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText("Some information about us...");
        alert.setContentText("Authors: Yi Feng, Melody Mao, Danqing Zhao, Robert Durst");

        alert.showAndWait();

    }

    
    /**
     * Opens a save dialog for the user to specify a filename
     * and directory, then writes the new file
     * @param event ActionEvent object
     */
    @FXML void handleSaveAsButtonAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save the file as...");
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                Tab tab = tabPane.getSelectionModel().getSelectedItem();
                TextArea textArea = (TextArea) tab.getContent();
                writer.write(textArea.getText());
                writer.close();
                tab.setUserData(file.toString());
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }

    }

    
    /**
     * Saves updates to pre-existing file, or if not previously
     * saved, opens a save dialog for the user to specify a filename
     * and directory, then writes the new file
     * @param event ActionEvent object
     */
    @FXML void handleSaveButtonAction(ActionEvent event){

        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String fileName = (String)tab.getUserData();
        
        //handle as unsaved file
        if (fileName == null) {
            handleSaveAsButtonAction(event);
        }
        
        //handle as previously saved file
        else{
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                TextArea textArea = (TextArea) tab.getContent();
                writer.write(textArea.getText());
                writer.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }


    /**
     * Responds to user clicks for each menu item under the edit
     * MenuBar drop down. Utilizes id from fxml to determine which
     * action to execute.
     * @param event ActionEvent object
     */
    @FXML void handleEditAction(ActionEvent event){

        // capture data relevent to the triggered action
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        TextArea textArea = (TextArea) tab.getContent();
        MenuItem clickedItem = (MenuItem) event.getTarget();
        
        switch(clickedItem.getId()) {
            case "undoMenuButton":  
                textArea.undo();
                break;
            case "redoMenuButton":  
                textArea.redo();
                break;
            case "cutMenuButton":  
                textArea.cut();
                break;
            case "copyMenuButton":  
                textArea.copy();
                break;
            case "pasteMenuButton":  
                textArea.paste();
                break;
            case "selectAllMenuButton":  
                textArea.selectAll();
                break;
            default:
                System.out.println("Unexpected event!");
        }
    }
    
}