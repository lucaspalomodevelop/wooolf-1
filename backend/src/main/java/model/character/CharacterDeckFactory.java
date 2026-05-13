package main.java.model.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory für die Erstellung des Charakterkarten-Decks.
 *
 * Verantwortlichkeit (SRP): ausschließlich das Charakterdeck
 * zusammenstellen und mischen – keine Spieler, keine Fragekarten.
 *
 * Die Zusammensetzung des Decks richtet sich nach der Spieleranzahl
 * gemäß den offiziellen Wooolf!!-Spielregeln.
 */
public class CharacterDeckFactory {

    /**
     * Erstellt ein gemischtes Charakterdeck passend zur Spieleranzahl.
     *
     * @param playerCount Gesamtanzahl der Spieler (4–8)
     * @return gemischte, unveränderliche Liste von Charakterkarten
     * @throws IllegalArgumentException wenn playerCount nicht unterstützt wird
     */
    public List<Character> create(int playerCount) {
        DeckComposition comp = resolveComposition(playerCount);
        List<Character> deck = buildDeck(comp);
        Collections.shuffle(deck);
        return Collections.unmodifiableList(deck);
    }

    // -----------------------------------------------------------------------
    // Private Hilfsmethoden
    // -----------------------------------------------------------------------

    /**
     * Bestimmt die Deck-Zusammensetzung anhand der Spieleranzahl.
     *
     * @param playerCount Gesamtanzahl der Spieler
     * @return DeckComposition mit den Kartenanzahlen
     * @throws IllegalArgumentException wenn playerCount ungültig
     */
    private DeckComposition resolveComposition(int playerCount) {
        return switch (playerCount) {
            case 4, 5 -> new DeckComposition(1, 1, 1, 4);
            case 6    -> new DeckComposition(1, 1, 2, 5);
            case 7    -> new DeckComposition(2, 1, 2, 6);
            case 8    -> new DeckComposition(2, 2, 2, 7);
            default   -> throw new IllegalArgumentException(
                    "Ungültige Spieleranzahl: " + playerCount);
        };
    }

    /**
     * Baut die Kartenliste anhand der Komposition auf.
     *
     * @param comp die gewünschte Deck-Zusammensetzung
     * @return rohe (noch ungemischte) Kartenliste
     */
    private List<Character> buildDeck(DeckComposition comp) {
        List<Character> deck = new ArrayList<>();
        addCards(deck, CharacterType.WOLF,       comp.wolves());
        addCards(deck, CharacterType.HUNTER,     comp.hunters());
        addCards(deck, CharacterType.HUNTINGDOG, comp.dogs());
        addCards(deck, CharacterType.SHEEP,      comp.sheep());
        return deck;
    }

    /**
     * Fügt n Karten eines Typs in das Deck ein.
     *
     * @param deck   Zieldeck
     * @param type   Charaktertyp
     * @param amount Anzahl hinzuzufügender Karten
     */
    private void addCards(List<Character> deck, CharacterType type, int amount) {
        for (int i = 0; i < amount; i++) {
            deck.add(buildCharacter(type));
        }
    }

    /**
     * Erstellt ein einzelnes Character-Objekt mit den regelkonformen
     * Eigenschaften für den angegebenen Typ.
     *
     * @param type der gewünschte Charaktertyp
     * @return neues Character-Objekt
     * @throws IllegalStateException bei unbekanntem Typ
     */
    private Character buildCharacter(CharacterType type) {
        return switch (type) {
            case WOLF       -> new Character(
                    CharacterType.WOLF,
                    CharacterType.SHEEP,
                    5,
                    List.of(CharacterType.SHEEP, CharacterType.HUNTER));
            case HUNTER     -> new Character(
                    CharacterType.HUNTER,
                    CharacterType.SHEPHERD,
                    3,
                    List.of(CharacterType.WOLF));
            case HUNTINGDOG -> new Character(
                    CharacterType.HUNTINGDOG,
                    CharacterType.SHEEP,
                    4,
                    List.of(CharacterType.WOLF));
            case SHEEP      -> new Character(
                    CharacterType.SHEEP,
                    CharacterType.SHEEP,
                    1,
                    List.of());
            default         -> throw new IllegalStateException(
                    "Unbekannter Charaktertyp: " + type);
        };
    }

    // -----------------------------------------------------------------------
    // Internes Value Object
    // -----------------------------------------------------------------------

    /**
     * Kapselt die Anzahl der Karten pro Typ für eine bestimmte Spieleranzahl.
     * Immutable – wird nur innerhalb der Factory verwendet.
     */
    private record DeckComposition(int wolves, int hunters, int dogs, int sheep) {

        DeckComposition {
            if (wolves < 0 || hunters < 0 || dogs < 0 || sheep < 0) {
                throw new IllegalArgumentException(
                        "Kartenanzahlen dürfen nicht negativ sein.");
            }
        }

        int total() {
            return wolves + hunters + dogs + sheep;
        }
    }
}