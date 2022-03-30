package fr.trollgun.gymnase.controller;

import fr.trollgun.gymnase.HelloApplication;
import fr.trollgun.gymnase.Toolbox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GymnaseController implements Initializable {

    private final ObjectProperty<Stage> stageSalle = new SimpleObjectProperty<>(null),
            stageSport = new SimpleObjectProperty<>(null),
            stageAssociation = new SimpleObjectProperty<>(null),
            stageAccueillir = new SimpleObjectProperty<>(null),
            stagePratiquer = new SimpleObjectProperty<>(null),
            stageReserver = new SimpleObjectProperty<>(null),
            stageStatisitiques = new SimpleObjectProperty<>(null);
    private Stage currentView;
    private Button currentButton;

    @FXML
    HBox hboxGymnase;
    @FXML
    AnchorPane anchorPaneLittleView;
    @FXML
    Button buttonHome, buttonSave, buttonSalle, buttonSport, buttonAssociation, buttonAccueillir, buttonPratiquer, buttonReserver;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loadStages();
        try {
            HelloApplication.getDatabase().getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        changeCurrentView(stageStatisitiques.getValue());
    }

    @FXML
    private void onActionChangeColor(ActionEvent actionEvent) {
        Color color = ((ColorPicker) actionEvent.getSource()).getValue();
        HelloApplication.setColor(color.toString().replace("0x", "#").substring(0, 7));
        root.setStyle(HelloApplication.getColor());
        stageSalle.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stageSport.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stageAssociation.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stageAccueillir.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stagePratiquer.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stageReserver.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
        stageStatisitiques.getValue().getScene().getRoot().setStyle(HelloApplication.getColor());
    }

    @FXML
    private void onActionHome(ActionEvent actionEvent) {
        clearCurrentView();
        buttonHome.getStyleClass().add("current-navbar");
        if (currentButton != null) {
            currentButton.getStyleClass().clear();
            currentButton.getStyleClass().add("button");
        }
        currentButton = buttonHome;
        anchorPaneLittleView.requestFocus();
    }

    @FXML
    private void onActionSave(ActionEvent actionEvent) {
        if (Toolbox.sendSaveRequest()) {
            try {
                HelloApplication.getDatabase().getConnection().commit();
                HelloApplication.dropFlagDatabaseEdit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStages() {
        loadStage(stageSalle, "Gestion des salles", "salle-view.fxml", buttonSalle);
        loadStage(stageSport, "Gestion des sports", "sport-view.fxml", buttonSport);
        loadStage(stageAssociation, "Gestion des associations", "association-view.fxml", buttonAssociation);
        loadStage(stageAccueillir, "Gestion d'accueil des sports", "accueillir-view.fxml", buttonAccueillir);
        loadStage(stagePratiquer, "Gestion de la pratique des sports", "pratiquer-view.fxml", buttonPratiquer);
        loadStage(stageReserver, "Gestion des révseration", "reserver-view.fxml", buttonReserver);
        loadStage(stageStatisitiques, "Statistiques", "statistiques-view.fxml", buttonHome);
        clearCurrentView();
    }

    private void loadStage(ObjectProperty<Stage> stage, String title, String ressource, Button button) {
        initView(stage, title, ressource);
        initClickView(stage.getValue(), button);
    }

    private void initView(ObjectProperty<Stage> stage, String title, String resource) {
        stage.setValue(
                Toolbox.loadStage(title,
                        resource,
                        600,
                        400,
                        HelloApplication.class));
        changeCurrentView(stage.getValue());
    }

    private void initClickView(Stage stage, Button button) {
        button.setOnAction(actionEvent -> {
            changeCurrentView(stage);
            button.getStyleClass().add("current-navbar");
            if (currentButton != null) {
                currentButton.getStyleClass().clear();
                currentButton.getStyleClass().add("button");
            }
            currentButton = button;
            anchorPaneLittleView.requestFocus();
        });
    }

    private void changeCurrentView(Stage stage) {
        if (currentView != null && currentView.equals(stage)) return;
        clearCurrentView();
        anchorPaneLittleView.getChildren().add(stage.getScene().lookup("#anchorPane"));
        currentView = stage;
    }

    private void clearCurrentView() {
        anchorPaneLittleView.getChildren().clear();
        currentView = null;
    }
}
