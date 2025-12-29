package konnekt.utils;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundPlayer {

    public static void playNotification() {
        try {
            InputStream is =
                    SoundPlayer.class.getResourceAsStream(
                            "/konnekt/resources/sounds/fahhhhhhhhhhhhhh.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception ignored) {}
    }
}
