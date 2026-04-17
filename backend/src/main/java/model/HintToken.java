package main.java.model;

/**
 * Repräsentiert Hinweis-Tokens, die eine Beziehung zwischen zwei {@link CharacterType}-Instanzen beschreiben.
 * <p>
 * Jedes Token steht für eine mögliche Kombination zweier Charaktertypen im Spiel.
 */
public enum HintToken {

    /** Kombination aus Wolf und Hunter. */
    WOLF_HUNTER(CharacterType.Wolf, CharacterType.Hunter),

    /** Kombination aus Wolf und Sheep. */
    WOLF_SHEEP(CharacterType.Wolf, CharacterType.Sheep),

    /** Kombination aus Wolf und Huntingdog. */
    WOLF_HUNTINGDOG(CharacterType.Wolf, CharacterType.Huntingdog),

    /** Kombination aus Hunter und Sheep. */
    HUNTER_SHEEP(CharacterType.Hunter, CharacterType.Sheep),

    /** Kombination aus Huntingdog und Sheep. */
    HUNTINGDOG_SHEEP(CharacterType.Huntingdog, CharacterType.Sheep),

    /** Kombination aus Huntingdog und Hunter. */
    HUNTINGDOG_HUNTER(CharacterType.Huntingdog, CharacterType.Hunter);

    /** Erster Charaktertyp der Kombination. */
    private final CharacterType sideA;

    /** Zweiter Charaktertyp der Kombination. */
    private final CharacterType sideB;

    /**
     * Konstruktor für ein HintToken.
     *
     * @param sideA erster Charaktertyp
     * @param sideB zweiter Charaktertyp
     */
    HintToken(CharacterType sideA, CharacterType sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
    }

    /**
     * Gibt den ersten Charaktertyp zurück.
     *
     * @return erster Charaktertyp
     */
    public CharacterType getSideA() {
        return sideA;
    }

    /**
     * Gibt den zweiten Charaktertyp zurück.
     *
     * @return zweiter Charaktertyp
     */
    public CharacterType getSideB() {
        return sideB;
    }
}