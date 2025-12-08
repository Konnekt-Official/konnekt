package konnekt.connection;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class Database {

    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("config.properties"));

                String url = "jdbc:mysql://" +
                        props.getProperty("DB_HOST") + ":" +
                        props.getProperty("DB_PORT") + "/" +
                        props.getProperty("DB_NAME");

                conn = DriverManager.getConnection(
                        url,
                        props.getProperty("DB_USER"),
                        props.getProperty("DB_PASSWORD")
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}