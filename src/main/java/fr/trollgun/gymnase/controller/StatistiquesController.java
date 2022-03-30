package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.modele.Association;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {

    @FXML
    ComboBox<Association> comboBoxAsso;
    @FXML
    Label labelReference;
    @FXML
    Label labelVille;
    @FXML
    Label labelAdresse;
    @FXML
    Label labelResponsable;
    @FXML
    Label labelNbReservation;
    @FXML
    Label labelSportPratiquer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxAsso.setItems(HelloApplication.getDatabase().getObservableListAssociation());
    }

    @FXML
    private void onActionComboBoxAsso(ActionEvent actionEvent) {
        Association association = comboBoxAsso.getSelectionModel().getSelectedItem();
        this.labelAdresse.setText(association.getAdresse());
        this.labelReference.setText(association.getRefAsso());
        this.labelResponsable.setText(association.getNomResponsable());
        this.labelVille.setText(association.getVille());
        this.labelSportPratiquer.setText(HelloApplication.getDatabase().getSportPratiquerForAssociation(association.getRefAsso()));
        this.labelNbReservation.setText(HelloApplication.getDatabase().getTotalReservationAssociation(association.getRefAsso())+"");
    }
}
