package main.java.model.token;


/**
 * Value Object für die Fehlermarken eines Spielers.
 *
 * Verantwortlichkeit (SRP): ausschließlich Fehlermarken verwalten
 * und vergleichen – keine Spiellogik, keine Punkte.
 *
 * Als Value Object ist ErrorTokens auf Gleichheit der Werte ausgelegt,
 * nicht auf Objektidentität.
 */
public final class ErrorTokens {

    private int red;
    private int black;

    /**
     * Erstellt einen leeren Fehlermarken-Zähler (0/0).
     */
    public ErrorTokens() {
        this.red   = 0;
        this.black = 0;
    }

    /**
     * Erstellt einen Fehlermarken-Zähler mit vorgegebenen Werten.
     * Nützlich beim Laden eines Spielstands.
     *
     * @param red   Anzahl roter Fehlermarken (>= 0)
     * @param black Anzahl schwarzer Fehlermarken (>= 0)
     * @throws IllegalArgumentException wenn ein Wert negativ ist
     */
    public ErrorTokens(int red, int black) {
        if (red < 0 || black < 0) {
            throw new IllegalArgumentException(
                    "Fehlermarken dürfen nicht negativ sein. red=" + red + " black=" + black);
        }
        this.red   = red;
        this.black = black;
    }

    /** Erhöht die roten Fehlermarken um 1. */
    public void addRed() {
        this.red++;
    }

    /** Erhöht die schwarzen Fehlermarken um 1. */
    public void addBlack() {
        this.black++;
    }

    /**
     * Addiert mehrere Fehlermarken beider Farben auf einmal.
     * Nützlich beim Laden eines Spielstands.
     *
     * @param amount zu addierende Anzahl (>= 0)
     * @throws IllegalArgumentException wenn amount negativ ist
     */
    public void addBoth(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "Fehlermarken dürfen nicht negativ sein, war: " + amount);
        }
        this.red   += amount;
        this.black += amount;
    }

    /** @return Anzahl roter Fehlermarken */
    public int getRed() {
        return red;
    }

    /** @return Anzahl schwarzer Fehlermarken */
    public int getBlack() {
        return black;
    }

    /**
     * Gibt den kleineren der beiden Werte zurück.
     * Wird bei der Siegerermittlung bei Punktegleichstand verwendet.
     *
     * @return Minimum aus red und black
     */
    public int getMinimum() {
        return Math.min(red, black);
    }

    /**
     * Gesamtanzahl aller Fehlermarken.
     *
     * @return red + black
     */
    public int getTotal() {
        return red + black;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorTokens other)) return false;
        return red == other.red && black == other.black;
    }

    @Override
    public int hashCode() {
        return 31 * red + black;
    }

    @Override
    public String toString() {
        return "ErrorTokens{red=" + red + ", black=" + black + "}";
    }
}