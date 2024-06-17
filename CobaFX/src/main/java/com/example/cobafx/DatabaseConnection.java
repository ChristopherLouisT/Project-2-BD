package com.example.cobafx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public static Connection db;

    public static Connection getConnection() {
        String dbName = "postgres";
        String user = "postgres";
        String pass = "31102005okto.";
        String url = "jdbc:postgresql://localhost:5432/" + dbName;

        try{
            Class.forName("org.postgresql.Driver");
            System.out.println(url);
            db = DriverManager.getConnection(url, user, pass);
            System.out.println("Aman");
        }catch (Exception e){
            e.printStackTrace();
        }

        return db;
    }

//    public static Statement getStatement() throws SQLException {
//        return getConnection().createStatement();
//    }
}
