package main.java.model.game;

import main.java.model.player.Player;
import java.util.List;

/**
 * Koordiniert ein laufendes Wooolf!!-Spiel.
 * Hält Setup-Ergebnis und Rundenlogik zusammen,
 * ohne deren Verantwortlichkeiten zu vermischen.
 */
public class Game {

    private final List<Player> players;
    private final SmallRound smallRound;
    private final BigRound bigRound;

    /**
     * Startet ein neues Spiel mit der angegebenen Spieleranzahl.
     *
     * @param humanPlayerCount Anzahl menschlicher Spieler (3–8)
     */
    public Game(int humanPlayerCount) {
        GameSetup setup = new GameSetup(humanPlayerCount);
        this.players = setup.getPlayers();
        this.smallRound = new SmallRound(players.size());  // Spieleranzahl NACH Setup, damit der Bot mitgezählt wird
        this.bigRound = new BigRound();
    }                                            //

    /** Registriert den Zug des aktuellen Spielers. */
    public void registerTurn() {
        smallRound.registerTurn();
    }

    /** @return true wenn das Spiel beendet ist */
    public boolean isGameOver() {

        if (bigRound.isBigRoundCountReached() == true) {    //Prüft ob maximale Anzahl an große Runden erreicht ist
            return true;
        //} else if () {        //Hier muss noch geprüft werden ob ein Spieler zwei oder mehr Fehlermarken hat

        } else {
            return false;
        }
    }

    /** @return aktuelle kleine Rundennummer */
    public int getCurrentSmallRound() {
        return smallRound.getCurrentSmallRound();
    }

    /** @return aktuelle kleine Rundennummer */
    public int getCurrentBigRound() {
        return bigRound.getCurrentBigRound();
    }


    /** @return unveränderliche Spielerliste */
    public List<Player> getPlayers() {
        return players;
    }
}