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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Node;
import javafx.stage.Window;

//import com.sun.java.swing.action.ExitAction;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * This class contains event handlers for the buttons and the menu items.
 * Buttons: Hello, Goodbye
 * Menu items: Exit, New, About, Save As, Save, Close, Edit, Open
 *
 * @author  Robert Durst, Yi Feng, Melogy Mao, Danqing Zhao
 */

public class Controller{


    // hello button specified in Main.fxml
    @FXML Button helloButton;
    // goodbye button specified in Main.fxml
    @FXML Button goodbyeButton;
    // tab pane containing text areas for open files, specified in Main.fxml
    @FXML TabPane tabPane;

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
     * Exit the program
     * It will call a handleClose method for all tabs in tab pane to ask if it needs to be saved.
     * @param event ActionEvent object
     */
    @FXML void handleExitAction(ActionEvent event) {
        Integer size = tabPane.getTabs().size();
        for(int i = 0; i< size; i++){
            handleClose(tabPane.getTabs().get(0), event);
        }
        if(tabPane.getTabs().size() == 0){
            System.exit(0);
        }
    }


    /** 
     * Create a new tab named 'New file' with a text area
     * 
     * @param even ActionEvent object
     */
    @FXML void handleNewAction(ActionEvent event) {
        Tab tab = new Tab();
        tab.setText("New file");
        tabPane.getTabs().add(tab);
        TextArea ta = new TextArea();
        ta.setText("Sample text");
        tab.setContent(ta);
    }


    /**
     * Create a dialog that shows information about the program
     *
     * @param event ActionEvent object
     */
    @FXML void handleAboutAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText("Some information about us...");
        alert.setContentText("Authors:\nRobert Durst, Yi Feng, Melody Mao, Danqing Zhao\njoyful programmers who code happily together everyday :)");

        alert.showAndWait();

    }

    
    /**
     * Opens a save dialog for the user to specify a filename
     * and directory, then writes the new file
     * @param event ActionEvent object
     */
    @FXML void handleSaveAsAction(ActionEvent event){
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
                tab.setText(file.toString());
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
    @FXML void handleSaveAction(ActionEvent event){

        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String fileName = (String)tab.getUserData();
        
        //handle as unsaved file
        if (fileName == null) {
            handleSaveAsAction(event);
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
    *   The close menu button behaviour. Call handleClose for current selected Tab.
    *
    *   @param event ActionEvent object
    */
    @FXML void handleCloseAction(ActionEvent event){
        Tab thisTab = tabPane.getSelectionModel().getSelectedItem();
        handleClose(thisTab, event);
    }

    /** 
    *   Once called check if the input tab has been saved into a file, 
    *   if so compares the file content with the content in textarea.
    *   If the contents are different ask if the user wants to save the tab before close it.
    */
    
    void handleClose(Tab thisTab, ActionEvent event){
        Boolean saved = false;
        if(thisTab.getUserData() != null){
            try{
                String fileName = (String) thisTab.getUserData();
                TextArea ta = (TextArea) thisTab.getContent();
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String fileText = reader.readLine();
                String taText = ta.getText();
                if(fileText.equals(taText)){
                    saved = true;
                    System.out.println("saved");
                    tabPane.getTabs().remove(thisTab);
                }
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if(saved == false){
            Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Want to save before exit?",
                ButtonType.YES,
                ButtonType.NO,
                ButtonType.CANCEL
            );
            alert.setTitle("Alert");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                handleSaveAction(event);
                tabPane.getTabs().remove(thisTab);
            }
            if(alert.getResult() == ButtonType.NO){
                tabPane.getTabs().remove(thisTab);
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

     /**
     * 
     * @param event ActionEvent object
     */
    @FXML void handleOpenAction(ActionEvent event){
        // create a new tab
        Tab tab = new Tab();

        // add tab to tab pane
        tabPane.getTabs().add(tab); 

        // set new tab as the focused on tab
        tabPane.getSelectionModel().select(tab);

        // define new textarea and add to new tab
        TextArea textArea = new TextArea();
        tab.setContent(textArea);
        
        // instantiate and define a filechooser
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Show save file dialog
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            String fileText = getFileContentString(file);
            textArea.setText(fileText);
            tab.setText(file.getAbsolutePath());
            tab.setUserData(file.getAbsolutePath());
        }
    }

    // TO FIX because copy-pasta
    private String getFileContentString(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
         
        try {
            // attempt to read a new file and instantiate a
            // new buffer Reader
            bufferedReader = new BufferedReader(new FileReader(file));
            
            // read through the file line by line and append
            // to the buffer to return
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }
 
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 

        // once we are done, close the buffer reader
        try {
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
         
        return stringBuffer.toString();
    }

}