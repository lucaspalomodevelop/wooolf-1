package main.java;

import main.java.controller.Player;
import main.java.model.Character;
import main.java.model.CharacterType;
import main.java.model.QuestionCard;

import java.util.ArrayList;

public class Wooolf {

    /**
     * Stellt eine Fragekarte im Namen von asker an target.
     *
     * Akzeptanzkriterien:
     * - Nur eigene Fragekarten nutzbar (IllegalArgumentException sonst).
     * - Zielspieler muss wählbar sein – simulierter Spieler (uid == -1) ist ausgeschlossen.
     * - Antwort ist "Ja", wenn die Frage auf mindestens eine Charakterkarte des Ziels passt.
     * - Fragekarte wird danach vom asker entfernt (verbraucht).
     *
     * @param asker  Spieler, der die Frage stellt
     * @param target Spieler, der befragt wird
     * @param card   die gespielte Fragekarte
     * @return "Ja" oder "Nein"
     */
    public String compare_questioncard(Player asker, Player target, QuestionCard card) {

        // 1. Karte muss dem asker gehören
        if (!asker.getQuestionCards().contains(card)) {
            throw new IllegalArgumentException(
                    "Spieler " + asker.getName() + " besitzt diese Fragekarte nicht.");
        }

        // 2. Simulierter Spieler (uid == -1) darf nicht befragt werden
        if (target.getUid() == -1) {
            throw new IllegalArgumentException(
                    "Der simulierte Spieler kann nicht befragt werden.");
        }

        // 3. Frage mit Charakterkarten des Ziels abgleichen
        boolean match = matchesQuestion(card.getQuestion(), target);

        // 4. Fragekarte verbrauchen – wir brauchen Zugriff auf die interne Liste,
        //    daher arbeiten wir über eine removeQuestionCard-Methode (siehe unten)
        asker.removeQuestionCard(card);

        return match ? "Ja" : "Nein";
    }

    /**
     * Prüft, ob die Frage auf mindestens eine Charakterkarte des Zielspielers passt.
     */
    private boolean matchesQuestion(String question, Player target) {
        for (Character c : target.getCharacterCards()) {
            if (characterMatchesQuestion(c.getCharacterType(), question)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Abbildung: Frage → relevante CharacterTypes.
     * Die Strings entsprechen exakt den Fragen im QuestionCardDeck.
     */
    private boolean characterMatchesQuestion(CharacterType type, String question) {
        return switch (question) {
            case "Spielst du einen der Charaktere Wolf, Jäger oder Schäfer?" ->
                    type == CharacterType.WOLF
                            || type == CharacterType.HUNTER
                            || type == CharacterType.SHEPHERD;

            case "Spielst du einen der Charaktere Schaf, Jäger oder Schäfer?" ->
                    type == CharacterType.SHEEP
                            || type == CharacterType.HUNTER
                            || type == CharacterType.SHEPHERD;

            case "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jagdhund?" ->
                    type == CharacterType.HUNTINGDOG;

            case "Hat mindestens eine Deiner Charakterkarten das Charakterbild Jäger?" ->
                    type == CharacterType.HUNTER;

            case "Hat mindestens eine Deiner Charakterkarten das Charakterbild Schaf?" ->
                    type == CharacterType.SHEEP;

            default -> false;
        };
    }

    public static void main(String[] args) {
        ArrayList<CharacterType> targets1 = new ArrayList<>();
        targets1.add(CharacterType.SHEEP);
        Character c1 = new Character(CharacterType.WOLF, CharacterType.WOLF, 5, targets1);

        ArrayList<CharacterType> targets2 = new ArrayList<>();
        targets2.add(CharacterType.HUNTINGDOG);
        targets2.add(CharacterType.HUNTER);
        Character c2 = new Character(CharacterType.SHEEP, CharacterType.SHEEP, 1, targets2);

        System.out.println(c1);
        System.out.println(c2);

        // IdentityResolver.resolveIdentity(c1, c2) → Player.getTrueIdentity()
        Player tempPlayer = new Player(0, 0, "temp");
        tempPlayer.addCharacterCard(c1);
        tempPlayer.addCharacterCard(c2);
        System.out.println(tempPlayer.getTrueIdentity());
    }
}