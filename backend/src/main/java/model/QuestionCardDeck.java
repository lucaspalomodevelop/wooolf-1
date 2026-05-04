package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionCardDeck {

    private final List<QuestionCard> cards;

    public QuestionCardDeck() {
        cards = new ArrayList<>();
        for (QuestionType type : QuestionType.values()) {
            for (int i = 0; i < type.getDeckCount(); i++) {
                cards.add(new QuestionCard(type));
            }
        }
    }

    public List<QuestionCard> getCards() {
        return cards;
    }
}