package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;
import fr.trollgun.gymnase.modele.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SalleController implements Initializable {

    @FXML
    Tooltip tooltipReferences;

    @FXML
    TableView<Salle> tableViewSalle;

    @FXML
    TableColumn<Salle, String> tableColumnReference;

    @FXML
    TableColumn<Salle, Float> tableColumnSurface;

    @FXML
    TableColumn<Salle, String> tableColumnTypeRevetement;

    @FXML
    TextField txtFieldReference;

    @FXML
    TextField txtFieldSurface;

    @FXML
    TextField txtFieldTypeRevetement;

    @FXML
    Button buttonAjouter;

    @FXML
    Button buttonStopModification;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewSalle.setItems(HelloApplication.getDatabase().getObservableListSalle());
        tableColumnReference.setCellValueFactory(new PropertyValueFactory<>("refSalle"));
        tableColumnSurface.setCellValueFactory(new PropertyValueFactory<>("surface"));
        tableColumnTypeRevetement.setCellValueFactory(new PropertyValueFactory<>("typeRevetement"));

        Toolbox.applyFloatFormat(txtFieldSurface);
        Toolbox.applyMaxLenght(txtFieldReference, 5);
        Toolbox.applyMaxLenght(txtFieldTypeRevetement, 30);
    }

    @FXML
    private void onKeyPressedSalle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) startModification();
        if (keyEvent.getCode() == KeyCode.ESCAPE) stopModification();
    }

    @FXML
    private void onMouseClickedSalle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) startModification();
    }

    @FXML
    private void onClickAjouterSalles(ActionEvent actionEvent) {
        if (buttonStopModification.isDisabled()) {
            if (txtFieldReference.getText().isEmpty()) {
                Toolbox.sendMissingArguments(null, "La référence ne peut être vide");
                return;
            }
            if (txtFieldSurface.getText().isEmpty()) {
                Toolbox.sendMissingArguments(null, "La surface ne peut être vide");
                return;
            }
            if (txtFieldTypeRevetement.getText().isEmpty()) {
                Toolbox.sendMissingArguments(null, "Le revêtement ne peut être vide");
                return;
            }
            Salle salle = new Salle(txtFieldReference.getText(), Float.parseFloat(txtFieldSurface.getText()), txtFieldTypeRevetement.getText());
            if (salle.isExist(tableViewSalle.getItems())) {
                Toolbox.sendError(null, "Les références ne peuvent être doublées");
                return;
            }
            tableViewSalle.getItems().add(salle);
        } else {
            //Modifier
            if (txtFieldSurface.getText().isEmpty()) {
                Toolbox.sendMissingArguments(null, "La surface ne peut être vide");
                return;
            }
            if (txtFieldTypeRevetement.getText().isEmpty()) {
                Toolbox.sendMissingArguments(null, "Le revêtement ne peut être vide");
                return;
            }
            try {
                Salle salle = tableViewSalle.getSelectionModel().getSelectedItem();
                salle.setSurface(Float.parseFloat(txtFieldSurface.getText()));
                salle.setTypeRevetement(txtFieldTypeRevetement.getText());
                if (!Toolbox.sendModifyRequest("")) {
                    int i = 1 / 0;
                }
            } catch (ArithmeticException e) {
                Toolbox.sendNotify("Modification annulée");
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                tableViewSalle.refresh();
            }
        }
    }

    @FXML
    private void onClickStopModification(ActionEvent actionEvent) {
        stopModification();
    }

    private void startModification() {
        buttonStopModification.setDisable(false);
        buttonAjouter.setText("Modifier");
        Salle salle = tableViewSalle.getSelectionModel().getSelectedItem();
        txtFieldReference.setText(salle.getRefSalle());
        txtFieldSurface.setText(String.valueOf(salle.getSurface()));
        txtFieldTypeRevetement.setText(salle.getTypeRevetement());
        txtFieldReference.setDisable(true);
        tooltipReferences.setOpacity(1);
    }

    private void stopModification() {
        buttonStopModification.setDisable(true);
        buttonAjouter.setText("Ajouter");
        txtFieldReference.setText("");
        txtFieldSurface.setText("");
        txtFieldTypeRevetement.setText("");
        txtFieldReference.setDisable(false);
        tooltipReferences.setOpacity(0);
    }
}
