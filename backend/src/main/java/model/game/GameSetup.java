package main.java.model.game;

import main.java.model.character.Character;
import main.java.model.player.Player;
import main.java.model.card.QuestionCardDistributor;
import main.java.model.character.CharacterDeckFactory;
import main.java.model.player.PlayerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Orchestriert die Initialisierung eines Wooolf!!-Spiels.
 *
 * Verantwortlichkeit (SRP): ausschließlich die drei Setup-Schritte
 * koordinieren – Spieler anlegen, Charakterkarten verteilen,
 * Fragekarten verteilen. Die eigentliche Logik liegt in den
 * jeweiligen Factories.
 *
 * Änderungsgründe für diese Klasse: nur wenn sich die Reihenfolge
 * oder Abhängigkeiten der Setup-Schritte ändern.
 */
public class GameSetup {

    private final List<Player> players;

    /**
     * Führt das vollständige Spiel-Setup durch.
     *
     * @param humanPlayerCount Anzahl menschlicher Spieler (3–8)
     * @throws IllegalArgumentException wenn humanPlayerCount außerhalb 3–8
     */
    public GameSetup(int humanPlayerCount) {

        if(humanPlayerCount < 4) {
            throw new IllegalArgumentException();
        }

        validate(humanPlayerCount);

        PlayerFactory playerFactory             = new PlayerFactory();
        CharacterDeckFactory deckFactory        = new CharacterDeckFactory();
        QuestionCardDistributor distributor     = new QuestionCardDistributor();

        // Schritt 1: Spieler anlegen
        this.players = new java.util.ArrayList<>(playerFactory.create(humanPlayerCount));

        // Schritt 2: Charakterkarten verteilen
        List<Character> deck = deckFactory.create(players.size());
        int cardIndex = 0;
        for (Player player : players) {
            player.addCharacterCard(deck.get(cardIndex++ % deck.size()));
            player.addCharacterCard(deck.get(cardIndex++ % deck.size()));
        }

        // Schritt 3: Fragekarten verteilen
        distributor.distribute(players);
    }

    /**
     * Gibt die unveränderliche Liste der initialisierten Spieler zurück.
     *
     * @return unveränderliche Spielerliste
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Prüft, ob ein simulierter Spieler (Bot) im Spiel vorhanden ist.
     * Delegiert an PlayerFactory.
     *
     * @return true wenn ein Bot vorhanden ist
     */
    public boolean hasSimulatedPlayer() {
        return new PlayerFactory().hasSimulatedPlayer(players);
    }

    // -----------------------------------------------------------------------
    // Private Hilfsmethoden
    // -----------------------------------------------------------------------

    private void validate(int humanPlayerCount) {
        if (humanPlayerCount < 3 || humanPlayerCount > 8) {
            throw new IllegalArgumentException(
                    "Spieleranzahl muss zwischen 3 und 8 liegen, war: " + humanPlayerCount);
        }
    }
}