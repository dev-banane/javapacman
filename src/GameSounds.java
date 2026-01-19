package src;

import java.net.URL;
import javax.sound.sampled.*;

public class GameSounds {
    Clip nomNom;
    Clip newGame;
    Clip death;
    boolean stopped;

    public GameSounds() {
        stopped = true;
        URL url;
        AudioInputStream audioIn;

        try {
            url = this.getClass().getClassLoader().getResource("sounds/nomnom.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            nomNom = AudioSystem.getClip();
            nomNom.open(audioIn);

            url = this.getClass().getClassLoader().getResource("sounds/newGame.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            newGame = AudioSystem.getClip();
            newGame.open(audioIn);

            url = this.getClass().getClassLoader().getResource("sounds/death.wav");
            audioIn = AudioSystem.getAudioInputStream(url);
            death = AudioSystem.getClip();
            death.open(audioIn);
        } catch (Exception e) {
        }
    }

    public void nomNom() {
        if (!stopped) return;
        stopped = false;
        nomNom.stop();
        nomNom.setFramePosition(0);
        nomNom.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void nomNomStop() {
        stopped = true;
        if (nomNom != null) {
            nomNom.stop();
            nomNom.setFramePosition(0);
        }
    }

    public void newGame() {
        if (newGame != null) {
            newGame.stop();
            newGame.setFramePosition(0);
            newGame.start();
        }
    }

    public void death() {
        if (death != null) {
            death.stop();
            death.setFramePosition(0);
            death.start();
        }
    }
}
