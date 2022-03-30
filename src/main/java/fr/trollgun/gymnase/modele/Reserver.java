package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Reserver implements Modele{

    private final String refSalle;
    private final LocalDate date;
    private final LocalTime heure;
    private String refAsso;

    public Reserver(String refSalle, LocalDate date, LocalTime heure, String refAsso) {
        this.refSalle = refSalle;
        this.date = date;
        this.heure = heure;
        this.refAsso = refAsso;
    }

    public String getRefSalle() {
        return refSalle;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public String getRefAsso() {
        return refAsso;
    }

    public void setRefAsso(String refAsso) {
        this.refAsso = refAsso;
        try {
            editRefAsso();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Reserver> getToutReserver(Connection connection){
        ObservableList<Reserver> reserverObservableList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("CALL getReservation()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                reserverObservableList.add(new Reserver(resultSet.getString(1),resultSet.getDate(2).toLocalDate(), resultSet.getTime(3).toLocalTime(),resultSet.getString(4)));
            }
            return reserverObservableList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Salle : " + refSalle + '\n' +
                "Date : " + date + '\n' +
                "Heure : " + heure + '\n' +
                "Association : " + refAsso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserver reserver = (Reserver) o;
        return Objects.equals(refSalle, reserver.refSalle) && Objects.equals(date, reserver.date) && Objects.equals(heure, reserver.heure) && Objects.equals(refAsso, reserver.refAsso);
    }


    public boolean isExist(List<Reserver> reservers){
        for (Reserver reserver : reservers){
            if(Objects.equals(refSalle, reserver.refSalle) && Objects.equals(date, reserver.date) && Objects.equals(heure, reserver.heure)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removed() throws SQLException {
        Modele.super.removed();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL removeReservation(?,?,?)");
        statement.setString(1, refSalle);
        statement.setDate(2, java.sql.Date.valueOf(date));
        statement.setTime(3, java.sql.Time.valueOf(heure));
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void add() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall(" CALL addReservation(?,?,?,?)");
        statement.setString(1, refSalle);
        statement.setDate(2, java.sql.Date.valueOf(date));
        statement.setTime(3, java.sql.Time.valueOf(heure));
        statement.setString(4, refAsso);
        statement.executeUpdate();
        statement.close();
    }

    public void editRefAsso() throws SQLException {
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall(" CALL updateRefAssoFromReservation(?,?,?,?)");
        statement.setString(1, refSalle);
        statement.setDate(2, java.sql.Date.valueOf(date));
        statement.setTime(3, java.sql.Time.valueOf(heure));
        statement.setString(4, refAsso);
        statement.executeUpdate();
        statement.close();
    }
}
