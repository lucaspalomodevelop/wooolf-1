package main.java.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repräsentiert einen Charakter im Spiel.
 * 
 * Ein Charakter besitzt eine geheime Identität, eine sichtbare Erscheinung,
 * einen Rangwert zur Vergleichbarkeit sowie optionale Zielvorgaben.
 */
public class Character {

        /**
         * Die wahre Identität des Charakters (nur dem Besitzer bekannt).
         */
        private final CharacterType trueIdentity;

        /**
        * Die Erscheinung des Charakters (was andere Spieler sehen).
        */
        private final CharacterType appearance;

        /**
         * Wert zur Rangfolge-Bestimmung.
         * Ein höherer Wert bedeutet einen höheren Rang.
         */
        private final int rankValue;

        
        // könnten nachher klassen sein anstatt String
        /**
        * Liste spezifischer Zielvorgaben dieser Karte
        * (z.B. "Jäger", "Wolf").
        */
        private final List<CharacterType> targets;


        /**
        * Erstellt eine neue Character-Instanz.
        *
        * @param trueIdentity Die wahre Identität des Charakters
        * @param appearance   Die sichtbare Erscheinung des Charakters
        * @param rankValue    Der Rangwert zur Vergleichbarkeit
        * @param targets      Liste der Zielvorgaben
        */
        public Character(CharacterType trueIdentity, CharacterType appearance, int rankValue, List<CharacterType> targets) {
            this.trueIdentity = trueIdentity;
            this.appearance   = appearance;
            this.rankValue    = rankValue;
            this.targets      = new ArrayList<>(targets);
        }

        /**
        * Gibt die wahre Identität des Charakters zurück.
        *
        * @return die geheime Identität
        */
        public CharacterType getTrueIdentity() {
            return trueIdentity;
        }

        /**
        * Gibt die sichtbare Erscheinung des Charakters zurück.
        *
        * @return die Erscheinung
        */
        public CharacterType getAppearance() {
            return appearance;
        }

        /**
        * Gibt den Rangwert des Charakters zurück.
        *
        * @return der Rangwert
        */
        public int getRankValue() {
            return rankValue;
        }

        /**
         * Gibt eine unveränderliche Liste der Zielvorgaben zurück.
         *
         * @return unveränderliche Liste der Targets
         */
        public List<CharacterType> getTargets() {
            return Collections.unmodifiableList(targets);
        }

        public CharacterType getCharacterType() {return this.appearance;}


    /**
     *  overrides ToString Method
     */
    @Override
    public String toString() {
        return "character{" +
                "trueIdentity=" + trueIdentity +
                ", appearance=" + appearance +
                ", rankValue=" + rankValue +
                ", targets=" + targets +
                '}';
    }
}
