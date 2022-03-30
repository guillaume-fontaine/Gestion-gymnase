module fr.trollgun.gymnase {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.datatransfer;
    requires java.desktop;
    requires java.sql;
    requires org.mariadb.jdbc;
    requires FranzXaver;


    opens fr.trollgun.gymnase to javafx.fxml;
    exports fr.trollgun.gymnase;
    exports fr.trollgun.gymnase.control to javafx.fxml;
    opens fr.trollgun.gymnase.controller to javafx.fxml;
    opens fr.trollgun.gymnase.modele to javafx.base;
    exports fr.trollgun.gymnase.controller;
    //--add-exports java.datatransfer/java.awt=fr.trollgun.gymnase
}