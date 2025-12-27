package konnekt.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {

    public static Connection getConnection() throws Exception {
        Properties props = new Properties();
        InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("konnekt/resources/config.properties");
        if (input == null) throw new RuntimeException("config.properties not found");

        props.load(input);
        String host = props.getProperty("DB_HOST");
        String port = props.getProperty("DB_PORT");
        String dbName = props.getProperty("DB_NAME");
        String user = props.getProperty("DB_USER");
        String password = props.getProperty("DB_PASSWORD");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
