package main.java.model.game;

/**
 * Verwaltet den Fortschritt einer großen Runde eines Wooolf!!-Spiels.
 * Eine große Runde beginnt mit der kleinen Runde 1.
 * Eine große Runde kann unendlich viele kleine Runde haben.
 * Eine große Runde endet, wenn alle Spieler eine Verdächtigung abgegeben haben und diese aufgelöst wurde.
 * Das Spiel beginnt mit Runde 1 und endet spätestens nach Runde 3.
 *
 * @author Niklas Schädlich
 * @author Philipp Ehrhardt
 * @version 2.0
 */


public class BigRound {

    /** Maximale Anzahl an großen Runden pro Spiel. */
    private static final int MAX_BIG_ROUNDS = 3;

    /** Die aktuell laufende große Runde, beginnt bei 1. */
    private int currentBigRound;


    /**
     * Erstellt eine neue Rundeninstanz und startet das Spiel bei der großen Runde 1.
     */

    public BigRound() {
        this.currentBigRound = 1;
    }

    /**
     * Registriert das Ende einer großen Runde.
     * Erhöht nach Ende der großen Runde die Anzahl.
     */
    public void registerEndBigRound() {
        currentBigRound++;
    }

    /**
     * Gibt die aktuell laufende große Runde zurück.
     *
     * @return aktuelle große Rundennummer
     */
    public int getCurrentBigRound() {
        return currentBigRound;
    }

    /**
     * Prüft, ob die maximale große Rundenzahl erreicht ist und das Spiel beendet werden muss.
     * Das Spiel endet, wenn die maximale Rundenanzahl überschritten wurde.
     *
     * @return {@code true} wenn maximale große Rundenzahl erreicht ist, sonst {@code false}
     */
    public boolean isBigRoundCountReached() {
        return currentBigRound >= MAX_BIG_ROUNDS;
    }






}
