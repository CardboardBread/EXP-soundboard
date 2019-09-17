package ca.exp.soundboard.model;

import ca.exp.soundboard.gui.SettingsController;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsListener implements NativeKeyListener {

    private SettingsController settings;
    private Logger logger;

    public SettingsListener() throws NativeHookException {
        if (!GlobalScreen.isNativeHookRegistered()) {
            throw new NativeHookException("Native hook has not been registered!");
        }

        logger = Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, "Settings Listener initialized");
    }

    public void listenOn(SettingsController settings) {
        logger.log(Level.INFO, "Listening for new hotkey");
        GlobalScreen.addNativeKeyListener(this);
        this.settings = settings;
    }

    public void stopListening() {
        logger.log(Level.INFO, "Stopping hotkey listening");
        GlobalScreen.removeNativeKeyListener(this);
        settings = null;
    }

    public boolean isListening() {
        return Objects.nonNull(settings);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        settings.setHotkeyText(KeyCombination.asReadable(nativeEvent));
        settings.setCombo(nativeEvent);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        // do nothing
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
        // do nothing
    }

}
