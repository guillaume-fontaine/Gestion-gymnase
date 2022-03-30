package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import fr.trollgun.gymnase.modele.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueillirController implements Initializable {

    @FXML
    TableView<Accueillir> tableViewAccueillir;

    @FXML
    TableColumn<Accueillir, String> tableColumnAccueillirSalle;

    @FXML
    TableColumn<Accueillir, String> tableColumnAccueillirSport;

    @FXML
    ComboBox<Sport> comboBoxSport;

    @FXML
    ComboBox<Salle> comboBoxSalle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewAccueillir.setItems(HelloApplication.getDatabase().getObservableListAccueillir());
        tableColumnAccueillirSport.setCellValueFactory(new PropertyValueFactory<>("nomSport"));
        tableColumnAccueillirSalle.setCellValueFactory(new PropertyValueFactory<>("refSalle"));
        comboBoxSalle.setItems(HelloApplication.getDatabase().getObservableListSalle());
        comboBoxSport.setItems(HelloApplication.getDatabase().getObservableListSport());
    }

    public void onClickSuppirmerAccueillir(ActionEvent actionEvent) {
        Accueillir accueillir = tableViewAccueillir.getSelectionModel().getSelectedItem();
        if (accueillir == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une ligne");
            return;
        }
        if (Toolbox.sendDeleteRequest(accueillir.toString())) {
            tableViewAccueillir.getItems().remove(accueillir);
        }
    }

    public void onClickAjouterAccueillir(ActionEvent actionEvent) {
        if (comboBoxSalle.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une salle");
            return;
        }
        if (comboBoxSport.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner un sport");
            return;
        }
        Accueillir accueillir = new Accueillir(comboBoxSalle.getValue().toString(), comboBoxSport.getValue().toString());
        if (tableViewAccueillir.getItems().contains(accueillir)) {
            Toolbox.sendError(null, "Cette ensemble est déjà présente");
            return;
        }
        tableViewAccueillir.getItems().add(accueillir);
    }

}
