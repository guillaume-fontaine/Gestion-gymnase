package fr.trollgun.gymnase.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimePickerSkin extends DatePickerSkin {

    private final ObjectProperty<LocalTime> timeObjectProperty;
    private final Node popupContent;
    private final TimePicker timePicker;
    private final Node timeSpinner;

    TimePickerSkin(TimePicker timePicker) {
        super(timePicker);
        this.timePicker = timePicker;
        timeObjectProperty = new SimpleObjectProperty<>(this, "displayedTime", LocalTime.from(timePicker.getDateTimeValue()));

        CustomBinding.bindBidirectional(timePicker.dateTimeValueProperty(), timeObjectProperty,
                LocalDateTime::toLocalTime,
                lt -> timePicker.getDateTimeValue().withHour(lt.getHour()).withMinute(lt.getMinute())
        );

        popupContent = super.getPopupContent();
        popupContent.getStyleClass().add("date-time-picker-popup");

        ((VBox) popupContent).getChildren().clear();

        timeSpinner = getTimeSpinner();
        ((VBox) popupContent).getChildren().add(timeSpinner);
        timeObjectProperty.addListener((observableValue, localTime, t1) -> {
            timePicker.getEditor().setText(t1.format(DateTimeFormatter.ofPattern(timePicker.DEFAULT_FORMAT)));
        });
    }

    private Node getTimeSpinner() {
        if (timeSpinner != null) {
            return timeSpinner;
        }
        final TimeSpinner spinnerHours =
                new TimeSpinner(0, 23, timePicker.getDateTimeValue().getHour());
        CustomBinding.bindBidirectional(timeObjectProperty, spinnerHours.valueProperty(),
                LocalTime::getHour,
                hour -> timeObjectProperty.get().withHour(hour)
        );
        final TimeSpinner spinnerMinutes =
                new TimeSpinner(0, 59, timePicker.getDateTimeValue().getMinute());
        CustomBinding.bindBidirectional(timeObjectProperty, spinnerMinutes.valueProperty(),
                LocalTime::getMinute,
                minute -> timeObjectProperty.get().withMinute(minute)
        );
        final Label labelTimeSeparator = new Label(":");
        HBox hBox = new HBox(5, /*new Label("Time:"),*/ spinnerHours, labelTimeSeparator, spinnerMinutes);
        hBox.setPadding(new Insets(8));
        hBox.setAlignment(Pos.CENTER_LEFT);
        timePicker.minutesSelectorProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                if (newValue) {
                    hBox.getChildren().add(labelTimeSeparator);
                    hBox.getChildren().add(spinnerMinutes);
                } else {
                    hBox.getChildren().remove(labelTimeSeparator);
                    hBox.getChildren().remove(spinnerMinutes);
                }
            }
        });
        registerChangeListener(timePicker.valueProperty(), e -> {
            LocalDateTime dateTimeValue = timePicker.getDateTimeValue();
            timeObjectProperty.set((dateTimeValue != null) ? LocalTime.from(dateTimeValue) : LocalTime.MIDNIGHT);
            timePicker.fireEvent(new ActionEvent());
        });
        ObservableList<String> styleClass = hBox.getStyleClass();
        styleClass.add("month-year-pane");
        styleClass.add("time-selector-pane");
        styleClass.add("time-selector-spinner-pane");
        return hBox;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Node getPopupContent() {
        return popupContent;
    }
}
