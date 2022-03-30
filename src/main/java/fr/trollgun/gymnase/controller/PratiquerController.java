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

public class PratiquerController implements Initializable {

    @FXML
    TableView<Pratiquer> tableViewPratiquer;
    @FXML
    TableColumn<Pratiquer, String> tableColumnPratiquerSport;
    @FXML
    TableColumn<Pratiquer, String> tableColumnPratiquerAssociation;
    @FXML
    ComboBox<Association> comboBoxAssociation;
    @FXML
    ComboBox<Sport> comboBoxSport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewPratiquer.setItems(HelloApplication.getDatabase().getObservableListPratiquer());
        tableColumnPratiquerAssociation.setCellValueFactory(new PropertyValueFactory<>("refAsso"));
        tableColumnPratiquerSport.setCellValueFactory(new PropertyValueFactory<>("nomSport"));
        comboBoxAssociation.setItems(HelloApplication.getDatabase().getObservableListAssociation());
        comboBoxSport.setItems(HelloApplication.getDatabase().getObservableListSport());
    }

    public void onClickAjouterPratiquer(ActionEvent actionEvent) {
        if(comboBoxAssociation.getValue() == null){
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une association");
            return;
        }
        if(comboBoxSport.getValue() == null){
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner un sport");
            return;
        }
        Pratiquer pratiquer = new Pratiquer(comboBoxAssociation.getValue().toString(), comboBoxSport.getValue().toString());
        if(tableViewPratiquer.getItems().contains(pratiquer)){
            Toolbox.sendError(null, "Cette ensemble est déjà présente");
            return;
        }
        tableViewPratiquer.getItems().add(pratiquer);
    }

    public void onClickSuppirmerPratiquer(ActionEvent actionEvent) {
        Pratiquer pratiquer = (Pratiquer) tableViewPratiquer.getSelectionModel().getSelectedItem();
        if(pratiquer == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une ligne");
            return;
        }
        if(Toolbox.sendDeleteRequest(pratiquer.toString())){
            tableViewPratiquer.getItems().remove(pratiquer);
        }
    }
}
