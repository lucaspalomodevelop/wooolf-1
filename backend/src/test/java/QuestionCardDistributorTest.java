package test.java;

import main.java.model.player.Player;
import main.java.model.card.QuestionCardDistributor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionCardDistributorTest {

    private final QuestionCardDistributor distributor = new QuestionCardDistributor();

    @Test
    void distribute_jederSpielerErhaeltZweiFragekarten() {
        List<Player> players = buildPlayers(3);
        distributor.distribute(players);
        players.forEach(p ->
                assertEquals(2, p.getQuestionCards().size()));
    }

    @Test
    void distribute_funktionierMitMaximalerSpieleranzahl() {
        List<Player> players = buildPlayers(8);
        distributor.distribute(players);
        players.forEach(p ->
                assertEquals(2, p.getQuestionCards().size()));
    }

    @Test
    void distribute_alleFragekartenHabenGueltigTyp() {
        List<Player> players = buildPlayers(3);
        distributor.distribute(players);
        players.forEach(p ->
                p.getQuestionCards().forEach(card ->
                        assertNotNull(card.getType())));
    }

    @Test
    void distribute_nullListeWirftException() {
        assertThrows(IllegalArgumentException.class,
                () -> distributor.distribute(null));
    }

    @Test
    void distribute_leereListeWirftException() {
        assertThrows(IllegalArgumentException.class,
                () -> distributor.distribute(List.of()));
    }

    @Test
    void distribute_zweiAufrufeGebenverschiedeneReihenfolgen() {
        // Mit unterschiedlichem Mischen sollten nicht immer
        // identische Kartentypen an Position 0 landen
        boolean unterschiedGefunden = false;
        for (int i = 0; i < 20; i++) {
            List<Player> run1 = buildPlayers(1);
            List<Player> run2 = buildPlayers(1);
            distributor.distribute(run1);
            distributor.distribute(run2);
            if (!run1.get(0).getQuestionCards().get(0).getType()
                    .equals(run2.get(0).getQuestionCards().get(0).getType())) {
                unterschiedGefunden = true;
                break;
            }
        }
        assertTrue(unterschiedGefunden,
                "Mischen sollte gelegentlich unterschiedliche Reihenfolgen erzeugen");
    }

    // Hilfsmethode
    private List<Player> buildPlayers(int count) {
        List<Player> players = new java.util.ArrayList<>();
        for (int i = 1; i <= count; i++) {
            players.add(new Player(i, i, "Spieler " + i));
        }
        return players;
    }
}
