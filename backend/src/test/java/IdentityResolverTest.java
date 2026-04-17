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
        CharacterType distractor = (trueIdentity == CharacterType.Sheep)
            ? CharacterType.Wolf
            : CharacterType.Sheep;
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
        Character c1 = card(CharacterType.Sheep, 1);
        Character c2 = card(CharacterType.Sheep, 1);
        Assertions.assertEquals(
            CharacterType.Sheep,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Wolf-Karten → Identität ist Wolf")
    void bothCardsSameType_Wolf() {
        Character c1 = card(CharacterType.Wolf, 5);
        Character c2 = card(CharacterType.Wolf, 5);
        assertEquals(
            CharacterType.Wolf,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Jäger-Karten → Identität ist Jäger")
    void bothCardsSameType_Hunter() {
        Character c1 = card(CharacterType.Hunter, 3);
        Character c2 = card(CharacterType.Hunter, 3);
        assertEquals(
            CharacterType.Hunter,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Schäfer(4) vs Jäger(3) → Identität ist Schäfer")
    void differentTypes_HigherRankWins_Shepherd() {
        Character shepherd = card(CharacterType.Shepherd, 4);
        Character hunter = card(CharacterType.Hunter, 3);
        assertEquals(
            CharacterType.Shepherd,
            IdentityResolver.resolveIdentity(shepherd, hunter)
        );
    }

    @Test
    @DisplayName("Schaf(1) vs Wolf(5) → Identität ist Wolf")
    void differentTypes_HigherRankWins_Wolf() {
        Character sheep = card(CharacterType.Sheep, 1);
        Character wolf = card(CharacterType.Wolf, 5);
        assertEquals(
            CharacterType.Wolf,
            IdentityResolver.resolveIdentity(sheep, wolf)
        );
    }

    @Test
    @DisplayName(
        "Reihenfolge spielt keine Rolle – Wolf(5) zuerst übergeben"
    )
    void differentTypes_OrderDoesNotMatter() {
        Character wolf = card(CharacterType.Wolf, 5);
        Character sheep = card(CharacterType.Sheep, 1);
        assertEquals(
            CharacterType.Wolf,
            IdentityResolver.resolveIdentity(wolf, sheep)
        );
    }

    @Test
    @DisplayName("Jagdhund(2) vs Jäger(3) → Identität ist Jäger")
    void differentTypes_HuntingdogVsHunter() {
        Character dog = card(CharacterType.Huntingdog, 2);
        Character hunter = card(CharacterType.Hunter, 3);
        assertEquals(
            CharacterType.Hunter,
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
            CharacterType.Sheep,
            CharacterType.Wolf,
            1,
            Collections.singletonList(CharacterType.Hunter)
        );
        Character c2 = card(
            CharacterType.Sheep,
            CharacterType.Shepherd,
            1,
            Collections.singletonList(CharacterType.Huntingdog)
        );
        assertEquals(
            CharacterType.Sheep,
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
            CharacterType.Shepherd,
            CharacterType.Sheep,
            4,
            Collections.singletonList(CharacterType.Wolf)
        );
        Character dog = card(
            CharacterType.Huntingdog,
            CharacterType.Wolf,
            2,
            Collections.singletonList(CharacterType.Sheep)
        );
        assertEquals(
            CharacterType.Shepherd,
            IdentityResolver.resolveIdentity(shepherd, dog)
        );
    }

    @Test
    @DisplayName("Erste Karte null → IllegalArgumentException")
    void nullCard1_ThrowsException() {
        Character c2 = card(CharacterType.Sheep, 1);
        assertThrows(IllegalArgumentException.class, () ->
            IdentityResolver.resolveIdentity(null, c2)
        );
    }

    @Test
    @DisplayName("Zweite Karte null → IllegalArgumentException")
    void nullCard2_ThrowsException() {
        Character c1 = card(CharacterType.Wolf, 5);
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
