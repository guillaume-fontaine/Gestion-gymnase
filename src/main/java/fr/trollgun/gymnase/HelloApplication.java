package fr.trollgun.gymnase;

import fr.trollgun.gymnase.modele.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloApplication extends Application {

    private static Database database;
    private static String color;
    private static boolean databaseEdit = false;

    public static void raiseFlagDatabaseEdit() {
        databaseEdit = true;
    }

    public static void dropFlagDatabaseEdit() {
        databaseEdit = false;
    }

    @Override
    public void start(Stage stage) throws IOException {
        database = new Database();
        color = "-fx-first-color: #da5555;";

        Locale locale = new Locale("fr", "FR");
        ResourceBundle bundle = ResourceBundle.getBundle("gymnase", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gymnase-view.fxml"), bundle);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(bundle.getString("gymnase.title.text"));
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            if(databaseEdit){
                if(Toolbox.sendSaveRequest()){
                    try {
                        HelloApplication.getDatabase().getConnection().commit();
                        HelloApplication.dropFlagDatabaseEdit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static Database getDatabase(){
        return database;
    }

    public static String getColor() {
        return color;
    }

    public static void setColor(String color) {
        HelloApplication.color = "-fx-first-color: "+color+";";
    }

    public static void main(String[] args) {
        launch();
    }
}