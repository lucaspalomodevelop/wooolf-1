package test.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import main.java.model.CharacterType;
import main.java.model.IdentityResolver;
import main.java.model.Character;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Akzeptanztests für IdentityResolver.
 *
 */
class IdentityResolverTest {

    // -----------------------------------------------------------------------
    // Hilfsmethode: erzeugt eine Karte mit bewusst "ablenkenden" appearance-
    // und target-Werten.
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
        // appearance und targets werden absichtlich anders gesetzt
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


    @Test
    @DisplayName("Zwei Schaf-Karten → Identität ist Schaf")
    void bothCardsSameType_Sheep() {
        Character c1 = card(CharacterType.SHEEP, 1);
        Character c2 = card(CharacterType.SHEEP, 1);
        Assertions.assertEquals(
            CharacterType.SHEEP,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Wolf-Karten → Identität ist Wolf")
    void bothCardsSameType_Wolf() {
        Character c1 = card(CharacterType.WOLF, 5);
        Character c2 = card(CharacterType.WOLF, 5);
        assertEquals(
            CharacterType.WOLF,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Jäger-Karten → Identität ist Jäger")
    void bothCardsSameType_Hunter() {
        Character c1 = card(CharacterType.HUNTER, 3);
        Character c2 = card(CharacterType.HUNTER, 3);
        assertEquals(
            CharacterType.HUNTER,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Schäfer(4) vs Jäger(3) → Identität ist Schäfer")
    void differentTypes_HigherRankWins_Shepherd() {
        Character shepherd = card(CharacterType.SHEPHERD, 4);
        Character hunter = card(CharacterType.HUNTER, 3);
        assertEquals(
            CharacterType.SHEPHERD,
            IdentityResolver.resolveIdentity(shepherd, hunter)
        );
    }

    @Test
    @DisplayName("Schaf(1) vs Wolf(5) → Identität ist Wolf")
    void differentTypes_HigherRankWins_Wolf() {
        Character sheep = card(CharacterType.SHEEP, 1);
        Character wolf = card(CharacterType.WOLF, 5);
        assertEquals(
            CharacterType.WOLF,
            IdentityResolver.resolveIdentity(sheep, wolf)
        );
    }

    @Test
    @DisplayName(
        "Reihenfolge spielt keine Rolle – Wolf(5) zuerst übergeben"
    )
    void differentTypes_OrderDoesNotMatter() {
        Character wolf = card(CharacterType.WOLF, 5);
        Character sheep = card(CharacterType.SHEEP, 1);
        assertEquals(
            CharacterType.WOLF,
            IdentityResolver.resolveIdentity(wolf, sheep)
        );
    }

    @Test
    @DisplayName("Jagdhund(2) vs Jäger(3) → Identität ist Jäger")
    void differentTypes_HuntingdogVsHunter() {
        Character dog = card(CharacterType.HUNTINGDOG, 2);
        Character hunter = card(CharacterType.HUNTER, 3);
        assertEquals(
            CharacterType.HUNTER,
            IdentityResolver.resolveIdentity(dog, hunter)
        );
    }

    @Test
    @DisplayName(
        "Gleicher TrueIdentity, verschiedene appearance → Identität korrekt"
    )
    void appearanceHasNoEffect_SameIdentity() {
        // trueIdentity = Sheep, appearance = Wolf (Täuschung)
        Character c1 = card(
            CharacterType.SHEEP,
            CharacterType.WOLF,
            1,
            Collections.singletonList(CharacterType.HUNTER)
        );
        Character c2 = card(
            CharacterType.SHEEP,
            CharacterType.SHEPHERD,
            1,
            Collections.singletonList(CharacterType.HUNTINGDOG)
        );
        assertEquals(
            CharacterType.SHEEP,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName(
        "Verschiedene TrueIdentity, verschiedene appearance → rankValue entscheidet"
    )
    void appearanceHasNoEffect_DifferentIdentity() {
        // Schäfer-Karte mit appearance=Schaf, Jagdhund-Karte mit appearance=Wolf
        Character shepherd = card(
            CharacterType.SHEPHERD,
            CharacterType.SHEEP,
            4,
            Collections.singletonList(CharacterType.WOLF)
        );
        Character dog = card(
            CharacterType.HUNTINGDOG,
            CharacterType.WOLF,
            2,
            Collections.singletonList(CharacterType.SHEEP)
        );
        assertEquals(
            CharacterType.SHEPHERD,
            IdentityResolver.resolveIdentity(shepherd, dog)
        );
    }

    @Test
    @DisplayName("Erste Karte null → IllegalArgumentException")
    void nullCard1_ThrowsException() {
        Character c2 = card(CharacterType.SHEEP, 1);
        assertThrows(IllegalArgumentException.class, () ->
            IdentityResolver.resolveIdentity(null, c2)
        );
    }

    @Test
    @DisplayName("Zweite Karte null → IllegalArgumentException")
    void nullCard2_ThrowsException() {
        Character c1 = card(CharacterType.WOLF, 5);
        assertThrows(IllegalArgumentException.class, () ->
            IdentityResolver.resolveIdentity(c1, null)
        );
    }

    @Test
    @DisplayName("Beide Karten null → IllegalArgumentException")
    void bothNull_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            IdentityResolver.resolveIdentity(null, null)
        );
    }
}
