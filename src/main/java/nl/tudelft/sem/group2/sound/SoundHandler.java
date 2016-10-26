package nl.tudelft.sem.group2.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Handles playing music and sounds using a MediaView.
 */
public final class SoundHandler {

    /**
     * Creates a new SoundHandler. Needs a mediaView.
     */
    private SoundHandler() {
        throw new AssertionError("Instantiating utility class...");
    }
    /**
     * Plays a sound file.
     *
     * @param string - the path to the sound file
     * @param volume - the volume in decibels
     */
    public static void playSound(final String string, final double volume) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    MediaView mediaView = new MediaView();
                    Media hit = new Media(getClass().getResource(string).toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                    mediaPlayer.setVolume(volume);
                    mediaPlayer.play();
                    mediaView.setMediaPlayer(mediaPlayer);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
