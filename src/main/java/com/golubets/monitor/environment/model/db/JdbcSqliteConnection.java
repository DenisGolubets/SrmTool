package com.golubets.monitor.environment.model.db;

import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by golubets on 20.08.2016.
 */
public class JdbcSqliteConnection implements DbConnector {
    private Connection conn;
    private final String file = "./db/data.db";
    private static final Logger log = Logger.getLogger(JdbcSqliteConnection.class);
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private final String CREATE_TABLE_ARDUINO = "CREATE TABLE `arduino` (" +
            "`id`INTEGER NOT NULL PRIMARY KEY UNIQUE," +
            "`name`TEXT NOT NULL" +
            ");";
    private final String CREATE_TABLE_DATE = "CREATE TABLE IF NOT EXISTS `date` (" +
            "`id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "`ardiuino_id`INTEGER NOT NULL," +
            "`date_time`TEXT NOT NULL," +
            "`temp`INTEGER NOT NULL," +
            "`hum`INTEGER NOT NULL," +
            "FOREIGN KEY (`ardiuino_id`) REFERENCES `arduino`(id)" +
            ");";

    //create default database
    public JdbcSqliteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException e) {
            log.error("Class not found", e);
        } catch (SQLException e) {
            log.error("Sql Exception", e);
        } catch (UnsatisfiedLinkError err) {
            log.error(err);
        }
    }

    //creating database in the specified path
    public JdbcSqliteConnection(String filePath) {
        try (Statement st = conn.createStatement()) {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
        } catch (ClassNotFoundException e) {
            log.error("Class not found", e);
        } catch (SQLException e) {
            log.error("Sql Exception", e);
        }
    }

    public void initialization(Integer arduinoId, String name) {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM arduino WHERE id=%s", arduinoId));
            if (rs.next()) {
                if (rs.getInt(1) == arduinoId) {
                    if (!(rs.getString(2).equals(name))) {
                        String query = String.format("UPDATE NAME SET name='%s' WHERE id=%s", name, arduinoId);
                        st.executeUpdate(query);
                    }
                }
            } else {
                String query = String.format("INSERT INTO 'arduino' ('id', 'name') VALUES ('%s','%s')", arduinoId, name);
                st.execute(query);
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Sql Exception", e);
        }
    }

    public void persist(Integer arduinoId, Date date, double t, double h) {

        String resultSQL = String.format("INSERT INTO 'date' ('ardiuino_id', 'date_time', 'temp', 'hum') VALUES ('%s','%s','%s','%s');",
                arduinoId, DATE_FORMAT.format(date), t, h);
        try (Statement st = conn.createStatement()) {
            st.execute(resultSQL);
        } catch (SQLException e) {
            log.error("Sql Exception", e);
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Sql Exception", e);
        }
    }
}
