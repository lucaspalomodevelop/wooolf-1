package main.java.controller;

import main.java.model.Character;
import main.java.model.HintToken;
import main.java.model.QuestionCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repräsentiert einen Spieler im Spiel.
 * Ein Spieler besitzt genau zwei Charakterkarten, zwei Fragekarten
 * sowie einen Satz von Hinweismarken.
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
    private final List<main.java.model.Character> characterCards;

    /**
     * Die Fragekarten des Spielers (maximal 2).
     */
    private final List<QuestionCard> questionCards;

    /**
     * Der Satz an Hinweismarken des Spielers (typischerweise 6 Stück).
     */
    private final List<HintToken> hintTokens = new ArrayList<>();

    /**
     * Erstellt einen neuen Spieler.
     *
     * @param uid  eindeutige interne ID
     * @param id   Spielernummer in der Partie
     * @param name Anzeigename des Spielers
     */
    //bin mir aber noch etwas unsicher hier @lp19?
    public Player(int uid, int id, String name) {
       this.uid = uid;
       this.id = id;
       this.name = name;
       this.characterCards = new ArrayList<>();
       this.questionCards  = new ArrayList<>();
        this.hintTokens.addAll(List.of(HintToken.values()));
    }

    public Player(int i, int uid, int id, List<Character> characterCards, List<QuestionCard> questionCards, String name) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.characterCards = characterCards;
        this.questionCards = questionCards;
        this.hintTokens.addAll(List.of(HintToken.values()));
    }

    /**
     * Fügt eine Charakterkarte hinzu (max. 2).
     *
     * @param character die zuzuweisende Charakterkarte
     * @throws IllegalStateException wenn bereits 2 Charakterkarten vorhanden sind
     */
    public void addCharacterCard(main.java.model.Character character) {
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
    public void addQuestionCard(QuestionCard card) {
        if (questionCards.size() >= 2) {
            throw new IllegalStateException(
                    "Spieler " + id + " hat bereits 2 Fragekarten.");
        }
        questionCards.add(card);
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
     *
     * @return Spielernummer innerhalb der Partie
     */
    public int getId() {
        return id;
    }

    /**
     * Gibt die eindeutige interne ID des Spielers zurück.
     *
     * @return eindeutige Spieler-ID
     */
    public int getUid() {
        return uid;
    }

    /**
     * Gibt den Namen des Spielers zurück.
     *
     * @return Spielername
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Spielers.
     *
     * @param name neuer Spielername
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt eine unveränderliche Liste der Charakterkarten zurück.
     *
     * @return Charakterkarten (nach Setup typischerweise genau 2)
     */
    public List<Character> getCharacterCards() {
        return Collections.unmodifiableList(characterCards);
    }

    /**
     * Gibt eine unveränderliche Liste der Fragekarten zurück.
     *
     * @return Fragekarten (nach Setup typischerweise genau 2)
     */
    public List<QuestionCard> getQuestionCards() {
        return Collections.unmodifiableList(questionCards);
    }

    /**
     * Gibt eine unveränderliche Liste der Hinweismarken zurück.
     *
     * @return Hinweismarken des Spielers
     */
    public List<HintToken> getHintTokens() {
        return Collections.unmodifiableList(hintTokens);
    }
}