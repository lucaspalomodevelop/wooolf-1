package main.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein Deck aus Fragekarten.
 * 
 * Das Deck enthält eine vordefinierte Anzahl an Fragekarten
 * mit unterschiedlichen Fragen, die mehrfach vorkommen können.
 */
public class QuestionCardDeck {
    /**
    * Verschiedene vordefinierte Fragen für die Karten.
    */
    private static final String question1 = "Spielst du einen der Charaktere Wolf, Jäger oder Schäfer?";
    private static final String question2 = "Spielst du einen der Charaktere Schaf, Jäger oder Schäfer?";
    private static final String question3 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jagdhund?";
    private static final String question4 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jäger?";
    private static final String question5 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Schaf?";

    /**
    * Liste aller Fragekarten im Deck.
    */
    private final List<QuestionCard> cards;

    /**
    * Erstellt ein neues Fragekarten-Deck.
    * 
    * Die Anzahl der jeweiligen Fragen ist fest definiert
    * und wird beim Erstellen des Decks automatisch befüllt.
    */
    public QuestionCardDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) cards.add(new QuestionCard(question1));
        for (int i = 0; i < 4; i++) cards.add(new QuestionCard(question2));
        for (int i = 0; i < 3; i++) cards.add(new QuestionCard(question3));
        for (int i = 0; i < 3; i++) cards.add(new QuestionCard(question4));
        for (int i = 0; i < 2; i++) cards.add(new QuestionCard(question5));
    }

    /**
    * Erstellt ein neues Fragekarten-Deck.
    * 
    * Die Anzahl der jeweiligen Fragen ist fest definiert
    * und wird beim Erstellen des Decks automatisch befüllt.
    */
    public List<QuestionCard> getCards() {
        return cards;
    }

}