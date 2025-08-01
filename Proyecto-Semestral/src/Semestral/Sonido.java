package Semestral;

import javax.sound.sampled.*;
import java.net.URL;

public class Sonido {
    private Clip musicaFondo;
    private boolean sonidoActivado = true;
    private float volumen = 0.7f;

    public void reproducirDisparo() {
        reproducirEfecto("disparo.wav");
    }

    public void reproducirGol() {
        reproducirEfecto("gol.wav");
    }

    public void reproducirAtajada() {
        reproducirEfecto("atajada.wav");
    }

    public void reproducirClick() {
        reproducirEfecto("click.wav");
    }

    private void reproducirEfecto(String nombreArchivo) {
        if (!sonidoActivado) return;

        try {
            URL url = getClass().getClassLoader().getResource("sounds/" + nombreArchivo);
            if (url == null) {
                System.err.println("Archivo de sonido no encontrado: " + nombreArchivo);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volumen) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir " + nombreArchivo + ": " + e.getMessage());
        }
    }

    public void iniciarMusicaFondo() {
        try {
            URL url = getClass().getClassLoader().getResource("sounds/background.wav");
            if (url == null) {
                System.err.println("Archivo de música no encontrado");
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);

            FloatControl gainControl = (FloatControl) musicaFondo.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volumen * 0.7) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error al iniciar música: " + e.getMessage());
        }
    }

    public void detenerMusica() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    }

    public void setVolumen(float volumen) {
        this.volumen = Math.max(0, Math.min(1, volumen));
    }

    public void toggleSonido(boolean activado) {
        this.sonidoActivado = activado;
        if (!activado) {
            detenerMusica();
        } else if (musicaFondo != null && !musicaFondo.isRunning()) {
            iniciarMusicaFondo();
        }
    }
}