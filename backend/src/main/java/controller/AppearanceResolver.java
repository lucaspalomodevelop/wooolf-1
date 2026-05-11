package main.java.controller;

import main.java.model.character.CharacterType;
import main.java.model.character.Character;
import main.java.model.player.Player;

/**
 * Strategie zur Bestimmung des öffentlich sichtbaren Erscheinungsbilds
 * einer Charakterkarte.
 *
 * Unterschiedliche Implementierungen können das Erscheinungsbild
 * nach verschiedenen Regeln auflösen (z.B. Schäfer-Sonderregel).
 */
public interface AppearanceResolver {

    /**
     * Bestimmt das Erscheinungsbild, das alle Mitspieler sehen.
     *
     * @param activePlayer der Spieler, der die Karte ansieht
     * @param targetCard   die angesehene Karte
     * @return das öffentlich sichtbare Erscheinungsbild, oder null bei Fragezeichen-Karte
     */
    CharacterType resolve(Player activePlayer, Character targetCard);
}