package fr.trollgun.gymnase.modele;

import fr.trollgun.gymnase.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class Salle implements Modele{

    private final String refSalle;

    private float surface;

    private String typeRevetement;

    public Salle(String refSalle, float surface, String typeRevetement) {
        this.refSalle = refSalle;
        this.surface = surface;
        this.typeRevetement = typeRevetement;
    }


    public String getRefSalle() {
        return refSalle;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
        try {
            editSurface();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTypeRevetement() {
        return typeRevetement;
    }

    public void setTypeRevetement(String typeRevetement) {
        this.typeRevetement = typeRevetement;
        try {
            editRevetement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Salle> getToutSalle(Connection connection){
        ObservableList<Salle> salleObservableList = FXCollections.observableArrayList();
        try {
            CallableStatement statement = connection.prepareCall("CALL getSalle()");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                salleObservableList.add(new Salle(resultSet.getString(1), resultSet.getFloat(2), resultSet.getString(3)));
            }
            return salleObservableList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return refSalle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return Float.compare(salle.surface, surface) == 0 && Objects.equals(refSalle, salle.refSalle) && Objects.equals(typeRevetement, salle.typeRevetement);
    }

    public boolean isExist(List<Salle> salles){
        for (Salle salle : salles){
            if(this.getRefSalle().equals(salle.getRefSalle())){
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
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL addSalle(?,?,?)");
        statement.setString(1, refSalle);
        statement.setFloat(2, surface);
        statement.setString(3, typeRevetement);
        statement.executeUpdate();
        statement.close();
    }

    public void editSurface() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL updateSurfaceFromSalle(?,?)");
        statement.setString(1, refSalle);
        statement.setFloat(2, surface);
        statement.executeUpdate();
        statement.close();
    }

    public void editRevetement() throws SQLException {
        Modele.super.add();
        CallableStatement statement = HelloApplication.getDatabase().getConnection().prepareCall("CALL updateRevetementFromSalle(?,?)");
        statement.setString(1, refSalle);
        statement.setString(2, typeRevetement);
        statement.executeUpdate();
        statement.close();
    }
}
