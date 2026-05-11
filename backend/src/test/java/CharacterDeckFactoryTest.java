package test.java;
import main.java.model.character.CharacterDeckFactory;
import main.java.model.character.CharacterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import main.java.model.character.Character;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CharacterDeckFactoryTest {

    private final CharacterDeckFactory factory = new CharacterDeckFactory();

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    void create_deckGroessePasstZurSpieleranzahl(int playerCount) {
        List<Character> deck = factory.create(playerCount);
        int expected = switch (playerCount) {
            case 4, 5 -> 7;   // 1+1+1+4
            case 6    -> 9;   // 1+1+2+5
            case 7    -> 11;  // 2+1+2+6
            case 8    -> 13;  // 2+2+2+7
            default   -> fail("Unerwartete Spieleranzahl");
        };
        assertEquals(expected, deck.size());
    }

    @Test
    void create_fuenfSpieler_enthaeltGenauEinenWolf() {
        assertCharacterCount(5, CharacterType.WOLF, 1);
    }

    @Test
    void create_siebenSpieler_enthaeltZweiWoelfe() {
        assertCharacterCount(7, CharacterType.WOLF, 2);
    }

    @Test
    void create_sechsSpieler_enthaeltZweiJagdhunde() {
        assertCharacterCount(6, CharacterType.HUNTINGDOG, 2);
    }

    @Test
    void create_achtSpieler_enthaeltSiebenSchafe() {
        assertCharacterCount(8, CharacterType.SHEEP, 7);
    }

    @Test
    void create_alleKartenHabenGueltigeWahrheIdentitaet() {
        List<Character> deck = factory.create(5);
        deck.forEach(card ->
                assertNotNull(card.getTrueIdentity()));
    }

    @Test
    void create_alleKartenHabenGueltigesErscheinungsbild() {
        List<Character> deck = factory.create(5);
        deck.forEach(card ->
                assertNotNull(card.getAppearance()));
    }

    @Test
    void create_wolfErscheinungsbildIstSchaf() {
        List<Character> deck = factory.create(5);
        deck.stream()
                .filter(c -> c.getTrueIdentity() == CharacterType.WOLF)
                .forEach(wolf ->
                        assertEquals(CharacterType.SHEEP, wolf.getAppearance()));
    }

    @Test
    void create_listeIstUnveraenderlich() {
        List<Character> deck = factory.create(5);
        assertThrows(UnsupportedOperationException.class,
                () -> deck.add(null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 9})
    void create_ungueltigeSpieleranzahlWirftException(int playerCount) {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(playerCount));
    }

    // Hilfsmethode
    private void assertCharacterCount(int playerCount,
                                      CharacterType type,
                                      int expectedCount) {
        List<Character> deck = factory.create(playerCount);
        long actual = deck.stream()
                .filter(c -> c.getTrueIdentity() == type)
                .count();
        assertEquals(expectedCount, actual,
                "Erwartete " + expectedCount + "x " + type
                        + " bei " + playerCount + " Spielern");
    }
}
