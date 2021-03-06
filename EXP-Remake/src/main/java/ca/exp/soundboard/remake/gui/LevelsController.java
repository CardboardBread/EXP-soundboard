package ca.exp.soundboard.remake.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import ca.exp.soundboard.remake.model.AudioMaster;
import javafx.stage.WindowEvent;

public class LevelsController extends GuiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    // --- GUI Objects --- //

    @FXML // fx:id="primarySlider"
    private Slider primarySlider; // Value injected by FXMLLoader

    @FXML // fx:id="secondarySlider"
    private Slider secondarySlider; // Value injected by FXMLLoader

    @FXML // fx:id="injectorSlider"
    private Slider injectorSlider; // Value injected by FXMLLoader

    // --- GUI Methods --- //

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert primarySlider != null : "fx:id=\"primarySlider\" was not injected: check your FXML file 'levels_jfx.fxml'.";
        assert secondarySlider != null : "fx:id=\"secondarySlider\" was not injected: check your FXML file 'levels_jfx.fxml'.";
        assert injectorSlider != null : "fx:id=\"injectorSlider\" was not injected: check your FXML file 'levels_jfx.fxml'.";
    }

    // --- General Methods --- //

    @Override
    void preload(SoundboardStage parent, Stage stage, Scene scene) {
        super.preload(parent, stage, scene);
        logger.log(Level.INFO, "Initializing level controller");

        // setup primary slider to change gain on primary speaker
        primarySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                parent.getModel().getAudio().setGain(0, newValue.floatValue());
            }
        });

        // setup secondary slider to change gain on secondary speaker
        secondarySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                parent.getModel().getAudio().setGain(1, newValue.floatValue());
            }
        });

        // setup tertiary slider to change gain on mic injector
        injectorSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // TODO update mic injector with new gain newValue.floatValue()
            }
        });

        // overwrites default behaviour from GuiController, calls a regular stop
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                stop();
            }
        });
    }

    public void reset() {
        logger.log(Level.INFO, "Resetting GUI elements");

        // set sliders to zero
        init(0f, 0f, 0f);
    }

    private void init(float primary, float secondary, float injector) {
        primarySlider.valueProperty().setValue(primary);
        secondarySlider.valueProperty().setValue(secondary);
        injectorSlider.valueProperty().setValue(injector);
    }

    @Override
    public void start() {
        logger.log(Level.INFO, "Starting levels controller");
        reset();

        AudioMaster audio = parent.getModel().getAudio();
        init(audio.getGain(0), audio.getGain(1), 0f);
        stage.show();
        active = true;
    }

    @Override
    public void stop() {
        if (active) {
            logger.info( "Closing levels controller");
            parent.getModel().getAudio().updateGain();
            parent.getModel().getAudio().updateGain();
            stage.close();
            active = false;
        } else {
            logger.info("Cannot close closed controller!");
        }
    }
}
