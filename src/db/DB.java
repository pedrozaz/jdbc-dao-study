package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                connection = DriverManager.getConnection(url, props);
            }
        }
        catch (SQLException error) {
            throw new DbException(error.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
            connection.close();
            }
        }
        catch (SQLException error) {
            throw new DbException(error.getMessage());
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException error) {
            throw new DbException(error.getMessage());
        }
    }

    public static void closeStatement(Statement statement) {
        if(statement != null ) {
            try {
                statement.close();
            }
            catch (SQLException error) {
                throw new DbException(error.getMessage());
            }
        }
    }


    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (SQLException error) {
                throw new DbException(error.getMessage());
            }
        }
    }
}
