package fr.trollgun.gymnase;

import afester.javafx.svg.SvgLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.SQLException;


public class Toolbox {

    public static void sendMissingArguments(String header, String context) {
        createAlertWithBip(Alert.AlertType.ERROR, "Argument non trouvé", header, context).showAndWait();
    }

    public static void sendError(String header, String context) {
        createAlertWithBip(Alert.AlertType.ERROR, "Erreur", header, context).showAndWait();
    }

    public static void sendAlert(String context) {
        createAlertWithBip(Alert.AlertType.INFORMATION, "Alert", "Alert", context).showAndWait();
    }

    public static void sendNotify(String context) {
        createAlert(Alert.AlertType.INFORMATION, "Notification", "Notification", context).showAndWait();
    }

    public static Alert createAlert(Alert.AlertType alertType, String title, String header, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        return alert;
    }

    public static Alert createAlertWithBip(Alert.AlertType alertType, String title, String header, String context) {
        Toolkit.getDefaultToolkit().beep();
        return createAlert(alertType, title, header, context);
    }

    public static boolean sendDeleteRequest(String context) {
        Alert alert = createAlert(
                Alert.AlertType.CONFIRMATION,
                "Demande de suppression",
                "Voulez vous supprimez cette données ?",
                context);
        alert.showAndWait();
        return alert.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }

    public static boolean sendModifyRequest(String context) {
        Alert alert = createAlert(
                Alert.AlertType.CONFIRMATION,
                "Demande de modification",
                "Voulez vous modifiez cette données ?",
                context);
        alert.showAndWait();
        return alert.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }

    public static boolean sendSaveRequest() {

        Alert alert = null;
        try {
            alert = createAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Demande de sauvegarde",
                    "Voulez vous sauvegardez les modification ?",
                    HelloApplication.getDatabase().getConnection().getCatalog());
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert alert != null;
        return alert.getResult().getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }

    private static Stage loadStage(String title, String resource, double width, double height, Modality modality, Class classLoader) {
        Stage deuxiemeStage = new Stage();
        try {
            deuxiemeStage.setTitle(title);
            Parent parent = FXMLLoader.load(classLoader.getResource(resource));
            deuxiemeStage.setScene(new Scene(parent, width, height));
            deuxiemeStage.initModality(modality);
            deuxiemeStage.initOwner(null);
            deuxiemeStage.setMinWidth(width);
            deuxiemeStage.setMinHeight(height);
        } catch (Exception e) {
            System.out.println("Erreur chargement seconde fenetre : " + e.getMessage());
            e.printStackTrace();
        }
        return deuxiemeStage;
    }

    public static Stage loadStage(String title, String resource, double width, double height, Class clazz) {
        return loadStage(title, resource, width, height, Modality.WINDOW_MODAL, clazz);
    }

    /**
     * Applique au {@code TextField} la possibilité
     * d'accepter que des nombres a virgule.
     * <p>
     * Si l'utilisateur utilise autre chose que des nombres
     * alors une *beep* rentetira
     *
     * @param textField Le {@code TextField a modifié}.
     */
    public static void applyFloatFormat(TextField textField) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                textField.setText(oldValue);
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    /**
     * Applique au {@code TextField} une limite de caractère.
     * <p>
     * Si l'utilisateur dépasse cette limite
     * alors une *beep* retentira
     *
     * @param textField Le {@code TextField} a modifié.
     * @param length    La longueur maximale.
     */
    public static void applyMaxLenght(TextField textField, int length) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String regex = "^.{0," + length + "}$";
            if (!newValue.matches(regex)) {
                textField.setText(oldValue);
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    public static void loadSVGFileInButton(Button button, String svgFile) {
        SvgLoader loader = new SvgLoader();
        Group svgImage = loader.loadSvg(HelloApplication.class.getResourceAsStream(svgFile));
        svgImage.getStyleClass().add("svg");
        svgImage.setScaleX(0.1);
        svgImage.setScaleY(0.1);
        Group graphic = new Group(svgImage);
        button.setGraphic(graphic);
    }

}
