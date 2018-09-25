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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.HashMap;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /** Keep a cache of opened/saved tabs
     *      key   - TextArea id
     *      value - SHA256(TextArea text)  
     */ 
    HashMap<String, String> tabContentCache = new HashMap<>();

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
     * Exit the program. Calls a handleClose method for all
     * tabs in tab pane to ask if it needs to be saved.
     * 
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
     * Create a new tab named 'New file' with a text area.
     * 
     * @param even ActionEvent object
     */
    @FXML void handleNewAction(ActionEvent event) {
        // instantiate a new Tab
        Tab tab = new Tab();
        tab.setText("New file");

        // add to tabPane
        tabPane.getTabs().add(tab);

        // set the new tab as the focus
        tabPane.getSelectionModel().select(tab);

        // instantiate the TextArea
        TextArea textArea = new TextArea();
        textArea.setText("Sample text");

        // add TextArea to tab
        tab.setContent(textArea);
        
        // Set a unique Id for the TextArea
        String id = UUID.randomUUID().toString();
        textArea.setId(id);
    }

    /**
     * Create and oepn a dialog that displays information about 
     * the program.
     *
     * @param event ActionEvent object
     */
    @FXML void handleAboutAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText("Some information about us...");
        alert.setContentText("Authors:\nRobert Durst, Yi Feng,Melody Mao, Danqing Zhao\njoyful programmers who code happily together everyday :)");

        alert.showAndWait();
    }
    
    /**
     * Opens a save dialog for the user to specify a filename and
     * directory, then writes the new file
     * 
     * @param event ActionEvent object
     */
    @FXML void handleSaveAsAction(ActionEvent event) {
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

                 // add TextArea to hashmap
                 appendTabContentCache(textArea);
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
    @FXML void handleSaveAction(ActionEvent event) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String fileName = (String)tab.getUserData();
        
        //handle as unsaved file
        if (fileName == null) {
            handleSaveAsAction(event);
        } else {
            //handle as previously saved file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                TextArea textArea = (TextArea) tab.getContent();
                writer.write(textArea.getText());
                writer.close();

                // add TextArea to hashmap
                appendTabContentCache(textArea);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /** 
    * Executes when close menu button selected. 
    *
    * @param event ActionEvent object
    */
    @FXML void handleCloseAction(ActionEvent event) {
        Tab thisTab = tabPane.getSelectionModel().getSelectedItem();
        handleClose(thisTab, event);
    }

    /** 
     * Checks against the tabContentCache if:
     *      a) the current tab has been saved yet
     *      b) the current tab has changed since last save
     * 
     * This method utilizes the tabContentCache for quick
     * and efficient lookups.
     * 
     * If the tab hasn't been saved, or has changed since last
     * save, then it asks the user if he/she wants to save the
     * tab's TextArea's contents before closing it.
     * 
     * @param event ActionEvent object
     * @param thisTab Tab object
    */
    void handleClose(Tab tab, ActionEvent event){
        TextArea textArea = (TextArea) tab.getContent();
        String hashedText = hashAString(textArea.getText());
        String id = textArea.getId();

        // check if (a) and (b)
        if (tabContentCache.containsKey(id) && tabContentCache.get(id).equals(hashedText)) {
            tabPane.getTabs().remove(tab);
            removeTabContentCache(textArea);
            return;
        } 

        Alert alert = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Want to save before exit?",
            ButtonType.YES,
            ButtonType.NO,
            ButtonType.CANCEL
        );
        alert.setTitle("Alert");

        // display alert
        alert.showAndWait();

        // handle user response
        if(alert.getResult() == ButtonType.YES){
            handleSaveAction(event);
            tabPane.getTabs().remove(tab);
        } else if(alert.getResult() == ButtonType.NO){
            tabPane.getTabs().remove(tab);
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
      * Presents user with option to open a (txt) file from their
      * local directory and then displays the text of the file in
      * a new tab.
      * 
      * @param event ActionEvent object
      */
    @FXML void handleOpenAction(ActionEvent event){
        // instantiate and define a filechooser
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        
        //Show save file dialog
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            // create a new tab
            Tab tab = new Tab();

            // add tab to tab pane
            tabPane.getTabs().add(tab); 

            // set new tab as the focused on tab
            tabPane.getSelectionModel().select(tab);

            // define new textarea and add to new tab
            TextArea textArea = new TextArea();
            tab.setContent(textArea);

            String fileText = getFileContentString(file);
            textArea.setText(fileText);
            tab.setText(file.getAbsolutePath());
            tab.setUserData(file.getAbsolutePath());

            // Set a unique Id for the thing
            String id = UUID.randomUUID().toString();
            textArea.setId(id);

            // add TextArea to hashmap
            appendTabContentCache(textArea);
        }
    }

    /**
     * Takes in a file, attempts to read the text from the file
     * and return it as a String.
     * 
     * Modified and borrowed code from:
     * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
     * 
     * @param  file    File object
     * @return String  contents of the file
     */
    String getFileContentString(File file){
        StringBuilder stringBuffer = new StringBuilder();
        FileReader reader = null;
         
        try {
            // attempt to read a new file and instantiate a
            // new buffer Reader
            reader = new FileReader(file);
            
            // read through the file character by character and append
            // to the buffer to return
            int chars = 0;
            while ((chars = reader.read()) != -1) {
                stringBuffer.append((char)chars);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(IOException io) {
            System.out.println(io.getMessage());
        }

        // once we are done, close the buffer reader
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
         
        return stringBuffer.toString();
    }

    /**
     * Updates/adds a textarea's content to the cache.
     * 
     * @param textArea TextArea object
     */
    private void appendTabContentCache(TextArea textArea) {
        // capture data related to TextArea
        String id = textArea.getId();
        String content = textArea.getText();

        tabContentCache.put(id, hashAString(content));
    }

    /**
     * Removes a textarea's content to the cache.
     * 
     * @param textArea TextArea object
     */
    private void removeTabContentCache(TextArea textArea) {
        // capture data related to TextArea
        String id = textArea.getId();

        tabContentCache.remove(id);
    }

    /**
     * Applies SHA-256 to a string.
     * 
     * @param text     a normal String 
     * @return String  a hashed String
     */
    private String hashAString(String text) {
        // default returns input
        String result = text;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            result = new String(messageDigest.digest());
          }
          catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
          }

        return result;
    }
}