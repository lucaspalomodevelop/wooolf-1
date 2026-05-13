package main.java.model.game;

/**
 * Verwaltet den Rundenfortschritt eines Wooolf!!-Spiels.
 * Eine Runde endet, wenn alle Spieler einen Zug gemacht haben.
 * Das Spiel beginnt mit Runde 1 und endet spätestens nach Runde 3.
 *
 * @author Niklas Schädlich
 * @version 1.0
 */
public class Round {

    /** Maximale Anzahl an Runden pro Spiel. */
    private static final int MAX_ROUNDS = 3;

    /** Die aktuell laufende Runde, beginnt bei 1. */
    private int currentRound;

    /** Anzahl der Spieler, die an der aktuellen Runde teilnehmen. */
    private int numberOfPlayers;

    /** Gesamtanzahl der bisher registrierten Züge über alle Runden. */
    private int turnsTaken;

    /**
     * Erstellt eine neue Rundeninstanz und startet das Spiel bei Runde 1.
     *
     * @param numberOfPlayers Anzahl der Spieler im Spiel
     */
    public Round(int numberOfPlayers) {
        this.currentRound = 1;
        this.numberOfPlayers = numberOfPlayers;
        this.turnsTaken = 0;
    }

    /**
     * Registriert den Zug eines Spielers.
     * Wenn alle Spieler einen Zug gemacht haben (Modulo-Prüfung),
     * wird die Runde automatisch erhöht.
     */
    public void registerTurn() {
        turnsTaken++;
        if (turnsTaken % numberOfPlayers == 0) {
            currentRound++;
        }
    }

    /**
     * Gibt die aktuell laufende Runde zurück.
     *
     * @return aktuelle Rundennummer
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Prüft, ob das Spiel beendet ist.
     * Das Spiel endet, wenn die maximale Rundenanzahl überschritten wurde.
     *
     * @return {@code true} wenn das Spiel vorbei ist, sonst {@code false}
     */
    public boolean isGameOver() {
        return currentRound > MAX_ROUNDS;
    }
}