package main.java.controller;

import main.java.model.CharacterType;

/**
 * Ergebnisobjekt der "Charakterkarte ansehen"-Aktion.
 *
 * <p>Kapselt, was der aktive Spieler sieht (Hauptbild / true identity)
 * und was alle anderen Spieler sehen (Erscheinungsbild / appearance),
 * sowie das tatsächlich ausgespielte Erscheinungsbild für die Hinweismarke.</p>
 */
public class ViewCardResult {

    /**
     * Das Erscheinungsbild, das alle Spieler sehen (ggf. durch Schäfer-Regel überschrieben).
     * Dies ist auch der Wert, der auf die Hinweismarke gedrückt wird.
     */
    private final CharacterType publicAppearance;

    /**
     * Die wahre Identität der Karte – nur dem aktiven Spieler kurzzeitig sichtbar.
     */
    private final CharacterType trueIdentity;

    /**
     * Gibt an, ob die Schäfer-Sonderregel angewendet wurde.
     */
    private final boolean shepherdRuleApplied;

    /**
     * Erstellt ein neues ViewCardResult.
     *
     * @param publicAppearance    das öffentlich sichtbare Erscheinungsbild
     * @param trueIdentity        die wahre Identität (nur für den aktiven Spieler)
     * @param shepherdRuleApplied {@code true} wenn die Schäfer-Sonderregel greift
     */
    public ViewCardResult(CharacterType publicAppearance,
                          CharacterType trueIdentity,
                          boolean shepherdRuleApplied) {
        this.publicAppearance = publicAppearance;
        this.trueIdentity = trueIdentity;
        this.shepherdRuleApplied = shepherdRuleApplied;
    }

    /**
     * Gibt das Erscheinungsbild zurück, das für alle Mitspieler sichtbar ist.
     * Bei der Schäfer-Sonderregel immer {@link CharacterType#WOLF}.
     *
     * @return öffentliches Erscheinungsbild
     */
    public CharacterType getPublicAppearance() {
        return publicAppearance;
    }

    /**
     * Gibt die wahre Identität zurück – nur dem aktiven Spieler kurzzeitig zugänglich.
     *
     * @return wahre Identität der angesehenen Karte
     */
    public CharacterType getTrueIdentity() {
        return trueIdentity;
    }

    /**
     * Gibt an, ob die Schäfer-Sonderregel angewendet wurde.
     *
     * @return {@code true} wenn die Schäfer-Sonderregel aktiv war
     */
    public boolean isShepherdRuleApplied() {
        return shepherdRuleApplied;
    }

    @Override
    public String toString() {
        return "ViewCardResult{" +
                "publicAppearance=" + publicAppearance +
                ", trueIdentity=" + trueIdentity +
                ", shepherdRuleApplied=" + shepherdRuleApplied +
                '}';
    }
}
