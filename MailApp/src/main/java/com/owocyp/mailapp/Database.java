package com.owocyp.mailapp;

import com.zaxxer.hikari.HikariDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;
import java.util.Properties;

public class Database {

    private static HikariDataSource dataSource;
    public static ResultSet resultSet;

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
        try (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("""
                        SELECT `username`, `password`, `mail`
                        FROM `account`
                    """)){
                resultSet = statement.executeQuery();

            }
        }
    }

    public static void updateData(String username, int messageSend) throws SQLException {
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
        try{
            FileReader reader = new FileReader("src\\main\\resources\\config.properties");
            Properties properties = new Properties();
            properties.load(reader);
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytesUrl = decoder.decode(url);
            byte[] bytesUsername = decoder.decode(username);
            byte[] bytesPassword = decoder.decode(password);

            System.out.println("Connection to database...");
            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(new String(bytesUrl));
            dataSource.setUsername(new String(bytesUsername));
            dataSource.setPassword(new String(bytesPassword));
            System.out.println("Connection established");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void closeDatabaseConnectionPool() {
        dataSource.close();
    }
}
