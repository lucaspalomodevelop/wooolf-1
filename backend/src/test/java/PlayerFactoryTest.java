package test.java;

import main.java.model.player.Player;
import main.java.model.player.PlayerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerFactoryTest {

    private final PlayerFactory factory = new PlayerFactory(42L); // fixer Seed

    @ParameterizedTest
    @ValueSource(ints = {3, 5, 6, 7, 8})
    void create_gibtKorrekteSpieleranzahlZurueck(int count) {
        List<Player> players = factory.create(count);
        assertEquals(count, players.size());
    }

    @Test
    void create_vierSpielerErgibtFuenfMitBot() {
        List<Player> players = factory.create(4);
        assertEquals(5, players.size());
    }

    @Test
    void create_vierSpielerHatSimuliertenSpieler() {
        List<Player> players = factory.create(4);
        assertTrue(factory.hasSimulatedPlayer(players));
    }

    @Test
    void create_fuenfSpielerHatKeinenBot() {
        List<Player> players = factory.create(5);
        assertFalse(factory.hasSimulatedPlayer(players));
    }

    @Test
    void create_spielerNamenSindKorrektDurchnummeriert() {
        List<Player> players = factory.create(3);
        assertEquals("Spieler 1", players.get(0).getName());
        assertEquals("Spieler 2", players.get(1).getName());
        assertEquals("Spieler 3", players.get(2).getName());
    }

    @Test
    void create_idsWerdenFortlaufendVergeben() {
        List<Player> players = factory.create(3);
        assertEquals(1, players.get(0).getId());
        assertEquals(2, players.get(1).getId());
        assertEquals(3, players.get(2).getId());
    }

    @Test
    void create_listeIstUnveraenderlich() {
        List<Player> players = factory.create(3);
        assertThrows(UnsupportedOperationException.class,
                () -> players.add(null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 9, 10})
    void create_ungueltigeSpieleranzahlWirftException(int count) {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(count));
    }

    @Test
    void create_mitFixemSeedLiefertReproduzierbarenBotNamen() {
        PlayerFactory f1 = new PlayerFactory(99L);
        PlayerFactory f2 = new PlayerFactory(99L);
        List<Player> p1 = f1.create(4);
        List<Player> p2 = f2.create(4);
        assertEquals(p1.get(4).getName(), p2.get(4).getName());
    }
}