package main.java;

import main.java.model.game.Game;
import main.java.model.game.GameSetup;
import main.java.model.player.Player;
import main.java.model.character.Character;
import main.java.model.card.QuestionType;
import main.java.model.character.CharacterType;
import main.java.model.card.QuestionCard;
import main.java.server.GameServer;
import java.io.IOException;

import java.util.ArrayList;

public class Wooolf {

    private static Boolean DEBUG = true;
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
        boolean match = matchesQuestion(card.getType(), target);

        // 4. Fragekarte verbrauchen – wir brauchen Zugriff auf die interne Liste,
        //    daher arbeiten wir über eine removeQuestionCard-Methode (siehe unten)
        asker.removeQuestionCard(card);

        return match ? "Ja" : "Nein";
    }

    /**
     * Prüft, ob die Frage auf mindestens eine Charakterkarte des Zielspielers passt.
     */
    private boolean matchesQuestion(QuestionType question, Player target) {
        for (Character c : target.getCharacterCards()) {
            if (characterMatchesQuestion(c.getCharacterType(), question)) {
                return true;
            }
        }
        return false;
    }

    private boolean characterMatchesQuestion(CharacterType type, QuestionType question) {
        return switch (question) {
            case WOLF_HUNTER_SHEPHERD -> type == CharacterType.WOLF
                    || type == CharacterType.HUNTER
                    || type == CharacterType.SHEPHERD;
            case SHEEP_HUNTER_SHEPHERD -> type == CharacterType.SHEEP
                    || type == CharacterType.HUNTER
                    || type == CharacterType.SHEPHERD;
            case HAS_HUNTINGDOG -> type == CharacterType.HUNTINGDOG;
            case HAS_HUNTER     -> type == CharacterType.HUNTER;
            case HAS_SHEEP      -> type == CharacterType.SHEEP;
        };
    }

    public static void main(String[] args) throws IOException {

        GameSetup gameSetup = null;
        Game game = null;
        if (DEBUG) { //TODO das müssen wir nach der Präsi dann noch mal schöner lösen
            ArrayList<Player> players = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                players.add(new Player(i, i, "Spieler " + i));
            }

            ArrayList<CharacterType> targets1 = new ArrayList<>();
            targets1.add(CharacterType.SHEEP);
            Character c1 = new Character(CharacterType.WOLF, CharacterType.WOLF, 5, targets1);

            ArrayList<CharacterType> targets2 = new ArrayList<>();
            targets2.add(CharacterType.HUNTINGDOG);
            targets2.add(CharacterType.HUNTER);
            Character c2 = new Character(CharacterType.SHEEP, CharacterType.SHEEP, 1, targets2);

            players.get(0).addCharacterCard(c1);
            players.get(0).addCharacterCard(c2);


            game = new Game(8);

            for (Player p : game.getPlayers()) {
                System.out.println("Spieler: " + p.getName()
                        + " | Charakterkarten: " + p.getCharacterCards()
                        + " | Fragekarten: " + p.getQuestionCards());
            }

// Rundenlogik ist jetzt auch direkt verfügbar:
            System.out.println("Aktuelle kleine Runde: " + game.getCurrentSmallRound());
            System.out.println("Aktuelle große Runde: " + game.getCurrentBigRound());
            System.out.println("Spiel vorbei: " + game.isGameOver());
        }
        GameServer gameServer = new GameServer(8080, null);
        gameServer.start();
        gameServer.setPlayers(game.getPlayers());
    }

}