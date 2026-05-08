package main.java.model;

public enum QuestionType {

    WOLF_HUNTER_SHEPHERD(
            "Spielst du einen der Charaktere Wolf, Jäger oder Schäfer?",
            4
    ),
    SHEEP_HUNTER_SHEPHERD(
            "Spielst du einen der Charaktere Schaf, Jäger oder Schäfer?",
            4
    ),
    HAS_HUNTINGDOG(
            "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jagdhund?",
            3
    ),
    HAS_HUNTER(
            "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jäger?",
            3
    ),
    HAS_SHEEP(
            "Hat mindestens eine Deiner Charakterkarten das Charakterbild Schaf?",
            2
    );

    private final String questionText;
    private final int deckCount;

    QuestionType(String questionText, int deckCount) {
        this.questionText = questionText;
        this.deckCount = deckCount;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getDeckCount() {
        return deckCount;
    }
}