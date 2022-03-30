package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Objects;

public class Pratiquer implements Modele{

    private String refAsso;
    private String nomSport;

    public Pratiquer(String refAsso, String nomSport) {
        this.refAsso = refAsso;
        this.nomSport = nomSport;
    }

    public String getRefAsso() {
        return refAsso;
    }

    public String getNomSport() {
        return nomSport;
    }

    public static ObservableList<Pratiquer> getToutPratiquer(Connection connection) {
        ObservableList<Pratiquer> pratiquerList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("Call getPratiquer()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                pratiquerList.add(new Pratiquer(resultSet.getString(1), resultSet.getString(2)));
            }
            return pratiquerList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Association : "+refAsso+"\n"+
                "Sport : "+nomSport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pratiquer pratiquer = (Pratiquer) o;
        return Objects.equals(refAsso, pratiquer.refAsso) && Objects.equals(nomSport, pratiquer.nomSport);
    }

    @Override
    public void removed() throws SQLException {
        Modele.super.removed();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL removePratiquer(?,?)");
        statement.setString(1, refAsso);
        statement.setString(2, nomSport);
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void add() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall(" CALL addPratiquer(?, ?)");
        statement.setString(1, refAsso);
        statement.setString(2, nomSport);
        statement.executeUpdate();
        statement.close();
    }
}
