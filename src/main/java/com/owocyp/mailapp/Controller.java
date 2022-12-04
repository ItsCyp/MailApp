package com.owocyp.mailapp;

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
import java.util.Objects;

public class Controller {

    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private TextField to;
    @FXML private TextField subject;
    @FXML private TextArea mailText;
    @FXML private Label errorText;
    @FXML private VBox parent;

    @FXML
    public void initialize() {

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
        if(!to.getText().isEmpty() && !subject.getText().isEmpty() && !mailText.getText().isEmpty()){
            SendMail.Send(to.getText(), subject.getText(), mailText.getText());
            try {
                Database.updateData(Main.usernameList.get(0), 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            errorText.setText("Sent successfully!");
        }else{
            errorText.setText("One or both fields ain't fill.");
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
            root.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toExternalForm());
            stage1.setTitle("MailApp");
            stage1.setScene(new Scene(root));
            stage1.setResizable(false);
            stage1.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
