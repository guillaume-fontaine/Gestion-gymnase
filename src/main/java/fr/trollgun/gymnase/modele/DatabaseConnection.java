package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.Toolbox;

import java.sql.Connection;
import java.sql.DriverManager;
import org.mariadb.jdbc.Driver;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        try{
            String url = "jdbc:mariadb://localhost/gymnase";
            String username = "root";
            String password = "";
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            Toolbox.sendError(e.getMessage(),"Base de données : échec");
            e.printStackTrace();
            System.exit(0);
        }
        return connection;
    }

}
