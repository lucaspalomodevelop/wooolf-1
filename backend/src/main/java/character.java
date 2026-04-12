package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class character {

        // Die wahre Identität des Charakters (nur dem Besitzer bekannt)
        private final String trueIdentity;

        // Die Erscheinung des Charakters (was andere beim Ansehen sehen)
        private final String appearance;

        // Wert zur Rangfolge-Bestimmung. Höherer Wert = höherer Rang
        private final int rankValue;

        // Spezifische Zielvorgaben dieser Karte (z.B.: 'Jäger' & 'Wolf')
        private final List<String> targets;


        // Erstellt eine neue CharacterCard
        public character(String trueIdentity, String appearance, int rankValue, List<String> targets) {
            this.trueIdentity = trueIdentity;
            this.appearance   = appearance;
            this.rankValue    = rankValue;
            this.targets      = new ArrayList<>(targets);
        }

        public String getTrueIdentity() {
            return trueIdentity;
        }

        public String getAppearance() {
            return appearance;
        }

        public int getRankValue() {
            return rankValue;
        }

        public List<String> getTargets() {
            return Collections.unmodifiableList(targets);
        }
}
