package com.owocyp.mailapp;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static HikariDataSource dataSource;
    private static String url = "jdbc:mariadb://---:3307/mail_app";
    private static String username = "---";
    private static String password = "---";
    public static ResultSet resultSet;

    public static List<String> usernameList = new ArrayList<>();
    public static List<String> mailList = new ArrayList<>();
    public static List<String> passwordList = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
        try {
            initDatabaseConnectionPool();
        }finally{
            closeDatabaseConnectionPool();
        }
    }

    public static void createData(String username, String password, String mail) throws SQLException {
        System.out.println("Creating data...");
        int rowsInserted;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO `account` (`username`, `password`, `mail`)
                        VALUES (?, ?, ?)
                    """)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, mail);
                rowsInserted = statement.executeUpdate();
            }
        }
        System.out.println("Rows inserted:" + rowsInserted);
    }

    public static void readData() throws SQLException {
        System.out.println("Reading data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        SELECT `username`, `password`, `mail`
                        FROM `account`
                        ORDER BY `ID` DESC
                    """)) {
                ResultSet resultSet = statement.executeQuery();
                boolean empty = true;
                while (resultSet.next()) {
                    String usernameResult = resultSet.getString(1);
                    String passwordResult = resultSet.getString(2);
                    String mailResult = resultSet.getString(3);
                    usernameList.add(usernameResult);
                    passwordList.add(passwordResult);
                    mailList.add(mailResult);
                    empty = false;
                }
                System.out.println(usernameList + " | " + passwordList + " | " + mailList);
                if (empty) {
                    System.out.println("\t (no data)");
                }
            }
        }
    }

    public static void readData2() throws SQLException{
        System.out.println("Reading data...");
        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("""
                        SELECT `username`, `password`, `mail`
                        FROM `account`
                    """)){
                resultSet = statement.executeQuery();

            }
        }
    }

    public static void updateData(String username, String newPassword, String mail) throws SQLException {
        System.out.println("Updating data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        UPDATE `account`
                        SET `password` = ?
                        WHERE `username` = ?
                    """)) {
                statement.setString(1, newPassword);
                statement.setString(2, username);
                int rowsUpdated = statement.executeUpdate();
                System.out.println("Rows updated: " + rowsUpdated);
            }
        }

    }

    public static void updateData2(String username, int messageSend) throws SQLException {
        System.out.println("Updating data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        UPDATE `account`
                        SET `sending message` = ?
                        WHERE `username` = ?
                    """)) {
                statement.setInt(1, messageSend);
                statement.setString(2, username);
                statement.executeUpdate();
            }
        }

    }

    public static void deleteData(String nameExpression) throws SQLException {
        System.out.println("Deleting data...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM `account`
                    WHERE `username` LIKE ?
                    """)) {
                statement.setString(1, nameExpression);
                int rowsDeleted = statement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
            }
        }
    }

    public static void initDatabaseConnectionPool() {
        System.out.println("Connection to database...");
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        System.out.println("Connection established");
    }

    public static void closeDatabaseConnectionPool() {
        dataSource.close();
    }
}
