package com.owocyp.mailapp;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private TextField to;
    @FXML private TextField subject;
    @FXML private TextArea mailText;
    @FXML private ComboBox<Integer> number;
    @FXML private Label errorText;
    @FXML private VBox parent;

    public Controller() {
    }

    @FXML
    public void initialize() {
        // 1 to 5 in 1 by 1
        for(int i=1; i!=5; i++){
            number.getItems().add(i);
        }

        // 5 to 50 in 5 by 5
        for(int i=5; i!=50; i=i+5){
            number.getItems().add(i);
        }

        // 50 to 100 in 10 by 10
        for(int i=50; i!=110; i=i+10){
            number.getItems().add(i);
        }

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
    }
    @FXML
    private void OkButton(){
        errorText.setText("Sending messages...");
        if(!to.getText().isEmpty() && !subject.getText().isEmpty() && !mailText.getText().isEmpty() && !number.getSelectionModel().isEmpty()){
            for(int i = 0; i!=number.getSelectionModel().getSelectedItem(); i++) {
                SendMail.Send(to.getText(), subject.getText(), mailText.getText());
                try {
                    Database.updateDataSendingMessages(Main.usernameList.get(0), i);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            errorText.setText("Sending done...");
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

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MailAppLogin.fxml"));
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
    }
}
