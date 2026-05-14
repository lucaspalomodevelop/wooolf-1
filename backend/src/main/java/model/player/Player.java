package main.java.model.player;

import main.java.model.character.Character;
import main.java.model.card.QuestionType;
import main.java.model.character.CharacterType;
import main.java.model.card.QuestionCard;
import main.java.model.token.ErrorTokens;
import main.java.model.token.HintToken;

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

    /** Eindeutige interne ID des Spielers. */
    private final int uid;

    /** Spielernummer innerhalb einer Partie. */
    private final int id;

    /** Anzeigename des Spielers. */
    private String name;

    /** Die Charakterkarten des Spielers (maximal 2). */
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
     * Punkte des Spielers, die im Spielverlauf gesammelt werden.
     * Wird für die Siegerermittlung benötigt.
     */
    private int points;

    /**
     * Fehlermarken des Spielers.
     * Bei Punktegleichstand entscheidet die niedrigere Anzahl an Fehlermarken.
     */
    private final ErrorTokens errorTokens = new ErrorTokens();

    /** Speichert ob der Spieler eine frühe Verdächtigung abgegeben hat. */
    private boolean playerHasEarlySuspicion;


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
        this.points = 0;
        this.playerHasEarlySuspicion = false;
    }


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
     * @param type der Typ der zuzuweisenden Fragekarte
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
     * Entnimmt die erste passende Hinweismarke aus dem eigenen Stapel.
     *
     * @param appearance das gesuchte Erscheinungsbild (sideA oder sideB)
     * @return ein {@link Optional} mit der passenden Marke, oder {@link Optional#empty()}
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
     * @param token        die platzierte Hinweismarke
     * @param fromPlayerId ID des Spielers, der die Marke legt
     */
    public void receiveHintToken(HintToken token, int fromPlayerId) {
        placedTokensFromOthers
                .computeIfAbsent(fromPlayerId, k -> new ArrayList<>())
                .add(token);
    }


    /**
     * Gibt die aktuellen Punkte des Spielers zurück.
     *
     * @return Punktzahl (>= 0)
     */
    public int getPoints() {
        return points;
    }

    /**
     * Addiert den angegebenen Wert zu den Punkten des Spielers.
     * Negative Werte werden ignoriert.
     *
     * @param amount zu addierende Punkte (muss > 0 sein)
     * @throws IllegalArgumentException wenn amount <= 0
     */
    public void addPoints(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Punkte müssen positiv sein, war: " + amount);
        }
        this.points += amount;
    }

    /**
     * Setzt die Punkte des Spielers auf einen konkreten Wert (z. B. beim Laden).
     *
     * @param points neuer Punktestand (>= 0)
     * @throws IllegalArgumentException wenn points < 0
     */
    public void setPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Punktestand darf nicht negativ sein.");
        }
        this.points = points;
    }

    /**
     * Gibt die Anzahl der Fehlermarken des Spielers zurück.
     *
     * @return Fehlermarken-Anzahl (>= 0)
     */
    public int getErrorTokensRed()         { return errorTokens.getRed(); }
    public int getErrorTokensBlack()       { return errorTokens.getBlack(); }

    /**
     * Erhöht die Fehlermarken um 1.
     * Wird aufgerufen, wenn der Spieler einen Fehler macht.
     */
    public void addErrorTokenRed()         { errorTokens.addRed(); }
    public void addErrorTokenBlack()       { errorTokens.addBlack(); }

    public int getErrorTokenMinimum()      { return errorTokens.getMinimum(); }
    public ErrorTokens getErrorTokens()    { return errorTokens; }

    /**
     * Addiert mehrere Fehlermarken auf einmal (z. B. beim Laden eines Spielstands).
     *
     * @param amount zu addierende Fehlermarken (muss >= 0 sein)
     * @throws IllegalArgumentException wenn amount < 0
     */
    public void addErrorTokens(int amount) { errorTokens.addBoth(amount); }


    /**
     * Ermittelt die wahre Identität eines Spielers für die aktuelle Runde.
     *
     * @return der CharacterType, der die wahre Identität des Spielers darstellt
     * @throws IllegalStateException    wenn weniger als 2 Charakterkarten vorhanden sind
     * @throws IllegalArgumentException wenn eine der Karten null ist
     */
    public CharacterType getTrueIdentity() {
        if (this.characterCards.size() < 2) {
            throw new IllegalStateException(
                    "Spieler " + id + " hat noch keine 2 Charakterkarten.");
        }

        Character card1 = this.characterCards.get(0);
        Character card2 = this.characterCards.get(1);

        if (card1 == null || card2 == null) {
            throw new IllegalArgumentException(
                    "Beide Charakterkarten müssen vorhanden sein (nicht null).");
        }

        // Regel 1: Beide Karten zeigen dasselbe Bild → diese Identität gilt.
        if (card1.getTrueIdentity() == card2.getTrueIdentity()) {
            return card1.getTrueIdentity();
        }

        // Regel 2: Unterschiedliche Bilder → Karte mit höherem Wert entscheidet.
        return (card2.getRankValue() > card1.getRankValue())
                ? card2.getTrueIdentity()
                : card1.getTrueIdentity();
    }

    /** @return Spielernummer innerhalb der Partie */
    public int getId() { return id; }

    /** @return eindeutige interne Spieler-ID */
    public int getUid() { return uid; }

    /** @return Spielername */
    public String getName() { return name; }

    /** @param name neuer Spielername */
    public void setName(String name) { this.name = name; }

    /** @return unverändерliche Liste der Charakterkarten */
    public List<Character> getCharacterCards() {
        return Collections.unmodifiableList(characterCards);
    }

    /** @return unveränderliche Liste der Fragekarten */
    public List<QuestionCard> getQuestionCards() {
        return Collections.unmodifiableList(questionCards);
    }

    /** @return unveränderlicher Hinweismarken-Stapel */
    public List<HintToken> getHintTokens() {
        return Collections.unmodifiableList(hintTokenStack);
    }

    /** @return gibt zurück ob der Spieler eine frühe Verdächtigung abgegeben hat */
    public boolean isPlayerHasEarlySuspicion() {
        return playerHasEarlySuspicion;
    }

    /** Ändert ob der Spieler eine frühe Verdächtigung abgegeben hat*/
    public void setPlayerHasEarlySuspicion(boolean playerHasEarlySuspicion) {
        this.playerHasEarlySuspicion = playerHasEarlySuspicion;
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
}