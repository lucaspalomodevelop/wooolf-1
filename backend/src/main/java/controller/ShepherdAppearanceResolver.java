package main.java.controller;

import main.java.model.character.Character;
import main.java.model.character.CharacterType;
import main.java.model.player.Player;

/**
 * Sonderregel: Ist der aktive Spieler ein Schäfer, sehen alle
 * anderen Spieler stets WOLF – unabhängig vom echten Erscheinungsbild.
 * Fragezeichen-Karten (appearance == null) sind ausgenommen.
 */
public class ShepherdAppearanceResolver implements AppearanceResolver {

    @Override
    public CharacterType resolve(Player activePlayer, Character targetCard) {
        CharacterType appearance = targetCard.getAppearance();

        if (appearance == null) {
            return null; // Fragezeichen-Karte: keine Regel greift
        }

        if (activePlayer.getTrueIdentity() == CharacterType.SHEPHERD) {
            return CharacterType.WOLF;
        }

        return appearance;
    }
}