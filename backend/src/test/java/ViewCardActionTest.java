package test.java;

import main.java.controller.ViewCardAction;
import main.java.controller.ViewCardResult;
import main.java.model.player.Player;
import main.java.model.character.Character;
import main.java.model.character.CharacterType;
import main.java.model.token.HintToken;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class ViewCardActionTest {

    // -----------------------------------------------------------------------
    // Hilfsmethoden
    // -----------------------------------------------------------------------

    /** Erzeugt einen Spieler mit zwei Charakterkarten fester Identität. */
    private Player playerWith(CharacterType identity1, int rank1,
                              CharacterType identity2, int rank2,
                              int id) {
        Player p = new Player(id, id, "Spieler" + id);
        p.addCharacterCard(card(identity1, identity1, rank1));
        p.addCharacterCard(card(identity2, identity2, rank2));
        return p;
    }

    /**
     * Erzeugt eine Charakterkarte, bei der trueIdentity und appearance
     * unabhängig gesetzt werden können.
     */
    private Character card(CharacterType trueIdentity, CharacterType appearance, int rank) {
        return new Character(trueIdentity, appearance, rank, List.of());
    }

    /** Aktiver Spieler ist Schäfer (beide Karten = SHEPHERD, rank 4). */
    private Player shepherdPlayer(int id) {
        return playerWith(CharacterType.SHEPHERD, 4, CharacterType.SHEPHERD, 4, id);
    }

    /** Aktiver Spieler ist Wolf (beide Karten = WOLF, rank 5). */
    private Player wolfPlayer(int id) {
        return playerWith(CharacterType.WOLF, 5, CharacterType.WOLF, 5, id);
    }

    /** Aktiver Spieler ist Schaf (beide Karten = SHEEP, rank 1). */
    private Player sheepPlayer(int id) {
        return playerWith(CharacterType.SHEEP, 1, CharacterType.SHEEP, 1, id);
    }

    /**
     * Erzeugt einen Zielspieler, dessen erste Karte eine bestimmte
     * trueIdentity und ein bestimmtes appearance hat.
     */
    private Player targetWith(CharacterType trueIdentity, CharacterType appearance, int id) {
        Player p = new Player(id, id, "Ziel" + id);
        p.addCharacterCard(card(trueIdentity, appearance, 1));
        p.addCharacterCard(card(CharacterType.SHEEP, CharacterType.SHEEP, 1));
        return p;
    }


    @Nested
    @DisplayName("Erscheinungsbild und wahre Identität")
    class AppearanceAndTrueIdentity {

        @Test
        @DisplayName("Öffentliches Erscheinungsbild entspricht dem appearance-Feld der Karte")
        void publicAppearance_isAppearanceField() {
            // Wolf-Karte erscheint als SHEEP (klassische Wolf-Verkleidung)
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.WOLF, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.SHEEP, result.getPublicAppearance(),
                    "Alle anderen sehen das appearance-Feld, nicht die wahre Identität.");
        }

        @Test
        @DisplayName("Aktiver Spieler sieht die wahre Identität der Karte")
        void trueIdentity_isReturnedForActivePlayer() {
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.WOLF, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.WOLF, result.getTrueIdentity(),
                    "Der aktive Spieler sieht kurzzeitig die wahre Identität.");
        }

        @Test
        @DisplayName("Wenn appearance == trueIdentity, sind beide Felder gleich")
        void appearanceEqualsTrueIdentity_bothFieldsSame() {
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.SHEEP, result.getPublicAppearance());
            assertEquals(CharacterType.SHEEP, result.getTrueIdentity());
        }

        @Test
        @DisplayName("Zweiter Kartenindex (index=1) wird korrekt gelesen")
        void secondCardIndex_isReadCorrectly() {
            Player active = sheepPlayer(1);
            Player target = new Player(2, 2, "Ziel2");
            target.addCharacterCard(card(CharacterType.SHEEP, CharacterType.SHEEP, 1));
            target.addCharacterCard(card(CharacterType.WOLF, CharacterType.HUNTINGDOG, 5));

            ViewCardResult result = new ViewCardAction().execute(active, target, 1);

            assertEquals(CharacterType.HUNTINGDOG, result.getPublicAppearance());
            assertEquals(CharacterType.WOLF, result.getTrueIdentity());
        }
    }

    @Nested
    @DisplayName("Schäfer-Sonderregel")
    class ShepherdRule {

        @Test
        @DisplayName("Schäfer sieht SHEEP-Karte → öffentlich wird WOLF angezeigt")
        void shepherd_seesSheeepCard_publicIsWolf() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.WOLF, result.getPublicAppearance(),
                    "Schäfer-Sonderregel: öffentlich immer WOLF.");
        }

        @Test
        @DisplayName("Schäfer sieht HUNTINGDOG-Karte → öffentlich wird WOLF angezeigt")
        void shepherd_seesHuntingdogCard_publicIsWolf() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.HUNTINGDOG, CharacterType.HUNTINGDOG, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.WOLF, result.getPublicAppearance());
        }

        @Test
        @DisplayName("Schäfer sieht HUNTER-Karte → öffentlich wird WOLF angezeigt")
        void shepherd_seesHunterCard_publicIsWolf() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.HUNTER, CharacterType.HUNTER, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.WOLF, result.getPublicAppearance());
        }

        @Test
        @DisplayName("Schäfer-Sonderregel: trueIdentity bleibt korrekt (unberührt)")
        void shepherd_trueIdentityUnaffected() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.HUNTER, CharacterType.HUNTER, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertEquals(CharacterType.HUNTER, result.getTrueIdentity(),
                    "Schäfer-Sonderregel beeinflusst nur publicAppearance, nicht trueIdentity.");
        }

        @Test
        @DisplayName("Schäfer-Flag isShepherdRuleApplied ist true wenn Regel greift")
        void shepherd_ruleAppliedFlag_isTrue() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertTrue(result.isShepherdRuleApplied());
        }

        @Test
        @DisplayName("Nicht-Schäfer: Schäfer-Flag isShepherdRuleApplied ist false")
        void nonShepherd_ruleAppliedFlag_isFalse() {
            Player active = wolfPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertFalse(result.isShepherdRuleApplied());
        }
    }

    @Nested
    @DisplayName("Fragezeichen-Ausnahme der Schäfer-Sonderregel")
    class ShepherdRuleQuestionMarkException {

        @Test
        @DisplayName("Schäfer sieht Fragezeichen-Karte (appearance=null) → publicAppearance bleibt null")
        void shepherd_questionMarkCard_publicAppearanceIsNull() {
            Player active = shepherdPlayer(1);
            // Fragezeichen: appearance == null
            Player target = new Player(2, 2, "Ziel");
            target.addCharacterCard(
                    new Character(CharacterType.WOLF, null, 5, List.of()));
            target.addCharacterCard(card(CharacterType.SHEEP, CharacterType.SHEEP, 1));

            ViewCardResult result = new ViewCardAction().execute(active, target, 0);

            assertNull(result.getPublicAppearance(),
                    "Fragezeichen-Karten sind von der Schäfer-Sonderregel ausgenommen.");
            assertFalse(result.isShepherdRuleApplied());
        }

        @Test
        @DisplayName("Schäfer sieht Fragezeichen → keine Hinweismarke wird platziert")
        void shepherd_questionMarkCard_noTokenPlaced() {
            Player active = shepherdPlayer(1);
            Player target = new Player(2, 2, "Ziel");
            target.addCharacterCard(
                    new Character(CharacterType.WOLF, null, 5, List.of()));
            target.addCharacterCard(card(CharacterType.SHEEP, CharacterType.SHEEP, 1));

            int tokensBefore = active.getHintTokens().size();
            new ViewCardAction().execute(active, target, 0);

            assertEquals(tokensBefore, active.getHintTokens().size(),
                    "Bei Fragezeichen wird keine Hinweismarke entnommen.");
            assertTrue(target.getPlacedTokensFromOthers().isEmpty(),
                    "Vor dem Zielspieler liegt keine Marke.");
        }
    }

    @Nested
    @DisplayName("Hinweismarken entnehmen und platzieren")
    class HintTokenManagement {

        @Test
        @DisplayName("Passende Hinweismarke wird aus dem Stapel des aktiven Spielers entfernt")
        void hintToken_removedFromActivePlayerStack() {
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.WOLF, CharacterType.SHEEP, 2);

            int before = active.getHintTokens().size();
            new ViewCardAction().execute(active, target, 0);
            int after = active.getHintTokens().size();

            assertEquals(before - 1, after,
                    "Genau eine Hinweismarke muss entnommen worden sein.");
        }

        @Test
        @DisplayName("Entnommene Marke passt zum publicAppearance (SHEEP)")
        void hintToken_matchesPublicAppearance_sheep() {
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.WOLF, CharacterType.SHEEP, 2);

            new ViewCardAction().execute(active, target, 0);

            // Token muss SHEEP als sideA oder sideB haben
            Map<Integer, List<HintToken>> placed = target.getPlacedTokensFromOthers();
            assertFalse(placed.isEmpty());
            HintToken token = placed.get(active.getId()).get(0);
            boolean matchesSheep = token.getSideA() == CharacterType.SHEEP
                    || token.getSideB() == CharacterType.SHEEP;
            assertTrue(matchesSheep,
                    "Die platzierte Marke muss SHEEP als Seite enthalten.");
        }

        @Test
        @DisplayName("Entnommene Marke passt zum publicAppearance (WOLF via Schäfer-Regel)")
        void hintToken_matchesPublicAppearance_wolfViaShepherd() {
            Player active = shepherdPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            new ViewCardAction().execute(active, target, 0);

            Map<Integer, List<HintToken>> placed = target.getPlacedTokensFromOthers();
            HintToken token = placed.get(active.getId()).get(0);
            boolean matchesWolf = token.getSideA() == CharacterType.WOLF
                    || token.getSideB() == CharacterType.WOLF;
            assertTrue(matchesWolf,
                    "Bei Schäfer-Sonderregel wird eine WOLF-Marke platziert.");
        }

        @Test
        @DisplayName("Hinweismarke liegt sichtbar vor dem Zielspieler (korrekte Spieler-ID)")
        void hintToken_placedBeforeTargetPlayer_withCorrectSourceId() {
            Player active = sheepPlayer(1);
            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            new ViewCardAction().execute(active, target, 0);

            Map<Integer, List<HintToken>> placed = target.getPlacedTokensFromOthers();
            assertTrue(placed.containsKey(active.getId()),
                    "Die Marke muss der ID des aktiven Spielers zugeordnet sein.");
            assertEquals(1, placed.get(active.getId()).size());
        }

        @Test
        @DisplayName("Mehrere Aktionen: Mehrere Marken landen vor dem Zielspieler")
        void multipleActions_multipleTokensPlaced() {
            Player active = sheepPlayer(1);
            Player target = new Player(2, 2, "Ziel");
            target.addCharacterCard(card(CharacterType.SHEEP, CharacterType.SHEEP, 1));
            target.addCharacterCard(card(CharacterType.HUNTER, CharacterType.HUNTER, 3));

            ViewCardAction action = new ViewCardAction();
            action.execute(active, target, 0);
            action.execute(active, target, 1);

            Map<Integer, List<HintToken>> placed = target.getPlacedTokensFromOthers();
            assertEquals(2, placed.get(active.getId()).size(),
                    "Nach zwei Aktionen liegen zwei Marken vor dem Zielspieler.");
        }
    }


    @Nested
    @DisplayName("Fehlerfälle und Validierung")
    class ErrorCases {

        @Test
        @DisplayName("activePlayer null → IllegalArgumentException")
        void nullActivePlayer_throwsException() {
            Player target = sheepPlayer(2);
            assertThrows(IllegalArgumentException.class,
                    () -> new ViewCardAction().execute(null, target, 0));
        }

        @Test
        @DisplayName("targetPlayer null → IllegalArgumentException")
        void nullTargetPlayer_throwsException() {
            Player active = sheepPlayer(1);
            assertThrows(IllegalArgumentException.class,
                    () -> new ViewCardAction().execute(active, null, 0));
        }

        @Test
        @DisplayName("Spieler versucht eigene Karte anzusehen → IllegalArgumentException")
        void samePlayer_throwsException() {
            Player player = sheepPlayer(1);
            assertThrows(IllegalArgumentException.class,
                    () -> new ViewCardAction().execute(player, player, 0));
        }

        @Test
        @DisplayName("Ungültiger Kartenindex (negativ) → IllegalArgumentException")
        void negativeIndex_throwsException() {
            Player active = sheepPlayer(1);
            Player target = sheepPlayer(2);
            assertThrows(IllegalArgumentException.class,
                    () -> new ViewCardAction().execute(active, target, -1));
        }

        @Test
        @DisplayName("Ungültiger Kartenindex (zu groß) → IllegalArgumentException")
        void tooLargeIndex_throwsException() {
            Player active = sheepPlayer(1);
            Player target = sheepPlayer(2);
            assertThrows(IllegalArgumentException.class,
                    () -> new ViewCardAction().execute(active, target, 2));
        }
    }

    @Nested
    @DisplayName("Kein passender Token vorhanden")
    class EmptyTokenStack {

        @Test
        @DisplayName("Alle passenden Tokens verbraucht → IllegalStateException")
        void noMatchingToken_throwsIllegalStateException() {
            Player active = sheepPlayer(1);
            // Alle Marken manuell entnehmen, die SHEEP als Seite enthalten
            while (active.removeHintToken(CharacterType.SHEEP).isPresent()) {
                // leert den Stapel für SHEEP-Marken
            }

            Player target = targetWith(CharacterType.SHEEP, CharacterType.SHEEP, 2);

            assertThrows(IllegalStateException.class,
                    () -> new ViewCardAction().execute(active, target, 0));
        }
    }
}