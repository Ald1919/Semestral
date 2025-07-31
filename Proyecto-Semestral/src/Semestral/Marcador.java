package Semestral;

public class Marcador {
    private int goles;
    private int atajadas;

    public Marcador() {
        this.goles = 0;
        this.atajadas = 0;
    }

    public int getGoles() {
        return goles;
    }

    public void incrementarGoles() {
        this.goles++;
    }

    public int getAtajadas() {
        return atajadas;
    }

    public void incrementarAtajadas() {
        this.atajadas++;
    }
}
