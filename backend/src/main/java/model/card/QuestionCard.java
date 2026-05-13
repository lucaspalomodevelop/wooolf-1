package main.java.model.card;

public class QuestionCard {

    private final QuestionType type;

    public QuestionCard(QuestionType type) {
        this.type = type;
    }

    public QuestionType getType() {
        return type;
    }
    public String getQuestion() {
        return type.getQuestionText();
    }
}