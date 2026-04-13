import java.util.ArrayList;
import java.util.List;

public class questionCardDeck {
    //Strings fuer die verschiedenen Fragen
    private static final String question1 = "Spielst du einen der Charaktere Wolf, Jäger oder Schäfer?";
    private static final String question2 = "Spielst du einen der Charaktere Schaf, Jäger oder Schäfer?";
    private static final String question3 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jagdhund?";
    private static final String question4 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jäger?";
    private static final String question5 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Schaf?";

    private final List<questionCard> cards;

    //Enum fuer alle Fragekarten. Anzahl der verschiedenen Fragen definiert
    public questionCardDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) cards.add(new questionCard(question1));
        for (int i = 0; i < 4; i++) cards.add(new questionCard(question2));
        for (int i = 0; i < 3; i++) cards.add(new questionCard(question3));
        for (int i = 0; i < 3; i++) cards.add(new questionCard(question4));
        for (int i = 0; i < 2; i++) cards.add(new questionCard(question5));
    }

    //getter fuer Cards
    public List<questionCard> getCards() {
        return cards;
    }

}