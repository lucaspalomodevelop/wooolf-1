package main.java.controller;

import main.java.model.character.Character;
import main.java.model.character.CharacterType;
import main.java.model.player.Player;

/**
 * Standardregel: gibt das Erscheinungsbild der Karte unverändert zurück.
 * Wird verwendet wenn keine Sonderregel aktiv ist.
 */
public class DefaultAppearanceResolver implements AppearanceResolver {

    @Override
    public CharacterType resolve(Player activePlayer, Character targetCard) {
        return targetCard.getAppearance();
    }
}