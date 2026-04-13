/**
 * Stellt eine Fragekarte im Spiel dar.
 * Jede Karte enthält genau eine Frage.
 */
public class questionCard {
    private final String question;

    //Erstellt die Fragekarten mit den bestimmten Fragen
    public QuestionCard(String question) {
        this.question = question;
    }

    //Gibt die Frage der Fragekarte zurueck
    public String getQuestion() {
        return question;
    }
}