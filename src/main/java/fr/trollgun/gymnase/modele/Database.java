package fr.trollgun.gymnase.modele;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final Connection connection = DatabaseConnection.getConnection();
    private final ObservableList<Salle> observableListSalle;
    private final ObservableList<Sport> observableListSport;
    private final ObservableList<Association> observableListAssociation;
    private final ObservableList<Accueillir> observableListAccueillir;
    private final ObservableList<Pratiquer> observableListPratiquer;
    private final ObservableList<Reserver> observableListReserver;

    public Database(){
        this.observableListSalle = Salle.getToutSalle(connection);
        this.observableListSport = Sport.getToutSport(connection);
        this.observableListAssociation = Association.getToutAssociation(connection);
        this.observableListAccueillir = Accueillir.getToutAccueillir(connection);
        this.observableListPratiquer = Pratiquer.getToutPratiquer(connection);
        this.observableListReserver = Reserver.getToutReserver(connection);
        this.observableListSalle.addListener((ListChangeListener<? super Salle>) this::addTrigger);
        this.observableListSport.addListener(((ListChangeListener<? super Sport>) this::addTrigger));
        this.observableListAssociation.addListener(((ListChangeListener<? super Association>) this::addTrigger));
        this.observableListAccueillir.addListener(((ListChangeListener<? super Accueillir>) this::addTrigger));
        this.observableListPratiquer.addListener(((ListChangeListener<? super Pratiquer>) this::addTrigger));
        this.observableListReserver.addListener(((ListChangeListener<? super Reserver>) this::addTrigger));
    }

    private void addTrigger(ListChangeListener.Change<?> change){
        while (change.next()){
            if(change.wasAdded()){
                for (Object o : change.getAddedSubList()) {
                    if(o instanceof Modele modele){
                        try {
                            modele.add();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else if(change.wasRemoved()){
                for (Object o : change.getRemoved()) {
                    if(o instanceof Modele modele){
                        try {
                            modele.removed();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public ObservableList<Salle> getObservableListSalle() {
        return observableListSalle;
    }

    public ObservableList<Sport> getObservableListSport() {
        return observableListSport;
    }

    public ObservableList<Association> getObservableListAssociation() {
        return observableListAssociation;
    }

    public ObservableList<Accueillir> getObservableListAccueillir() {
        return observableListAccueillir;
    }

    public ObservableList<Pratiquer> getObservableListPratiquer() {
        return observableListPratiquer;
    }

    public ObservableList<Reserver> getObservableListReserver() {
        return observableListReserver;
    }

    public Connection getConnection() {
        return connection;
    }

    public Salle getSalleFromRefSalle(String refSalle){
        for (Salle salle:getObservableListSalle()) {
            if(salle.getRefSalle().equals(refSalle))return salle;
        }
        return null;
    }

    public Association getAssociationFromRefAssociation(String refAsso){
        for (Association association:getObservableListAssociation()) {
            if(association.getRefAsso().equals(refAsso))return association;
        }
        return null;
    }

    public String getSportPratiquerForAssociation(String refAsso){
        StringBuilder stringBuilder = new StringBuilder("");
        for (Pratiquer pratiquer : getObservableListPratiquer()) {
            if(pratiquer.getRefAsso().equals(refAsso)){
                if(!stringBuilder.toString().equals(""))stringBuilder.append(" - ");
                stringBuilder.append(pratiquer.getNomSport());
            }
        }
        return stringBuilder.toString().equals("") ? "Pratique aucun sport" : stringBuilder.toString();
    }

    public Integer getTotalReservationAssociation(String refAsso){
        return  (int) getObservableListReserver().stream().filter(reserver -> reserver.getRefAsso().equals(refAsso)).count();
    }

    public ObservableList<Association> getObservableListAssociationCanMakeSports(String refSalle) {
        List<String> nomSports = new ArrayList<>();
        getObservableListAccueillir().stream()
                .filter(accueillir -> accueillir.getRefSalle().equals(refSalle))
                .forEach(accueillir -> nomSports.add(accueillir.getNomSport()));
        return FXCollections.observableList(getObservableListAssociation().stream().filter(association -> canDoSports(nomSports, association)).toList());
    }

    public Boolean canDoSports(List<String> nomSports, Association association){
        return (int) getObservableListPratiquer().stream().filter(pratiquer -> pratiquer.getRefAsso().equals(association.getRefAsso()))
                .filter(pratiquer -> nomSports.contains(pratiquer.getNomSport())).count() >= 1;
    }
}
