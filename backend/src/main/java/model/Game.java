package main.java.model;

import main.java.controller.Player;
import java.util.List;

/**
 * Koordiniert ein laufendes Wooolf!!-Spiel.
 * Hält Setup-Ergebnis und Rundenlogik zusammen,
 * ohne deren Verantwortlichkeiten zu vermischen.
 */
public class Game {

    private final List<Player> players;
    private final Round round;

    /**
     * Startet ein neues Spiel mit der angegebenen Spieleranzahl.
     *
     * @param humanPlayerCount Anzahl menschlicher Spieler (3–8)
     */
    public Game(int humanPlayerCount) {
        GameSetup setup = new GameSetup(humanPlayerCount);
        this.players = setup.getPlayers();
        this.round = new Round(players.size());  // Spieleranzahl NACH Setup,
    }                                            // damit der Bot mitgezählt wird

    /** Registriert den Zug des aktuellen Spielers. */
    public void registerTurn() {
        round.registerTurn();
    }

    /** @return true wenn das Spiel beendet ist */
    public boolean isGameOver() {
        return round.isGameOver();
    }

    /** @return aktuelle Rundennummer */
    public int getCurrentRound() {
        return round.getCurrentRound();
    }

    /** @return unveränderliche Spielerliste */
    public List<Player> getPlayers() {
        return players;
    }
}