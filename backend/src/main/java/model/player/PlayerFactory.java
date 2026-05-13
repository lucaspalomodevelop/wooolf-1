package main.java.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Factory für die Erstellung von Spielern.
 *
 * Verantwortlichkeit (SRP): ausschließlich Spieler-Objekte erzeugen
 * und benennen – keine Karten, keine Spiellogik.
 *
 * Sonderregel: Bei genau 4 menschlichen Spielern wird automatisch
 * ein simulierter Spieler (Bot) mit zufälligem Namen hinzugefügt,
 * sodass die Gesamtzahl auf 5 steigt.
 */
public class PlayerFactory {

    private static final List<String> FUNNY_NAMES = List.of(
            "Sir Bellt Viel",
            "Captain Ahnungslos",
            "Dr. Würfelglück",
            "Wooolf Master 30000",
            "xxWooolf",
            "Captain Jäger",
            "Captain Hund",
            "Der Verwirrte Walter",
            "Sneaky Schnitzel",
            "König Keks",
            "Graf ZahlZuViel",
            "Tapferer Hirte",
            "Bot McBotface",
            "General Planlos"
    );

    private final Random random;

    /**
     * Erstellt eine PlayerFactory mit echtem Zufallsgenerator.
     */
    public PlayerFactory() {
        this.random = new Random();
    }

    /**
     * Erstellt eine PlayerFactory mit einem vorgegebenen Seed –
     * nützlich für deterministische Tests.
     *
     * @param seed Seed für den Zufallsgenerator
     */
    public PlayerFactory(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Erzeugt die Spielerliste für eine Partie.
     *
     * Bei 4 menschlichen Spielern wird ein Bot ergänzt (Gesamtzahl: 5).
     * UIDs werden fortlaufend ab 1 vergeben.
     *
     * @param humanPlayerCount Anzahl menschlicher Spieler (3–8)
     * @return unveränderliche Liste der erzeugten Spieler
     * @throws IllegalArgumentException wenn humanPlayerCount außerhalb 3–8
     */
    public List<Player> create(int humanPlayerCount) {
        validate(humanPlayerCount);

        boolean needsBot = (humanPlayerCount == 4);
        int totalPlayers = needsBot ? 5 : humanPlayerCount;

        List<Player> players = new ArrayList<>(totalPlayers);

        for (int i = 1; i <= totalPlayers; i++) {
            boolean isBot = needsBot && (i == totalPlayers);
            String name   = isBot ? randomBotName() : "Spieler " + i;
            players.add(new Player(i, i, name));
        }

        return Collections.unmodifiableList(players);
    }

    /**
     * Prüft, ob in der erzeugten Liste ein simulierter Spieler (Bot) enthalten ist.
     *
     * @param players die zu prüfende Liste
     * @return true wenn ein Bot vorhanden ist
     */
    public boolean hasSimulatedPlayer(List<Player> players) {
        return players.size() == 5 &&
                players.stream().anyMatch(p -> FUNNY_NAMES.contains(p.getName()));
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

    private String randomBotName() {
        return FUNNY_NAMES.get(random.nextInt(FUNNY_NAMES.size()));
    }
}