package test.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import main.java.model.player.Player;
import main.java.model.character.CharacterType;
import main.java.model.character.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Akzeptanztests für Player#getTrueIdentity()
 * (ehemals IdentityResolver).
 */
class IdentityResolverTest {

    // -----------------------------------------------------------------------
    // Hilfsmethoden
    // -----------------------------------------------------------------------

    private Character card(
            CharacterType trueIdentity,
            CharacterType appearance,
            int rankValue,
            List<CharacterType> targets
    ) {
        return new Character(trueIdentity, appearance, rankValue, targets);
    }

    private Character card(CharacterType trueIdentity, int rankValue) {
        CharacterType distractor = (trueIdentity == CharacterType.SHEEP)
                ? CharacterType.WOLF
                : CharacterType.SHEEP;
        return card(
                trueIdentity,
                distractor,
                rankValue,
                Collections.singletonList(distractor)
        );
    }

    /** Erzeugt einen minimalen Test-Player mit zwei Charakterkarten. */
    private Player playerWith(Character c1, Character c2) {
        Player player = new Player(0, 1, "TestPlayer");
        player.addCharacterCard(c1);
        player.addCharacterCard(c2);
        return player;
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Zwei Schaf-Karten → Identität ist Schaf")
    void bothCardsSameType_Sheep() {
        Player player = playerWith(
                card(CharacterType.SHEEP, 1),
                card(CharacterType.SHEEP, 1)
        );
        assertEquals(CharacterType.SHEEP, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Zwei Wolf-Karten → Identität ist Wolf")
    void bothCardsSameType_Wolf() {
        Player player = playerWith(
                card(CharacterType.WOLF, 5),
                card(CharacterType.WOLF, 5)
        );
        assertEquals(CharacterType.WOLF, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Zwei Jäger-Karten → Identität ist Jäger")
    void bothCardsSameType_Hunter() {
        Player player = playerWith(
                card(CharacterType.HUNTER, 3),
                card(CharacterType.HUNTER, 3)
        );
        assertEquals(CharacterType.HUNTER, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Schäfer(4) vs Jäger(3) → Identität ist Schäfer")
    void differentTypes_HigherRankWins_Shepherd() {
        Player player = playerWith(
                card(CharacterType.SHEPHERD, 4),
                card(CharacterType.HUNTER, 3)
        );
        assertEquals(CharacterType.SHEPHERD, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Schaf(1) vs Wolf(5) → Identität ist Wolf")
    void differentTypes_HigherRankWins_Wolf() {
        Player player = playerWith(
                card(CharacterType.SHEEP, 1),
                card(CharacterType.WOLF, 5)
        );
        assertEquals(CharacterType.WOLF, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Reihenfolge spielt keine Rolle – Wolf(5) zuerst übergeben")
    void differentTypes_OrderDoesNotMatter() {
        Player player = playerWith(
                card(CharacterType.WOLF, 5),
                card(CharacterType.SHEEP, 1)
        );
        assertEquals(CharacterType.WOLF, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Jagdhund(2) vs Jäger(3) → Identität ist Jäger")
    void differentTypes_HuntingdogVsHunter() {
        Player player = playerWith(
                card(CharacterType.HUNTINGDOG, 2),
                card(CharacterType.HUNTER, 3)
        );
        assertEquals(CharacterType.HUNTER, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Gleicher TrueIdentity, verschiedene appearance → Identität korrekt")
    void appearanceHasNoEffect_SameIdentity() {
        Player player = playerWith(
                card(CharacterType.SHEEP, CharacterType.WOLF, 1,
                        Collections.singletonList(CharacterType.HUNTER)),
                card(CharacterType.SHEEP, CharacterType.SHEPHERD, 1,
                        Collections.singletonList(CharacterType.HUNTINGDOG))
        );
        assertEquals(CharacterType.SHEEP, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Verschiedene TrueIdentity, verschiedene appearance → rankValue entscheidet")
    void appearanceHasNoEffect_DifferentIdentity() {
        Player player = playerWith(
                card(CharacterType.SHEPHERD, CharacterType.SHEEP, 4,
                        Collections.singletonList(CharacterType.WOLF)),
                card(CharacterType.HUNTINGDOG, CharacterType.WOLF, 2,
                        Collections.singletonList(CharacterType.SHEEP))
        );
        assertEquals(CharacterType.SHEPHERD, player.getTrueIdentity());
    }

    @Test
    @DisplayName("Weniger als 2 Karten → IllegalStateException")
    void lessThanTwoCards_ThrowsException() {
        Player player = new Player(0, 1, "TestPlayer");
        player.addCharacterCard(card(CharacterType.SHEEP, 1));
        assertThrows(IllegalStateException.class, player::getTrueIdentity);
    }

    @Test
    @DisplayName("Keine Karten → IllegalStateException")
    void noCards_ThrowsException() {
        Player player = new Player(0, 1, "TestPlayer");
        assertThrows(IllegalStateException.class, player::getTrueIdentity);
    }
}