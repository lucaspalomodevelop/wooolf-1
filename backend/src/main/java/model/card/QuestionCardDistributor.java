package main.java.model.card;

import main.java.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Verteilt Fragekarten an alle Spieler.
 *
 * Verantwortlichkeit (SRP): ausschließlich Fragekarten mischen
 * und gleichmäßig an Spieler verteilen – keine Charakterkarten,
 * keine Spieler-Erstellung, keine Spiellogik.
 *
 * Jeder Spieler erhält genau zwei Fragekarten. Das Deck wird
 * vor der Verteilung gemischt. Bei mehr benötigten Karten als
 * im Deck vorhanden wird zyklisch gewählt (Modulo).
 */
public class QuestionCardDistributor {

    /**
     * Mischt das Fragekarten-Deck und verteilt je zwei Karten
     * an jeden Spieler in der übergebenen Liste.
     *
     * @param players Liste der Spieler, die Karten erhalten sollen
     * @throws IllegalArgumentException wenn players null oder leer ist
     */
    public void distribute(List<Player> players) {
        validate(players);

        List<QuestionCard> deck = buildShuffledDeck();
        dealToPlayers(players, deck);
    }

    // -----------------------------------------------------------------------
    // Private Hilfsmethoden
    // -----------------------------------------------------------------------

    /**
     * Erstellt ein frisch gemischtes Fragekarten-Deck.
     *
     * @return gemischte Kopie aller Fragekarten
     */
    private List<QuestionCard> buildShuffledDeck() {
        List<QuestionCard> deck = new ArrayList<>(new QuestionCardDeck().getCards());
        Collections.shuffle(deck);
        return deck;
    }

    /**
     * Verteilt je zwei Karten an jeden Spieler.
     * Greift zyklisch auf das Deck zu, falls es kleiner
     * als die Gesamtzahl benötigter Karten ist.
     *
     * @param players Empfänger der Karten
     * @param deck    gemischtes Quelldeck
     */
    private void dealToPlayers(List<Player> players, List<QuestionCard> deck) {
        int deckSize = deck.size();
        int cardIndex = 0;

        for (Player player : players) {
            QuestionType first  = deck.get(cardIndex++ % deckSize).getType();
            QuestionType second = deck.get(cardIndex++ % deckSize).getType();
            player.addQuestionCard(first);
            player.addQuestionCard(second);
        }
    }

    /**
     * Validiert die Eingabe.
     *
     * @param players die zu prüfende Spielerliste
     * @throws IllegalArgumentException wenn players null oder leer ist
     */
    private void validate(List<Player> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException(
                    "Spielerliste darf nicht null oder leer sein.");
        }
    }
}