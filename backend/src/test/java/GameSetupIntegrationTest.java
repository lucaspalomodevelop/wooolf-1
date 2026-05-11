package test.java;
import main.java.model.player.Player;
import main.java.model.game.GameSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GameSetupIntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    void setup_jederSpielerHatZweiCharakterkarten(int playerCount) {
        GameSetup setup = new GameSetup(playerCount);
        setup.getPlayers().forEach(p ->
                assertEquals(2, p.getCharacterCards().size()));
    }

    @ParameterizedTest
    @ValueSource(ints = { 4, 5, 6, 7, 8})
    void setup_jederSpielerHatZweiFragekarten(int playerCount) {
        GameSetup setup = new GameSetup(playerCount);
        setup.getPlayers().forEach(p ->
                assertEquals(2, p.getQuestionCards().size()));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    void setup_jederSpielerHatHinweismarkenStapel(int playerCount) {
        GameSetup setup = new GameSetup(playerCount);
        setup.getPlayers().forEach(p ->
                assertFalse(p.getHintTokens().isEmpty()));
    }

    @Test
    void setup_vierSpielerErgibtFuenfMitBot() {
        GameSetup setup = new GameSetup(4);
        assertEquals(5, setup.getPlayers().size());
        assertTrue(setup.hasSimulatedPlayer());
    }

    @Test
    void setup_fuenfSpielerHatKeinenBot() {
        GameSetup setup = new GameSetup(5);
        assertFalse(setup.hasSimulatedPlayer());
    }

    /*@Test
    void setup_spielerlisteIstUnveraenderlich() {
        GameSetup setup = new GameSetup(3);
        assertThrows(UnsupportedOperationException.class,
                () -> setup.getPlayers().add(null));
    }*/

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 9})
    void setup_ungueltigeSpieleranzahlWirftException(int playerCount) {
        assertThrows(IllegalArgumentException.class,
                () -> new GameSetup(playerCount));
    }

    @Test
    void setup_alleCharacterkartenHabenGueltigeIdentitaet() {
        GameSetup setup = new GameSetup(5);
        setup.getPlayers().forEach(p ->
                p.getCharacterCards().forEach(c ->
                        assertNotNull(c.getTrueIdentity())));
    }

    @Test
    void setup_keineSpielerTeiltDieselbeUID() {
        GameSetup setup = new GameSetup(5);
        long uniqueIds = setup.getPlayers().stream()
                .map(Player::getUid)
                .distinct()
                .count();
        assertEquals(setup.getPlayers().size(), uniqueIds);
    }
}