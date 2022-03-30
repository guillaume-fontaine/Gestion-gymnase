package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;
import fr.trollgun.gymnase.control.TimePicker;
import fr.trollgun.gymnase.modele.Accueillir;
import fr.trollgun.gymnase.modele.Association;
import fr.trollgun.gymnase.modele.Reserver;
import fr.trollgun.gymnase.modele.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReserverController implements Initializable {


    @FXML
    private TableView<Reserver> tableViewReserver;
    @FXML
    private TableColumn<Reserver, String> tableColumnAssociation;
    @FXML
    private TableColumn<Reserver, String> tableColumnSalle;
    @FXML
    private TableColumn<Reserver, LocalDate> tableColumnDate;
    @FXML
    private TableColumn<Reserver, LocalTime> tableColumnHeure;
    @FXML
    private ComboBox<Salle> comboBoxSalle;
    @FXML
    private ComboBox<Association> comboBoxAssociation;
    @FXML
    private DatePicker datePickerDate;
    @FXML
    private TimePicker timePickerHeure;
    @FXML
    private Button buttonReserverAjouter;
    @FXML
    private Button buttonStopModification;
    @FXML
    private Button buttonReserverSuppirmer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewReserver.setItems(HelloApplication.getDatabase().getObservableListReserver());
        tableColumnAssociation.setCellValueFactory(new PropertyValueFactory<>("refAsso"));
        tableColumnSalle.setCellValueFactory(new PropertyValueFactory<>("refSalle"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnHeure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        comboBoxAssociation.setItems(HelloApplication.getDatabase().getObservableListAssociation());
        comboBoxSalle.setItems(HelloApplication.getDatabase().getObservableListSalle());
    }

    @FXML
    private void onClickAjouterReserver(ActionEvent actionEvent) {
        if (comboBoxSalle.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une salle");
            return;
        }
        if (comboBoxAssociation.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner un sport");
            return;
        }
        if (datePickerDate.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une date");
            return;
        }
        if (timePickerHeure.getValue() == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner un heure");
            return;
        }
        if (buttonStopModification.isDisabled()) {
            Reserver reserver = new Reserver(comboBoxSalle.getValue().toString(), datePickerDate.getValue(), timePickerHeure.getDateTimeValue().toLocalTime(), comboBoxAssociation.getValue().toString());
            if (reserver.isExist(tableViewReserver.getItems())) {
                Toolbox.sendError(null, "Cette ensemble est déjà présente");
                return;
            }
            tableViewReserver.getItems().add(reserver);
        } else {
            try {
                Reserver reserver = tableViewReserver.getSelectionModel().getSelectedItem();
                reserver.setRefAsso(comboBoxAssociation.getSelectionModel().getSelectedItem().getRefAsso());
                if (!Toolbox.sendModifyRequest("")) {
                    int i = 1 / 0;
                }
            } catch (ArithmeticException e) {
                Toolbox.sendNotify("Modification annulée");
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @FXML
    private void onClickSuppirmerReserver(ActionEvent actionEvent) {
        Reserver reserver = tableViewReserver.getSelectionModel().getSelectedItem();
        if (reserver == null) {
            Toolbox.sendMissingArguments(null, "Veuillez sélectionner une ligne");
            return;
        }
        if (Toolbox.sendDeleteRequest(reserver.toString())) {
            tableViewReserver.getItems().remove(reserver);
        }
    }

    @FXML
    public void onClickStopModification(ActionEvent actionEvent) {
        stopModification();
    }

    @FXML
    public void onMouseClickedReserver(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) startModification();
    }

    @FXML
    public void onKeyReleasedReserver(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) startModification();
        if (keyEvent.getCode() == KeyCode.ESCAPE) stopModification();
    }

    private void startModification() {
        buttonStopModification.setDisable(false);
        buttonReserverAjouter.setText("Modifier");
        Reserver reserver = tableViewReserver.getSelectionModel().getSelectedItem();
        comboBoxSalle.getSelectionModel().select(HelloApplication.getDatabase().getSalleFromRefSalle(reserver.getRefSalle()));
        datePickerDate.setValue(reserver.getDate());
        timePickerHeure.setDateTimeValue(LocalDateTime.of(reserver.getDate(), reserver.getHeure()));
        comboBoxAssociation.getSelectionModel().select(HelloApplication.getDatabase().getAssociationFromRefAssociation(reserver.getRefAsso()));
        comboBoxSalle.setDisable(true);
        datePickerDate.setDisable(true);
        timePickerHeure.setDisable(true);
    }

    private void stopModification() {
        buttonStopModification.setDisable(true);
        buttonReserverAjouter.setText("Ajouter");
        comboBoxSalle.getSelectionModel().select(null);
        datePickerDate.setValue(null);
        timePickerHeure.setDateTimeValue(null);
        comboBoxAssociation.getSelectionModel().select(null);
        comboBoxSalle.setDisable(false);
        datePickerDate.setDisable(false);
        timePickerHeure.setDisable(false);
    }

    @FXML
    private void onActionComboBoxSalle(ActionEvent actionEvent) {
        comboBoxAssociation.setItems(HelloApplication.getDatabase()
                .getObservableListAssociationCanMakeSports(comboBoxSalle.getSelectionModel().getSelectedItem().getRefSalle()));
    }
}
