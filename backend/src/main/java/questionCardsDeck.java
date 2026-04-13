import java.util.ArrayList;
import java.util.List;

public class questionCardDeck {
    //Strings fuer die verschiedenen Fragen
    private static final String Question1 = "Spielst du einen der Charaktere Wolf, Jäger oder Schäfer?";
    private static final String Question2 = "Spielst du einen der Charaktere Schaf, Jäger oder Schäfer?";
    private static final String Question3 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jagdhund?";
    private static final String Question4 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jäger?";
    private static final String Question5 = "Hat mindestens eine Deiner Charakterkarten das Charakterbild Schaf?";

    private final List<QuestionCard> cards;

    //Enum fuer alle Fragekarten. Anzahl der verschiedenen Fragen definiert
    public QuestionCardDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) cards.add(new QuestionCard(Question1));
        for (int i = 0; i < 4; i++) cards.add(new QuestionCard(Question2));
        for (int i = 0; i < 3; i++) cards.add(new QuestionCard(Question3));
        for (int i = 0; i < 3; i++) cards.add(new QuestionCard(Question4));
        for (int i = 0; i < 2; i++) cards.add(new QuestionCard(Question5));
    }

    //getter fuer Cards
    public List<QuestionCard> getCards() {
        return cards;
    }

}