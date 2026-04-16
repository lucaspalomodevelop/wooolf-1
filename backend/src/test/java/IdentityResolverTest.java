package main.java;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import main.java.CharacterType;
import main.java.IdentityResolver;
import main.java.character;
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
    private character card(
        CharacterType trueIdentity,
        CharacterType appearance,
        int rankValue,
        List<CharacterType> targets
    ) {
        return new character(trueIdentity, appearance, rankValue, targets);
    }

    private character card(CharacterType trueIdentity, int rankValue) {
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
        character c1 = card(CharacterType.Sheep, 1);
        character c2 = card(CharacterType.Sheep, 1);
        Assertions.assertEquals(
            CharacterType.Sheep,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Wolf-Karten → Identität ist Wolf")
    void bothCardsSameType_Wolf() {
        character c1 = card(CharacterType.Wolf, 5);
        character c2 = card(CharacterType.Wolf, 5);
        assertEquals(
            CharacterType.Wolf,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Zwei Jäger-Karten → Identität ist Jäger")
    void bothCardsSameType_Hunter() {
        character c1 = card(CharacterType.Hunter, 3);
        character c2 = card(CharacterType.Hunter, 3);
        assertEquals(
            CharacterType.Hunter,
            IdentityResolver.resolveIdentity(c1, c2)
        );
    }

    @Test
    @DisplayName("Schäfer(4) vs Jäger(3) → Identität ist Schäfer")
    void differentTypes_HigherRankWins_Shepherd() {
        character shepherd = card(CharacterType.Shepherd, 4);
        character hunter = card(CharacterType.Hunter, 3);
        assertEquals(
            CharacterType.Shepherd,
            IdentityResolver.resolveIdentity(shepherd, hunter)
        );
    }

    @Test
    @DisplayName("Schaf(1) vs Wolf(5) → Identität ist Wolf")
    void differentTypes_HigherRankWins_Wolf() {
        character sheep = card(CharacterType.Sheep, 1);
        character wolf = card(CharacterType.Wolf, 5);
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
        character wolf = card(CharacterType.Wolf, 5);
        character sheep = card(CharacterType.Sheep, 1);
        assertEquals(
            CharacterType.Wolf,
            IdentityResolver.resolveIdentity(wolf, sheep)
        );
    }

    @Test
    @DisplayName("Jagdhund(2) vs Jäger(3) → Identität ist Jäger")
    void differentTypes_HuntingdogVsHunter() {
        character dog = card(CharacterType.Huntingdog, 2);
        character hunter = card(CharacterType.Hunter, 3);
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
        character c1 = card(
            CharacterType.Sheep,
            CharacterType.Wolf,
            1,
            Collections.singletonList(CharacterType.Hunter)
        );
        character c2 = card(
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
        character shepherd = card(
            CharacterType.Shepherd,
            CharacterType.Sheep,
            4,
            Collections.singletonList(CharacterType.Wolf)
        );
        character dog = card(
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
        character c2 = card(CharacterType.Sheep, 1);
        assertThrows(IllegalArgumentException.class, () ->
            IdentityResolver.resolveIdentity(null, c2)
        );
    }

    @Test
    @DisplayName("Zweite Karte null → IllegalArgumentException")
    void nullCard2_ThrowsException() {
        character c1 = card(CharacterType.Wolf, 5);
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
