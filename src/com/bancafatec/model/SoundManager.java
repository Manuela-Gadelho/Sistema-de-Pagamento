package com.bancafatec.model; 

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream; // Importar InputStream

public class SoundManager {

    private static Clip successClip;
    private static Clip errorClip;
    private static Clip beepClip;

    private static final String SOUND_PATH_SUCCESS = "/sounds/success.wav"; 
    private static final String SOUND_PATH_ERROR = "/sounds/error.wav";
    private static final String SOUND_PATH_BEEP = "/sounds/beep.wav";

    public static void loadSounds() {
        try {
            InputStream successStream = SoundManager.class.getResourceAsStream(SOUND_PATH_SUCCESS);
            if (successStream != null) {
                successClip = AudioSystem.getClip();
                successClip.open(AudioSystem.getAudioInputStream(successStream));
            } else {
                System.err.println("Erro: Arquivo de som não encontrado no classpath: " + SOUND_PATH_SUCCESS);
            }

            InputStream errorStream = SoundManager.class.getResourceAsStream(SOUND_PATH_ERROR);
            if (errorStream != null) {
                errorClip = AudioSystem.getClip();
                errorClip.open(AudioSystem.getAudioInputStream(errorStream));
            } else {
                System.err.println("Erro: Arquivo de som não encontrado no classpath: " + SOUND_PATH_ERROR);
            }

            InputStream beepStream = SoundManager.class.getResourceAsStream(SOUND_PATH_BEEP);
            if (beepStream != null) {
                beepClip = AudioSystem.getClip();
                beepClip.open(AudioSystem.getAudioInputStream(beepStream));
            } else {
                System.err.println("Erro: Arquivo de som não encontrado no classpath: " + SOUND_PATH_BEEP);
            }

        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            System.err.println("Erro ao carregar sons: " + e.getMessage());
        }
    }

    public static void playSuccessSound() {
        if (successClip != null) {
            new Thread(() -> { 
                successClip.setFramePosition(0); 
                successClip.start();
            }).start();
        }
    }

    public static void playErrorSound() {
        if (errorClip != null) {
            new Thread(() -> {
                errorClip.setFramePosition(0);
                errorClip.start();
            }).start();
        }
    }

    public static void playBeepSound() {
        if (beepClip != null) {
            new Thread(() -> {
                beepClip.setFramePosition(0);
                beepClip.start();
            }).start();
        }
    }
}
