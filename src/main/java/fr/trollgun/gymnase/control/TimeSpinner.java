package fr.trollgun.gymnase.control;

import fr.trollgun.gymnase.HelloApplication;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


import static javafx.scene.input.MouseButton.PRIMARY;

class TimeSpinner extends VBox {

    private final SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory;
    private final Image imageArrow = new Image(HelloApplication.class.getResource("icon/arrow.png").toString());
    ObjectProperty<Integer> valueProperty() {
        return integerSpinnerValueFactory.valueProperty();
    }

    TimeSpinner(int min, int max, int initial) {
        integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial);
        integerSpinnerValueFactory.setWrapAround(true);
        this.getStyleClass().add("spinner");
        Button downButton = new Button();
        downButton.setPrefSize(36,24);
        downButton.getStyleClass().add("down-button");
        Region downArrow = new Region();
        downArrow.getStyleClass().add("down-arrow");
        downButton.setGraphic(downArrow);
        Button upButton = new Button();
        upButton.setPrefSize(36,24);
        upButton.getStyleClass().add("up-button");
        Region upArrow = new Region();
        upArrow.getStyleClass().add("up-arrow");
        upButton.setGraphic(upArrow);
        Label valueLabel = new Label();
        valueLabel.setMinWidth(20);
        valueLabel.textProperty().bind(Bindings.format("%02d", integerSpinnerValueFactory.valueProperty()));
        valueLabel.getStyleClass().add("spinner-label");
        valueLabel.setFont(new Font(25));
        TimeSpinner.IncrementHandler incrementHandler = new TimeSpinner.IncrementHandler(integerSpinnerValueFactory);
        downButton.setOnAction(event -> integerSpinnerValueFactory.decrement(1));
        downButton.addEventFilter(MouseEvent.MOUSE_PRESSED, incrementHandler);
        downButton.addEventFilter(MouseEvent.MOUSE_RELEASED, incrementHandler);
        upButton.setOnAction(event -> integerSpinnerValueFactory.increment(1));
        upButton.addEventFilter(MouseEvent.MOUSE_PRESSED, incrementHandler);
        upButton.addEventFilter(MouseEvent.MOUSE_RELEASED, incrementHandler);

        this.getChildren().addAll(upButton, valueLabel, downButton);
    }

    private static final class IncrementHandler implements EventHandler<MouseEvent> {
        private SpinnerValueFactory spinnerValueFactory;
        private boolean increment;
        private long startTimestamp;
        private long nextStep;
        private static final long DELAY = 1000L * 1000L * 500; // 0.5 sec
        private static final long STEP = 1000L * 1000L * 100; // 0.5 sec
        private final AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - startTimestamp >= nextStep) {
                    nextStep += STEP;
                    if (increment) {
                        spinnerValueFactory.increment(1);
                    } else {
                        spinnerValueFactory.decrement(1);
                    }
                }
            }
        };

        IncrementHandler(SpinnerValueFactory.IntegerSpinnerValueFactory integerSpinnerValueFactory) {
            spinnerValueFactory = integerSpinnerValueFactory;
        }

        @Override
        public void handle(MouseEvent event) {
            if (event.getButton() == PRIMARY) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    Button button = (Button) event.getSource();
                    increment = button.getStyleClass().contains("down-button");
                    startTimestamp = System.nanoTime();
                    nextStep = DELAY;
                    timer.handle(startTimestamp + DELAY);
                    timer.start();
                    event.consume();
                    button.requestFocus();
                }
                if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    timer.stop();
                }
            }
        }
    }

}
