
package main.java.model;

/**
 * Stellt eine Fragekarte im Spiel dar.
 * Jede Karte enthält genau eine Frage.
 */
public class QuestionCard {
    
    /**
    * Die Frage, die diese Karte enthält.
    */
    private String question;

    /**
    * Erstellt eine neue Fragekarte mit einer bestimmten Frage.
    *
    * @param question die Frage, die auf der Karte steht
    */
    public QuestionCard(String question) {
        this.question = question;
    }

    /**
    * Gibt die Frage dieser Karte zurück.
    *
    * @return die enthaltene Frage
    */
    public String getQuestion() {
        return question;
    }
}