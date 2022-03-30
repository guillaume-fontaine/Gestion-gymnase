package fr.trollgun.gymnase.controller;



import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import fr.trollgun.gymnase.modele.Sport;

import java.net.URL;
import java.util.ResourceBundle;

public class SportController implements Initializable{

    @FXML
    TableView<Sport> tableViewSport;
    @FXML
    TableColumn<Sport, String> tableColumnSport;
    @FXML
    TextField txtFieldTypeSports;
    @FXML
    Button buttonAjouter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewSport.setItems(HelloApplication.getDatabase().getObservableListSport());
        tableColumnSport.setCellValueFactory(new PropertyValueFactory<>("nomSport"));

        Toolbox.applyMaxLenght(txtFieldTypeSports, 25);
    }

    @FXML
    public void onClickAjouterSports(ActionEvent actionEvent) {
        if(txtFieldTypeSports.getText().isEmpty()){
            Toolbox.sendMissingArguments(null, "Le sport ne peut être vide");
            return;
        }
        Sport sport = new Sport(txtFieldTypeSports.getText());
        if(tableViewSport.getItems().contains(sport)){
            Toolbox.sendError(null, "Cette ensemble est déjà présente");
            return;
        }
        tableViewSport.getItems().add(sport);
    }

}
