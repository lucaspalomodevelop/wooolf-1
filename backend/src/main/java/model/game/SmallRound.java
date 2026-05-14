package main.java.model.game;

/**
 * Verwaltet den Fortschritt einer kleinen Runde eines Wooolf!!-Spiels.
 * Eine kleine Runde endet, wenn alle Spieler einen Zug gemacht haben.
 * Eine große Runde beginnt mit der kleinen Runde 1.
 * Eine große Runde kann unendlich viele kleine Runde haben.
 *
 * @author Niklas Schädlich
 * @author Philipp Ehrhardt
 * @version 2.0
 */


public class SmallRound {

    /** Die aktuell laufende kleine Runde, beginnt bei 1. */
    private int currentSmallRound;

    /** Anzahl der Spieler, die an der aktuell teilnehmen. */
    private int numberOfPlayers;

    /** Gesamtanzahl der bisher registrierten Züge über alle kleinen Runden. */
    private int turnsTaken;

    /**
     * Erstellt eine neue Rundeninstanz und startet die große Runde bei der kleinen Runde 1.
     *
     * @param numberOfPlayers Anzahl der Spieler im Spiel
     */
    public SmallRound(int numberOfPlayers) {
        this.currentSmallRound = 1;
        this.numberOfPlayers = numberOfPlayers;
        this.turnsTaken = 0;
    }

    /**
     * Registriert den Zug eines Spielers.
     * Wenn alle Spieler einen Zug gemacht haben (Modulo-Prüfung),
     * wird die kleine Runde automatisch erhöht.
     */
    public void registerTurn() {
        turnsTaken++;
        if (turnsTaken % numberOfPlayers == 0) {
            currentSmallRound++;
        }
    }

    /**
     * Gibt die aktuell laufende kleine Runde zurück.
     *
     * @return aktuelle kleine Rundennummer
     */
    public int getCurrentSmallRound() {
        return currentSmallRound;
    }

}
