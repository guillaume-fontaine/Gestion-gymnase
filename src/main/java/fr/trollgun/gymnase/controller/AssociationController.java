package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;

import fr.trollgun.gymnase.modele.Association;
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

public class AssociationController implements Initializable {

    @FXML
    TableView<Association> tableViewAssociation;
    @FXML
    TableColumn<Association, String> tableColumnReference;
    @FXML
    TableColumn<Association, String> tableColumnResponsable;
    @FXML
    TableColumn<Association, String> tableColumnAdresse;
    @FXML
    TableColumn<Association, String> tableColumnVille;
    @FXML
    Button buttonAjouter;
    @FXML
    Button buttonStopModification;
    @FXML
    TextField txtFieldReference;
    @FXML
    Tooltip tooltipReferences;
    @FXML
    TextField txtFieldResponsable;
    @FXML
    TextField txtFieldAdresse;
    @FXML
    TextField txtFieldVille;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableViewAssociation.setItems(HelloApplication.getDatabase().getObservableListAssociation());
        tableColumnReference.setCellValueFactory(new PropertyValueFactory<>("refAsso"));
        tableColumnAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        tableColumnVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        tableColumnResponsable.setCellValueFactory(new PropertyValueFactory<>("nomResponsable"));

        Toolbox.applyMaxLenght(txtFieldReference, 20);
        Toolbox.applyMaxLenght(txtFieldVille, 30);
        Toolbox.applyMaxLenght(txtFieldAdresse, 50);
        Toolbox.applyMaxLenght(txtFieldResponsable, 30);
    }

    @FXML
    public void onClickAjouterAssociation(ActionEvent actionEvent) {
        if(txtFieldReference.getText().isEmpty()){
            Toolbox.sendMissingArguments(null, "La référence ne peut être vide");
            return;
        }
        if(txtFieldResponsable.getText().isEmpty()){
            Toolbox.sendMissingArguments(null, "La responsable ne peut être vide");
            return;
        }
        if(txtFieldAdresse.getText().isEmpty()){
            Toolbox.sendMissingArguments(null, "L'adresse ne peut être vide");
            return;
        }
        if(txtFieldVille.getText().isEmpty()){
            Toolbox.sendMissingArguments(null, "La ville ne peut être vide");
            return;
        }
        if(buttonStopModification.isDisabled()){
            Association association = new Association(txtFieldReference.getText(), txtFieldVille.getText(), txtFieldAdresse.getText(), txtFieldResponsable.getText());
            if(association.isExist(tableViewAssociation.getItems())){
                Toolbox.sendError(null, "Les références ne peuvent être doublées");
                return;
            }
            tableViewAssociation.getItems().add(association);
        }else {
            try{
                Association association = tableViewAssociation.getSelectionModel().getSelectedItem();
                association.setAdresse(txtFieldAdresse.getText());
                association.setVille(txtFieldVille.getText());
                association.setNomResponsable(txtFieldResponsable.getText());
                if(!Toolbox.sendModifyRequest("")){
                    int i = 1/0;
                }
            }catch (ArithmeticException e){
                Toolbox.sendNotify("Modification annulée");
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        tableViewAssociation.refresh();
    }

    @FXML
    public void onClickStopModification(ActionEvent actionEvent) {
        stopModification();
    }

    @FXML
    public void onMouseClickedAssociation(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() >= 2) startModification();
    }

    @FXML
    public void onKeyPressedAssociation(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) startModification();
        if(keyEvent.getCode() == KeyCode.ESCAPE)stopModification();
    }

    private void startModification(){
        buttonStopModification.setDisable(false);
        buttonAjouter.setText("Modifier");
        Association association = tableViewAssociation.getSelectionModel().getSelectedItem();
        txtFieldReference.setText(association.getRefAsso());
        txtFieldAdresse.setText(association.getAdresse());
        txtFieldResponsable.setText(association.getNomResponsable());
        txtFieldVille.setText(association.getVille());
        txtFieldReference.setDisable(true);
        tooltipReferences.setOpacity(1);
    }

    private void stopModification(){
        buttonStopModification.setDisable(true);
        buttonAjouter.setText("Ajouter");
        txtFieldReference.setText("");
        txtFieldVille.setText("");
        txtFieldResponsable.setText("");
        txtFieldAdresse.setText("");
        txtFieldReference.setDisable(false);
        tooltipReferences.setOpacity(0);
    }

}
