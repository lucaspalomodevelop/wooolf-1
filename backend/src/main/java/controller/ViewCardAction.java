package main.java.controller;

import main.java.model.character.Character;
import main.java.model.character.CharacterType;
import main.java.model.player.Player;
import main.java.model.token.HintToken;

import java.util.List;

/**
 * Implementiert die Spielaktion "Charakterkarte ansehen".
 *
 * <h2>Ablauf</h2>
 * <ol>
 *   <li>Der aktive Spieler wählt eine Charakterkarte eines anderen Spielers aus.</li>
 *   <li>Der aktive Spieler sieht kurzzeitig die <em>wahre Identität</em> (Hauptbild).</li>
 *   <li>Alle anderen Spieler sehen nur das <em>Erscheinungsbild</em> (Rückseite / appearance).</li>
 *   <li><strong>Schäfer-Sonderregel:</strong> Ist die aufgelöste Identität des aktiven Spielers
 *       {@link CharacterType#SHEPHERD}, wird für alle anderen Spieler stets
 *       {@link CharacterType#WOLF} angezeigt – unabhängig vom tatsächlichen Erscheinungsbild.
 *       Ausgenommen sind Fragezeichen-Karten (appearance == {@code null}).</li>
 *   <li>Der aktive Spieler entnimmt die passende {@link HintToken} aus seinem Stapel
 *       und legt sie sichtbar vor den Besitzer der angesehenen Karte.</li>
 * </ol>
 */
public class ViewCardAction {



    private final AppearanceResolver appearanceResolver;

    /**
     * Erstellt eine ViewCardAction mit der Standard-Schäfer-Regel.
     * Dieser Konstruktor wird im normalen Spielbetrieb verwendet.
     */
    public ViewCardAction() {
        this.appearanceResolver = new ShepherdAppearanceResolver();
    }

    /**
     * Erstellt eine ViewCardAction mit einer eigenen Resolver-Strategie.
     * Nützlich für Tests oder zukünftige Regeländerungen.
     *
     * @param appearanceResolver die zu verwendende Strategie
     */
    public ViewCardAction(AppearanceResolver appearanceResolver) {
        this.appearanceResolver = appearanceResolver;
    }


    /**
     * Führt die "Charakterkarte ansehen"-Aktion aus.
     *
     * <p>Schritte:</p>
     * <ol>
     *   <li>Bestimmt die wahre Identität des aktiven Spielers.</li>
     *   <li>Bestimmt das öffentliche Erscheinungsbild der gewählten Karte (inkl. Schäfer-Regel).</li>
     *   <li>Entnimmt dem aktiven Spieler die passende Hinweismarke und platziert sie.</li>
     * </ol>
     *
     * @param activePlayer  der Spieler, der die Aktion ausführt (darf nicht {@code null} sein)
     * @param targetPlayer  der Spieler, dessen Karte angesehen wird (darf nicht {@code null} sein)
     * @param cardIndex     Index der Karte im Kartenstapel des Zielspielers (0 oder 1)
     * @return              ein {@link ViewCardResult} mit öffentlichem Erscheinungsbild,
     *                      wahrer Identität und angewendeter Regel
     * @throws IllegalArgumentException wenn Spieler {@code null} sind oder der Index ungültig ist
     * @throws IllegalStateException    wenn der aktive Spieler keine passende Hinweismarke besitzt
     *                                  oder keine zwei Charakterkarten hat
     */
    public ViewCardResult execute(Player activePlayer, Player targetPlayer, int cardIndex) {
        validateInputs(activePlayer, targetPlayer, cardIndex);

        // 1. Wahre Identität der angesehenen Karte
        Character targetCard = targetPlayer.getCharacterCards().get(cardIndex);
        CharacterType trueIdentity = targetCard.getTrueIdentity();

        // 2. Erscheinungsbild für die Öffentlichkeit bestimmen
        CharacterType publicAppearance = appearanceResolver.resolve(activePlayer, targetCard);
        boolean shepherdRuleApplied = (publicAppearance == CharacterType.WOLF)
                && (targetCard.getAppearance() != CharacterType.WOLF);

        // 3. Passende Hinweismarke entnehmen und vor dem Zielspieler platzieren
        placeHintToken(activePlayer, targetPlayer, publicAppearance);

        return new ViewCardResult(publicAppearance, trueIdentity, shepherdRuleApplied);
    }

    // -----------------------------------------------------------------------
    // Private Hilfsmethoden
    // -----------------------------------------------------------------------

    /**
     * Bestimmt das öffentlich sichtbare Erscheinungsbild der gewählten Karte.
     *
     * <p>Schäfer-Sonderregel: Hat der aktive Spieler die Identität Schäfer,
     * wird für alle anderen immer {@link CharacterType#WOLF} angezeigt –
     * sofern kein Fragezeichen vorliegt (appearance != {@code null}).</p>
     *
     * @param activePlayer der aktive Spieler
     * @param targetCard   die angesehene Karte
     * @return das öffentlich sichtbare Erscheinungsbild
     */
    /*private CharacterType resolvePublicAppearance(Player activePlayer, Character targetCard) {
        CharacterType appearance = targetCard.getAppearance();

        // Fragezeichen-Karten (null) sind von der Schäfer-Regel ausgenommen
        if (appearance == null) {
            return null;
        }

        if (isShepherd(activePlayer)) {
            return CharacterType.WOLF;
        }

        return appearance;
    }*/

    /**
     * Prüft, ob die aktuelle Identität des aktiven Spielers {@link CharacterType#SHEPHERD} ist.
     *
     * @param activePlayer der zu prüfende Spieler
     * @return {@code true} wenn die aufgelöste Identität Schäfer ist
     * @throws IllegalStateException wenn der Spieler nicht genau zwei Charakterkarten hat
     */
    /*private boolean isShepherd(Player activePlayer) {
        return activePlayer.getTrueIdentity() == CharacterType.SHEPHERD;
    }*/

    /**
     * Entnimmt dem aktiven Spieler die zum Erscheinungsbild passende Hinweismarke
     * und legt sie sichtbar vor den Zielspieler.
     *
     * <p>Die Hinweismarke wird anhand des {@code publicAppearance}-Werts gewählt:
     * Es wird die erste Marke ausgewählt, deren {@link HintToken#getSideA()} oder
     * {@link HintToken#getSideB()} dem Erscheinungsbild entspricht.</p>
     *
     * @param activePlayer    der Spieler, der die Marke abgibt
     * @param targetPlayer    der Spieler, vor dem die Marke platziert wird
     * @param publicAppearance das öffentliche Erscheinungsbild
     * @throws IllegalStateException wenn keine passende Hinweismarke im Stapel vorhanden ist
     */
    private void placeHintToken(Player activePlayer, Player targetPlayer,
                                CharacterType publicAppearance) {
        if (publicAppearance == null) {
            // Fragezeichen-Karte: keine Hinweismarke wird platziert
            return;
        }

        HintToken token = activePlayer.removeHintToken(publicAppearance)
                .orElseThrow(() -> new IllegalStateException(
                        "Spieler " + activePlayer.getId()
                                + " hat keine passende Hinweismarke für: " + publicAppearance));

        targetPlayer.receiveHintToken(token, activePlayer.getId());
    }

    /**
     * Validiert die Eingabeparameter der Aktion.
     */
    private void validateInputs(Player activePlayer, Player targetPlayer, int cardIndex) {
        if (activePlayer == null) {
            throw new IllegalArgumentException("activePlayer darf nicht null sein.");
        }
        if (targetPlayer == null) {
            throw new IllegalArgumentException("targetPlayer darf nicht null sein.");
        }
        if (activePlayer.getId() == targetPlayer.getId()) {
            throw new IllegalArgumentException(
                    "Ein Spieler kann nicht seine eigene Karte ansehen.");
        }
        List<Character> targetCards = targetPlayer.getCharacterCards();
        if (cardIndex < 0 || cardIndex >= targetCards.size()) {
            throw new IllegalArgumentException(
                    "Ungültiger Kartenindex: " + cardIndex
                            + ". Zielspieler hat " + targetCards.size() + " Karten.");
        }
    }
}
