package fr.trollgun.gymnase.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Boolean.TRUE;

public class TimePicker extends DatePicker {

    public static final String DEFAULT_FORMAT = "HH:mm";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
    private ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<>(LocalDateTime.now());
    private ObjectProperty<Boolean> minutesSelector = new SimpleObjectProperty<>(TRUE);
    private ObjectProperty<String> format = new SimpleObjectProperty<>() {
        public void set(String newValue) {
            super.set(newValue);
            formatter = DateTimeFormatter.ofPattern(newValue);
        }
    };

    public TimePicker(LocalDateTime localDateTime) {
        super(localDateTime.toLocalDate());
        getStyleClass().add("date-time-picker");
        setConverter(new InternalConverter());
        valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                dateTimeValue.set(null);
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
                } else {
                    LocalTime time = dateTimeValue.get().toLocalTime();
                    dateTimeValue.set(LocalDateTime.of(newValue, time));
                }
            }
        });
        dateTimeValue.addListener((obs, oldVal, newVal) -> setValue(newVal == null ? null : newVal.toLocalDate()));
        getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                getEditor().fireEvent(
                        new KeyEvent(getEditor(), getEditor(), KeyEvent.KEY_PRESSED, null, null,
                                KeyCode.ENTER, false, false, false, false));
            }
        });
        dateTimeValue.setValue(localDateTime);
        this.getEditor().setDisable(true);
    }

    public TimePicker() {
        this(LocalDateTime.of(2000,1,1,0,0,0));
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue.get();
    }

    public void setDateTimeValue(LocalDateTime dateTimeValue) {
        if (dateTimeValue != null && dateTimeValue.isAfter(LocalDateTime.of(1971, 6, 30, 12, 0))) {
            this.dateTimeValue.set(dateTimeValue);
        } else {
            this.dateTimeValue.set(LocalDateTime.of(2000,1,1,0,0,0));
        }
    }

    public ObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return dateTimeValue;
    }

    public String getFormat() {
        return format.get();
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public ObjectProperty<String> formatProperty() {
        return format;
    }

    public Boolean getMinutesSelector() {
        return minutesSelector.get();
    }

    public void setMinutesSelector(Boolean minutesSelector) {
        this.minutesSelector.set(minutesSelector);
    }

    public ObjectProperty<Boolean> minutesSelectorProperty() {
        return minutesSelector;
    }

    class InternalConverter extends StringConverter<LocalDate> {
        public String toString(LocalDate object) {
            LocalDateTime value = getDateTimeValue();
            return (value != null) ? value.format(formatter) : "";
        }

        public LocalDate fromString(String value) {
            if (value == null || value.isEmpty()) {
                dateTimeValue.set(null);
                return null;
            }
            dateTimeValue.set(LocalDateTime.parse(value, formatter));
            return dateTimeValue.get().toLocalDate();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TimePickerSkin(this);
    }






}
