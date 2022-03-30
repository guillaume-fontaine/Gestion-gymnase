package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Objects;

public class Accueillir implements Modele {

    private String refSalle;
    private String nomSport;

    public Accueillir(String refSalle, String nomSport) {
        this.refSalle = refSalle;
        this.nomSport = nomSport;
    }

    public String getRefSalle() {
        return refSalle;
    }

    public String getNomSport() {
        return nomSport;
    }

    public static ObservableList<Accueillir> getToutAccueillir(Connection connection) {
        ObservableList<Accueillir> accueillirList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("Call getAccueillir()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accueillirList.add(new Accueillir(resultSet.getString(1), resultSet.getString(2)));
            }
            return accueillirList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Salle : "+refSalle+"\n"
                +"Sport : "+nomSport;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accueillir that = (Accueillir) o;
        return Objects.equals(refSalle, that.refSalle) && Objects.equals(nomSport, that.nomSport);
    }

    @Override
    public void removed() throws SQLException {
        Modele.super.removed();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL removeAccueillir(?,?)");
        statement.setString(1, refSalle);
        statement.setString(2, nomSport);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void add() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall(" CALL addAccueillir(?, ?)");
        statement.setString(1, refSalle);
        statement.setString(2, nomSport);
        statement.executeUpdate();
        statement.close();
    }
}
