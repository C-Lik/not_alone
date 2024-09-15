package Database;

import Exceptions.CriticalExceptionHandler;
import Exceptions.NotCriticalExceptionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

public class DBManager {

    private Connection c;
    private Statement stm;

    public DBManager(String dbFileName) {
        File file = new File(dbFileName);
        if(file.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbFileName));
                stm = c.createStatement();
            } catch (Exception e) {
                CriticalExceptionHandler.handle(e);
            }
        } else {
            CriticalExceptionHandler.handle(new FileNotFoundException("Data Base does not exists!\n"));
        }
    }


    public void executeUpdate(String command) {
        try {
            stm.executeUpdate(command);
        } catch (Exception e) {
            CriticalExceptionHandler.handle(e);
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return stm.executeQuery(query);
    }

    public void close() {
        try {
            c.close();
            stm.close();
        } catch (Exception e) {
            NotCriticalExceptionHandler.handle(e);
        }
    }
}
