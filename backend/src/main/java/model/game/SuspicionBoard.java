package main.java.model.game;
import main.java.model.character.Character;
import main.java.model.character.CharacterType;

/**
 * Repräsentiert die persönliche Verdächtigungstafel eines Spielers.
 * Symbol X (keine Vermutung) = -1
 */


public class SuspicionBoard {


    private static final int NO_SUSPICION = -1;

    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 8;

    private int firstSuspicion; /*Erste Verdächtigung */
    private int secondSuspicion; /* Zweite Verdächtigung */
    private Character character; /* Referenz zum Charakter*/
    
    
    /* Erstellung einer neuen Verdächtigungstafel für einen Spieler (Konstruktor) 
    @param character: Der Charakter des Spielers, dessen wahre Identität für die Wolf-Sonderregel benötigt wird
    */
    public SuspicionBoard(Character character) {

        if (character == null) {
            throw new IllegalArgumentException("character darf nicht null sein.");
        }

        this.character = character;
        this.firstSuspicion = NO_SUSPICION;
        this.secondSuspicion = NO_SUSPICION;
    }
    

    /* Zurücksetzen der Verdächtigungstafel vor jeder Runde */
    public void resetSuspicion(Character character) {
        this.firstSuspicion = NO_SUSPICION;
        this.secondSuspicion = NO_SUSPICION;
        this.character = character;
    }


    /* Setzt die beiden Verdächtigungszeiger
    @param firstSuspicion: Index der ersten Vermutung (0-8 oder -1 für keine Vermutung)
    @param secondSuspicion: Index der zweiten Vermutung (0-8 oder -1; nur, wenn der Charakter nicht "Wolf" ist)
    */
    public void setSuspicion(int firstSuspicion, int secondSuspicion) {
        this.firstSuspicion = firstSuspicion;
        if (!character.getTrueIdentity().equals(CharacterType.WOLF)) {
            this.secondSuspicion = secondSuspicion;
        } else {
            this.secondSuspicion = NO_SUSPICION; // Keine zweite Vermutung für "Wolf"
        }
    }

    
    /* Gibt die erste Verdächtigung zurück
    @return: Index der ersten Vermutung (0-8 oder -1 für keine Vermutung)
     */
    public int getFirstSuspicion() {
        return firstSuspicion;
    }

    
    /* Gibt die zweite Verdächtigung zurück
    @return: Index der zweiten Vermutung (0-8 oder -1 für keine Vermutung)
     */
    public int getSecondSuspicion() {
        return secondSuspicion;
    }

}
