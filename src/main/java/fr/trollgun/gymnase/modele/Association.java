package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class Association implements Modele{

    private final String refAsso;

    private ObjectProperty<String> adresse;

    private ObjectProperty<String> nomResponsable;

    private ObjectProperty<String> ville;


    public Association(String refAsso, String ville, String adresse, String nomResponsable) {
        this.refAsso = refAsso;
        this.adresse = new SimpleObjectProperty<>(adresse);
        this.nomResponsable = new SimpleObjectProperty<>(nomResponsable);
        this.ville = new SimpleObjectProperty<>(ville);
    }

    public String getRefAsso() {
        return refAsso;
    }

    public String getAdresse() {
        return adresse.getValue();
    }

    public void setAdresse(String adresse) {
        this.adresse.setValue(adresse);
    }

    public String getNomResponsable() {
        return nomResponsable.getValue();
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable.setValue(nomResponsable);
    }

    public String getVille() {
        return ville.getValue();
    }

    public void setVille(String ville) {
        this.ville.setValue(ville);
    }

    public static ObservableList<Association> getToutAssociation(Connection connection){
        ObservableList<Association> associationObservableList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("CALL getAssociation()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Association association = new Association(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                association.adresse.addListener((observableValue, oldValue, newValue) -> {
                    try {
                        HelloApplication.raiseFlagDatabaseEdit();
                        CallableStatement callableStatement = connection.prepareCall("CALL `updateAdresseFromAssociation` (?,?)");
                        callableStatement.setString(1, association.refAsso);
                        callableStatement.setString(2, newValue);
                        System.out.println(callableStatement.executeUpdate());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                association.nomResponsable.addListener((observableValue, oldValue, newValue) -> {
                    try {
                        HelloApplication.raiseFlagDatabaseEdit();
                        CallableStatement callableStatement = connection.prepareCall("CALL `updateNomReponsableFromAssociation` (?,?)");
                        callableStatement.setString(1, association.refAsso);
                        callableStatement.setString(2, newValue);
                        System.out.println(callableStatement.executeUpdate());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                association.ville.addListener((observableValue, oldValue, newValue) -> {
                    try {
                        HelloApplication.raiseFlagDatabaseEdit();
                        CallableStatement callableStatement = connection.prepareCall("CALL `updateVilleFromAssociation` (?,?)");
                        callableStatement.setString(1, association.refAsso);
                        callableStatement.setString(2, newValue);
                        System.out.println(callableStatement.executeUpdate());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                associationObservableList.add(association);
            }
            return associationObservableList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return refAsso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Association that = (Association) o;
        return Objects.equals(refAsso, that.refAsso) && Objects.equals(adresse, that.adresse) && Objects.equals(nomResponsable, that.nomResponsable) && Objects.equals(ville, that.ville);
    }

    public boolean isExist(List<Association> associations){
        for (Association association : associations){
            if(this.getRefAsso().equals(association.getRefAsso())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removed() {
    }

    @Override
    public void add() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL addAssociation(?,?,?,?)");
        statement.setString(1, refAsso);
        statement.setString(2, adresse.getValue());
        statement.setString(3 ,nomResponsable.getValue());
        statement.setString(4, ville.getValue());
        statement.executeUpdate();
        statement.close();
    }
}
