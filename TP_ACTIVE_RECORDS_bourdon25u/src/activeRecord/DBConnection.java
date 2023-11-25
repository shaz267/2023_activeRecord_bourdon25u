package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection{
    public static Connection instance;
    public static String userName = "root";
    public static String password = "";
    public static String serverName = "localhost";
    public static String portNumber = "3306";
    public static String dbName = "testpersonne";

    public static synchronized Connection getConnection(){

        String DBName = null;
        if (instance != null) {

            try {
                DBName = instance.getCatalog();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(instance == null || !dbName.equals(DBName)){
            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", userName);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":";
            urlDB += portNumber + "/" + dbName;
            try {
                instance = DriverManager.getConnection(urlDB, connectionProps);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Setter de la variable dbName
     * @param nomDB le nom de la base de données
     */
    public static void setNomDB(String nomDB){
        dbName = nomDB;
    }

    /**
     * Getter de la variable dbName
     * @return le nom de la base de données
     */
    public static String getDbName() {
        return dbName;
    }
}
