package com.owocyp.mailapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ControllerLogin {
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorText;
    @FXML private VBox parent;
    @FXML
    public void initialize() {
        try {
            Database.initDatabaseConnectionPool();
            okButton.setOnAction(event -> {
                OkButton();
            });
            cancelButton.setOnAction(event -> {
                Close();
            });

            parent.setOnKeyPressed(event -> {
                if(event.getCode() == KeyCode.ENTER){
                    OkButton();
                }
                if(event.getCode() == KeyCode.ESCAPE){
                    Close();
                }
            });
        }finally{
            Database.closeDatabaseConnectionPool();
        }
    }
    
    public ControllerLogin() {
    }

    @FXML
    private void OkButton()  {
        if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()){
            try {
                Database.initDatabaseConnectionPool();
                Database.readData2();
                while (Database.resultSet.next()){
                    String usernameResult = Database.resultSet.getString(1);
                    String passwordResult = Database.resultSet.getString(2);
                    String mailResult = Database.resultSet.getString(3);
                    if((usernameResult.equals(usernameField.getText()) || mailResult.equals(usernameField.getText())) && passwordResult.equals(passwordField.getText())){
                        System.out.println("Connection...");
                        errorText.setText("Connection...");
                        Main.usernameList.add(usernameField.getText());
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                        try{
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MailApp.fxml"));
                            Parent root = fxmlLoader.load();
                            Stage stage1 = new Stage();
                            root.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
                            stage1.setTitle("MailApp");
                            stage1.setScene(new Scene(root));
                            stage1.setResizable(false);
                            stage1.show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        break;
                    }else{
                        errorText.setText("Wrong username or password");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            errorText.setText("One or both fields ain't fill");
        }
    }

    @FXML
    private void Close(){
        Stage stage = (Stage) parent.getScene().getWindow();
        Database.closeDatabaseConnectionPool();
        Main.usernameList.clear();
        stage.close();
        System.exit(1);
    }
}