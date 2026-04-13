/**
 * Stellt eine Fragekarte im Spiel dar.
 * Jede Karte enthält genau eine Frage.
 */
public class questionCard {
    private String question;

    //Erstellt die Fragekarten mit den bestimmten Fragen
    public questionCard(String question) {
        this.question = question;
    }

    //Gibt die Frage der Fragekarte zurueck
    public String getQuestion() {
        return question;
    }
}