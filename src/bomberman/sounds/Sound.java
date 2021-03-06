package bomberman.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;


public class Sound {
    public static Clip getSound(URL url){
        try {
            AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(url);
            Clip clip= AudioSystem.getClip();
            clip.open(audioInputStream);
            return  clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
