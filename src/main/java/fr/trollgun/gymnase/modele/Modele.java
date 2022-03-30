package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;

import java.sql.SQLException;

public interface Modele {

    default void removed() throws SQLException{
        HelloApplication.raiseFlagDatabaseEdit();
    }

    default void add() throws SQLException{
        HelloApplication.raiseFlagDatabaseEdit();
    }

}
