package com.mycompany.oracleveri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class OracleOperations {

    public static void main(String[] args) {
        String jdbcURL = "jdbc:oracle:thin:@localhost:1521/xe";
        String username = "sys as sysdba";
        String password = "your_password";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database");

                // PUT operation: Insert 100,000 random numbers
               // long putStartTime = System.currentTimeMillis();
               // insertRandomNumbers(connection, 100000);
                //long putEndTime = System.currentTimeMillis();
                //System.out.println("Time taken for PUT operation: " + (putEndTime - putStartTime) + " ms");

                // GET operation: Retrieve 20,000 random numbers
               long getStartTime = System.currentTimeMillis();
               retrieveRandomNumbers(connection, 100000);
               long getEndTime = System.currentTimeMillis();
               System.out.println("Time taken for GET operation: " + (getEndTime - getStartTime) + " ms");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertRandomNumbers(Connection connection, int count) throws SQLException {
        String insertSQL = "INSERT INTO NUMBERS (VALUE) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            Random random = new Random();

            for (int i = 0; i < count; i++) {
                int number = random.nextInt(100000);
                preparedStatement.setInt(1, number);
                preparedStatement.executeUpdate();
            }
        }
    }

    private static void retrieveRandomNumbers(Connection connection, int count) throws SQLException {
        String selectSQL = "SELECT * FROM (SELECT * FROM NUMBERS ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, count);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    int value = resultSet.getInt("VALUE");
                    System.out.println("ID: " + id + ", Value: " + value);
                }
            }
        }
    }
}
