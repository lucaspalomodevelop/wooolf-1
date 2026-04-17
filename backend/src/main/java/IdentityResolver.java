package main.java;

/**
 * Bestimmt die wahre Identität eines Spielers anhand seiner zwei Charakterkarten.
 *
 * Regeln (aus dem Regelwerk):
 *  1. Zeigen beide Karten dasselbe Charakterbild → dieses Bild ist die Identität.
 *  2. Zeigen die Karten unterschiedliche Bilder  → die Karte mit dem höheren Wert
 *     (rankValue) bestimmt die Identität.
 *
 * Erscheinungsbild und Zielvorgaben haben keinen Einfluss auf diese Berechnung.
 */
public class IdentityResolver {

    /**
     * Ermittelt die wahre Identität eines Spielers für die aktuelle Runde.
     *
     * @param card1 erste Charakterkarte des Spielers (darf nicht null sein)
     * @param card2 zweite Charakterkarte des Spielers (darf nicht null sein)
     * @return der CharacterType, der die wahre Identität des Spielers darstellt
     * @throws IllegalArgumentException wenn eine der Karten null ist
     */
    public static CharacterType resolveIdentity(character card1, character card2) {
        if (card1 == null || card2 == null) {
            throw new IllegalArgumentException("Beide Charakterkarten müssen vorhanden sein (nicht null).");
        }

        // Regel 1: Beide Karten zeigen dasselbe Charakterbild → diese Identität gilt.
        if (card1.getTrueIdentity() == card2.getTrueIdentity()) {
            return card1.getTrueIdentity();
        }

        // Regel 2: Unterschiedliche Bilder → Karte mit höherem Wert bestimmt die Identität.
        //          Bei Gleichstand (sollte laut Regelwerk nicht auftreten) gewinnt Karte 1.
        return (card2.getRankValue() > card1.getRankValue())
                ? card2.getTrueIdentity()
                : card1.getTrueIdentity();
    }
}