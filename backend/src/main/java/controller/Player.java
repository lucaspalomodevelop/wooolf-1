package main.java.controller;

import main.java.model.Character;
import main.java.model.CharacterType;
import main.java.model.HintToken;
import main.java.model.QuestionCard;
import main.java.model.QuestionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repräsentiert einen Spieler im Spiel.
 *
 * Ein Spieler besitzt genau zwei Charakterkarten, zwei Fragekarten,
 * einen eigenen Stapel an {@link HintToken Hinweismarken} sowie eine Ablage
 * für Marken, die andere Spieler sichtbar vor ihm platziert haben.
 */
public class Player {

    /**
     * Eindeutige interne ID des Spielers (z. B. für Persistenz oder Vergleich).
     */
    private final int uid;

    /**
     * Spielernummer innerhalb einer Partie (z. B. Spieler 1, 2, 3, ...).
     */
    private final int id;

    /**
     * Anzeigename des Spielers.
     */
    private String name;

    /**
     * Die Charakterkarten des Spielers (maximal 2).
     */
    private List<Character> characterCards = List.of();

    /** Die Fragekarten des Spielers (maximal 2). */
    private final List<QuestionCard> questionCards;

    /**
     * Der persönliche Vorrat an Hinweismarken.
     * Zu Spielbeginn enthält der Stapel je eine Marke aller {@link HintToken}-Typen.
     */
    private final List<HintToken> hintTokenStack;

    /**
     * Marken, die andere Spieler sichtbar vor diesem Spieler platziert haben.
     * Key = ID des legenden Spielers, Value = Liste der platzierten Marken.
     */
    private final Map<Integer, List<HintToken>> placedTokensFromOthers;

    /**
     * Erstellt einen neuen Spieler und füllt seinen Hinweismarken-Stapel automatisch.
     *
     * @param uid  eindeutige interne ID
     * @param id   Spielernummer in der Partie
     * @param name Anzeigename des Spielers
     */
    public Player(int uid, int id, String name) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.characterCards = new ArrayList<>();
        this.questionCards = new ArrayList<>();
        this.hintTokenStack = new ArrayList<>(List.of(HintToken.values()));
        this.placedTokensFromOthers = new HashMap<>();
    }

    /**
     * Vollständiger Konstruktor (z. B. für Tests oder Wiederherstellung aus Persistenz).
     */
    // public Player(int i, int uid, int id, List<Character> characterCards,
    //       List<QuestionCard> questionCards, String name) {
        //    this.uid = uid;
        //   this.id = id;
        //  this.name = name;
        //  this.characterCards = new ArrayList<>(characterCards);
        //  this.questionCards = new ArrayList<>();
        //  this.hintTokenStack = new ArrayList<>(List.of(HintToken.values()));
        //  this.placedTokensFromOthers = new HashMap<>();
        // }

    /**
     * Fügt eine Charakterkarte hinzu (max. 2).
     *
     * @param character die zuzuweisende Charakterkarte
     * @throws IllegalStateException wenn bereits 2 Charakterkarten vorhanden sind
     */
    public void addCharacterCard(Character character) {
        if (characterCards.size() >= 2) {
            throw new IllegalStateException(
                    "Spieler " + id + " hat bereits 2 Charakterkarten.");
        }
        characterCards.add(character);
    }

    /**
     * Fügt eine Fragekarte hinzu (max. 2).
     *
     * @param card die zuzuweisende Fragekarte
     * @throws IllegalStateException wenn bereits 2 Fragekarten vorhanden sind
     */
    public void addQuestionCard(QuestionType type) {
        if (questionCards.size() >= 2) {
            throw new IllegalStateException(
                    "Spieler " + id + " hat bereits 2 Fragekarten.");
        }
        questionCards.add(new QuestionCard(type));
    }

    /**

     * Entfernt eine verbrauchte Fragekarte.
     *
     * @param card die zu entfernende Fragekarte
     * @throws IllegalArgumentException wenn die Karte nicht vorhanden ist
     */
    public void removeQuestionCard(QuestionCard card) {
        if (!questionCards.remove(card)) {
            throw new IllegalArgumentException(
                    "Spieler " + id + " besitzt diese Fragekarte nicht.");
        }
    }

    /**
     * Gibt die Spielernummer zurück.

     * Entnimmt die erste Hinweismarke aus dem eigenen Stapel, die zu
     * {@code appearance} passt (d. h. deren {@code sideA} oder {@code sideB}
     * dem übergebenen {@link CharacterType} entspricht).

     *
     * @param appearance das gesuchte Erscheinungsbild
     * @return ein {@link Optional} mit der passenden Marke, oder {@link Optional#empty()}
     *         wenn keine passende Marke vorhanden ist
     */
    public Optional<HintToken> removeHintToken(CharacterType appearance) {
        for (int i = 0; i < hintTokenStack.size(); i++) {
            HintToken token = hintTokenStack.get(i);
            if (token.getSideA() == appearance || token.getSideB() == appearance) {
                hintTokenStack.remove(i);
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }

    /**
     * Nimmt eine Hinweismarke entgegen, die ein anderer Spieler sichtbar
     * vor diesem Spieler platziert.
     *
     * @param token          die platzierte Hinweismarke
     * @param fromPlayerId   ID des Spielers, der die Marke legt
     */
    public void receiveHintToken(HintToken token, int fromPlayerId) {
        placedTokensFromOthers
                .computeIfAbsent(fromPlayerId, k -> new ArrayList<>())
                .add(token);
    }

    /**
     * Ermittelt die wahre Identität eines Spielers für die aktuelle Runde.
     *
     * @param card1 erste Charakterkarte des Spielers (darf nicht null sein)
     * @param card2 zweite Charakterkarte des Spielers (darf nicht null sein)
     * @return der CharacterType, der die wahre Identität des Spielers darstellt
     * @throws IllegalArgumentException wenn eine der Karten null ist
     */
    public CharacterType getTrueIdentity() {

        if(this.characterCards.size() < 2) {
            throw new IllegalStateException();
        }

        Character card1 = this.characterCards.get(0);
        Character card2 = this.characterCards.get(1);


        if (card1 == null || card2 == null) {
            throw new IllegalArgumentException("Beide Charakterkarten müssen vorhanden sein (nicht null).");
        }

        // Regel 1: Beide Karten zeigen dasselbe Charakterbild → diese Identität gilt.
        if (card1.getTrueIdentity() == card2.getTrueIdentity()) {
            return card1.getTrueIdentity();
        }

        // Regel 2: Unterschiedliche Bilder → Karte mit höherem Wert bestimmt die Identität.
        //          Bei Gleichstand (sollte laut Regelwerk nicht auftreten) gewinnt Karte 1.
        return (card2.getRankValue() > card1.getRankValue())
                ? card2.getTrueIdentity()
                : card1.getTrueIdentity();
    }

    /**
     * Gibt alle Marken zurück, die sichtbar vor diesem Spieler liegen
     * (von allen anderen Spielern zusammen).
     *
     * @return unveränderliche Kopie aller platzierten Marken (nach Spieler-ID gruppiert)
     */
    public Map<Integer, List<HintToken>> getPlacedTokensFromOthers() {
        Map<Integer, List<HintToken>> copy = new HashMap<>();
        placedTokensFromOthers.forEach(
                (k, v) -> copy.put(k, Collections.unmodifiableList(v)));
        return Collections.unmodifiableMap(copy);
    }

    /**
     * Gibt den eigenen Hinweismarken-Stapel als unveränderliche Liste zurück.
     *
     * @return verbleibende Hinweismarken des Spielers
     */
    public List<HintToken> getHintTokens() {
        return Collections.unmodifiableList(hintTokenStack);
    }


    /** @return Spielernummer innerhalb der Partie */
    public int getId() { return id; }

    /** @return eindeutige interne Spieler-ID */
    public int getUid() { return uid; }

    /** @return Spielername */
    public String getName() { return name; }

    /** @param name neuer Spielername */
    public void setName(String name) { this.name = name; }

    /** @return unveränderliche Liste der Charakterkarten */
    public List<Character> getCharacterCards() {
        return Collections.unmodifiableList(characterCards);
    }

    /** @return unveränderliche Liste der Fragekarten */
    public List<QuestionCard> getQuestionCards() {
        return Collections.unmodifiableList(questionCards);
    }
}
