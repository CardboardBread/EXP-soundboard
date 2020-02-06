package ca.exp.soundboard.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundPlayer implements Runnable {

    private AudioMaster master;
    private Logger logger;

    private File sound;
    private SourceDataLine output;
    private AudioInputStream clip;
    private int index;

    public final AtomicBoolean running;
    public final AtomicBoolean paused;

    public SoundPlayer(AudioMaster master, File sound, SourceDataLine output, int index)
            throws UnsupportedAudioFileException, IOException {
        this.master = master;
        this.sound = sound;
        this.output = output;
        this.index = index;
        logger = Logger.getLogger(this.getClass().getName());
        running = new AtomicBoolean(true);
        paused = new AtomicBoolean(false);

        // grab formats from file
        AudioFormat clipFormat;
        try {
            clip = AudioSystem.getAudioInputStream(sound);
            clipFormat = clip.getFormat();
            if (!clipFormat.equals(AudioMaster.decodeFormat)) {
                clip = AudioSystem.getAudioInputStream(AudioMaster.decodeFormat, clip);
            }
        } catch (UnsupportedAudioFileException uafe) {
            logger.log(Level.INFO, "Target file: \"" + sound.getName() + "\" is unsupported natively, using converted format");
            throw uafe;
        }

        logger.log(Level.INFO, "Initialized sound player on: \"" + sound.getName() + "\"");
    }

    public void run() {

        // setup buffer for containing samples of audio to be played
        byte[] buffer = new byte[output.getBufferSize()];
        int bytesRead = 0;
        int bytesWritten = 0;

        while (running.get()) { // TODO: update writing sound

            if (paused.get()) {
                synchronized (paused) {
                    try {
                        logger.log(Level.WARNING, this + " pausing");
                        paused.wait();
                    } catch (InterruptedException ie) {
                        logger.log(Level.WARNING, "Failed to pause thread: ", ie);
                        running.compareAndSet(true, false);
                        paused.compareAndSet(true, false);
                    }
                }
            }

            // read a sample from the audio file
            try {
                bytesRead = clip.read(buffer, 0, buffer.length);
            } catch (IOException ioe) {
                logger.log(Level.SEVERE, "Failed to read from file: \"" + sound.getName() + "\"", ioe);
                running.compareAndSet(true, false);
            }

            // if anything was read
            if (bytesRead >= 0) {
                // write the sample to the output
                bytesWritten = output.write(buffer, 0, bytesRead);

                if (bytesWritten < bytesRead) {
                    logger.log(Level.WARNING, "Line closed before stream was finished!");
                    running.compareAndSet(true, false);
                    break;
                }
            } else {
                // once there is nothing left to write
                logger.log(Level.INFO, "Reached end of stream");
                running.compareAndSet(true, false);
            }
        }

        // close remaining stream, clean buffer and release access to resource
        try {
            clip.close();
            output.drain();
            output.close();
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Failed to close stream from file: \"" + sound.getName() + "\"", ioe);
        }

        // remove self from active list
        logger.log(Level.INFO, "Instance finished: \"" + sound.getName() + "\"");
    }

    @Override
    public String toString() {
        return Thread.currentThread().getName() + "/" + sound.getName();
    }

}
