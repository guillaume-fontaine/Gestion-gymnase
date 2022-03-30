package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Objects;

public class Sport implements Modele{

    private final String nomSport;

    public Sport(String nomSport) {
        this.nomSport = nomSport;
    }

    public String getNomSport() {
        return nomSport;
    }

    public static ObservableList<Sport> getToutSport(Connection connection){
        ObservableList<Sport> sportObservableList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("CALL getSport()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                sportObservableList.add(new Sport(resultSet.getString(1)));
            }
            return sportObservableList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return nomSport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sport sport = (Sport) o;
        return Objects.equals(nomSport, sport.nomSport);
    }

    @Override
    public void removed() {
    }

    @Override
    public void add() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL addSport(?)");
        statement.setString(1, nomSport);
        statement.executeUpdate();
        statement.close();
    }

}
