package main.java.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import main.java.controller.Player;

/**
 * Die Klasse GameSetup ist für die Initialisierung des Spiels zuständig.
 * Sie verwaltet die Erstellung der Spieler, die Generierung des Charakter-Decks
 * sowie die Verteilung der Charakter- und Fragekarten.
 */
public class GameSetup {

    /** Liste der am Spiel teilnehmenden Spieler. */
    private final List<Player> players;

    /** Eine Liste mit lustigen Namen für simulierte Spieler (Bots). */
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

    /** Zufallsgenerator für die Namenswahl und Kartenverteilung. */
    private final Random random = new Random();

    /**
     * Erstellt ein neues Spiel-Setup und initialisiert Spieler sowie Karten.
     * * @param humanPlayerCount Die Anzahl der menschlichen Spieler (3 bis 8).
     * @throws IllegalArgumentException Wenn die Spieleranzahl außerhalb des erlaubten Bereichs liegt.
     */
    public GameSetup(int humanPlayerCount) {
        if (humanPlayerCount < 3 || humanPlayerCount > 8) {
            throw new IllegalArgumentException(
                    "Spieleranzahl muss zwischen 3 und 8 liegen, war: " + humanPlayerCount);
        }

        players = new ArrayList<>();

        // Sonderregel: Bei 4 Spielern wird ein simulierter Spieler hinzugefügt
        boolean needsSimulatedPlayer = (humanPlayerCount == 4);
        int totalPlayers = needsSimulatedPlayer ? 5 : humanPlayerCount;

        int uidCounter = 1;

        // --- 1. Spieler anlegen ---
        for (int i = 1; i <= totalPlayers; i++) {
            String name;
            if (needsSimulatedPlayer && i == totalPlayers) {
                name = getRandomFunnyName();
            } else {
                name = "Spieler " + i;
            }
            players.add(new Player(uidCounter++, i, name));
        }

        // --- 2. Charakterkarten verteilen ---
        List<Character> characterDeck = createCharacterDeck(players.size());
        Collections.shuffle(characterDeck);

        int cIndex = 0;
        for (Player player : players) {
            player.addCharacterCard(characterDeck.get(cIndex++));
            player.addCharacterCard(characterDeck.get(cIndex++));
        }

        // --- 3. Fragekarten verteilen ---
        QuestionCardDeck questionDeck = new QuestionCardDeck();
        List<QuestionCard> questionCards = new ArrayList<>(questionDeck.getCards());
        Collections.shuffle(questionCards);

        int qIndex = 0;
        for (Player player : players) {
            player.addQuestionCard(questionCards.get(qIndex++));
            player.addQuestionCard(questionCards.get(qIndex++));
        }
    }

    /**
     * Fügt dem Deck eine bestimmte Anzahl an Charakteren eines Typs hinzu.
     * * @param deck Das Ziel-Deck.
     * @param type Der Charaktertyp, der hinzugefügt werden soll.
     * @param amount Die Anzahl der Karten.
     */
    private void addCharacters(List<Character> deck, CharacterType type, int amount) {
        for (int i = 0; i < amount; i++) {
            deck.add(createCharacter(type));
        }
    }

    /**
     * Factory-Methode zur Erstellung einzelner Charakter-Objekte basierend auf ihrem Typ.
     * * @param type Der zu erstellende Charaktertyp.
     * @return Ein neues Character-Objekt mit vordefinierten Eigenschaften.
     * @throws IllegalStateException Wenn ein unbekannter CharacterType übergeben wird.
     */
    private Character createCharacter(CharacterType type) {
        switch (type) {
            case WOLF:
                return new Character(CharacterType.WOLF, CharacterType.SHEEP, 5,
                        List.of(CharacterType.SHEEP, CharacterType.HUNTER));
            case HUNTER:
                return new Character(CharacterType.HUNTER, CharacterType.SHEPHERD, 3,
                        List.of(CharacterType.WOLF));
            case HUNTINGDOG:
                return new Character(CharacterType.HUNTINGDOG, CharacterType.SHEEP, 4,
                        List.of(CharacterType.WOLF));
            case SHEEP:
                return new Character(CharacterType.SHEEP, CharacterType.SHEEP, 1,
                        List.of());
            default:
                throw new IllegalStateException("Unbekannter Typ: " + type);
        }
    }

    /**
     * Erstellt die Zusammensetzung des Charakter-Decks basierend auf der Spieleranzahl.
     * * @param playerCount Die Gesamtanzahl der Spieler.
     * @return Eine Liste von Charakterkarten für das Spiel.
     * @throws IllegalArgumentException Wenn die Spieleranzahl nicht unterstützt wird.
     */
    private List<Character> createCharacterDeck(int playerCount) {
        List<Character> deck = new ArrayList<>();
        int wolves, hunters, dogs, sheep;

        switch (playerCount) {
            case 4:
            case 5:
                wolves = 1; hunters = 1; dogs = 1; sheep = 4;
                break;
            case 6:
                wolves = 1; hunters = 1; dogs = 2; sheep = 5;
                break;
            case 7:
                wolves = 2; hunters = 1; dogs = 2; sheep = 6;
                break;
            case 8:
                wolves = 2; hunters = 2; dogs = 2; sheep = 7;
                break;
            default:
                throw new IllegalArgumentException("Ungültige Spieleranzahl: " + playerCount);
        }

        addCharacters(deck, CharacterType.WOLF, wolves);
        addCharacters(deck, CharacterType.HUNTER, hunters);
        addCharacters(deck, CharacterType.HUNTINGDOG, dogs);
        addCharacters(deck, CharacterType.SHEEP, sheep);

        return deck;
    }

    /**
     * Wählt einen zufälligen Namen aus der FUNNY_NAMES Liste aus.
     * * @return Ein zufälliger Name als String.
     */
    private String getRandomFunnyName() {
        return FUNNY_NAMES.get(random.nextInt(FUNNY_NAMES.size()));
    }

    /**
     * Gibt die Liste der initialisierten Spieler zurück.
     * * @return Eine unveränderliche Liste der Spieler.
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Überprüft, ob ein simulierter Spieler (Bot) im Spiel vorhanden ist.
     * * @return true, wenn ein simulierter Spieler existiert, sonst false.
     */
    public boolean hasSimulatedPlayer() {
        return players.size() == 5 && players.stream()
                .anyMatch(p -> p.getId() == 5);
    }
}