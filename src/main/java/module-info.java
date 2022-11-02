module com.owocyp.mailapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.mail;
    requires java.desktop;


    opens com.owocyp.mailapp to javafx.fxml;
    exports com.owocyp.mailapp;
}