package main.java.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import main.java.controller.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * HTTP-Server für das Spiel.
 * <p>
 * Stellt eine API bereit, um Gewinnerinformationen abzurufen.
 * Aktuell verfügbar:
 * <ul>
 *     <li><b>/api/winner</b> - Gibt den/die Gewinner des Spiels zurück</li>
 * </ul>
 */
public class GameServer {

    /** Interner HTTP-Server. */
    private final HttpServer server;

    /** Liste aller Spieler der aktuellen Runde. */
    private List<Player> players;

    /**
     * Erstellt einen neuen GameServer.
     *
     * @param port Port, auf dem der Server laufen soll
     * @param players Aktuelle Spielerliste
     * @throws IOException Falls der HTTP-Server nicht gestartet werden kann
     */
    public GameServer(int port, List<Player> players) throws IOException {
        this.players = players;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        this.server.createContext("/api/winner", new WinnerHandler());
        this.server.createContext("/", new FallbackHandler());

        this.server.setExecutor(null);
    }

    /**
     * Aktualisiert die Spielerliste.
     *
     * @param players Neue Liste der Spieler
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Startet den HTTP-Server.
     */
    public void start() {
        server.start();
        System.out.println("GameServer läuft auf Port " +
                server.getAddress().getPort());
    }

    /**
     * Stoppt den HTTP-Server sofort.
     */
    public void stop() {
        server.stop(0);
    }

    /**
     * Handler für die Gewinner-API.
     * <p>
     * Unterstützt ausschließlich GET-Anfragen.
     */
    private class WinnerHandler implements HttpHandler {

        /**
         * Verarbeitet eingehende HTTP-Anfragen.
         *
         * @param exchange HTTP-Anfrage/-Antwort Kontext
         * @throws IOException Falls beim Schreiben der Antwort ein Fehler auftritt
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405,
                        "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            // Spiel noch nicht gestartet
            if (players == null || players.isEmpty()) {
                sendResponse(exchange, 200,
                        "{\"status\":\"waiting\", " +
                                "\"message\":\"No players registered yet.\"}");
                return;
            }

            List<Player> winners = determineWinners(players);
            String json = buildWinnerJson(winners);

            sendResponse(exchange, 200, json);
        }
    }

    /**
     * Fallback-Handler für unbekannte Endpunkte.
     */
    private class FallbackHandler implements HttpHandler {

        /**
         * Gibt eine 404-Antwort zurück.
         *
         * @param exchange HTTP-Anfrage/-Antwort Kontext
         * @throws IOException Falls beim Schreiben der Antwort ein Fehler auftritt
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response =
                    "{\"error\":\"Not Found\", \"path\":\""
                            + exchange.getRequestURI().getPath()
                            + "\"}";

            sendResponse(exchange, 404, response);
        }
    }

    /**
     * Ermittelt den oder die Gewinner des Spiels.
     * <p>
     * Zuerst werden Spieler mit den meisten Punkten bestimmt.
     * Bei Gleichstand gewinnt der Spieler mit den wenigsten Fehlerpunkten.
     *
     * @param players Liste aller Spieler
     * @return Liste der Gewinner (bei Gleichstand mehrere)
     */
    static List<Player> determineWinners(List<Player> players) {

        int maxPoints = players.stream()
                .mapToInt(Player::getPoints)
                .max()
                .orElse(0);

        List<Player> topPlayers = players.stream()
                .filter(p -> p.getPoints() == maxPoints)
                .toList();

        // Kein Gleichstand
        if (topPlayers.size() == 1) {
            return topPlayers;
        }

        int minErrors = topPlayers.stream()
                .mapToInt(Player::getErrorTokensRed)
                .min()
                .orElse(0);

        return topPlayers.stream()
                .filter(p ->
                        p.getErrorTokensRed() == minErrors
                                || p.getErrorTokensBlack() == minErrors)
                .toList();
    }

    /**
     * Baut die JSON-Antwort für Gewinnerinformationen.
     *
     * @param winners Gewinnerliste
     * @return JSON-String mit Gewinnerdaten
     */
    private String buildWinnerJson(List<Player> winners) {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\"winners\":[");

        for (int i = 0; i < winners.size(); i++) {
            Player p = winners.get(i);

            sb.append("{");
            sb.append("\"id\":").append(p.getUid()).append(",");
            sb.append("\"name\":\"")
                    .append(escape(p.getName()))
                    .append("\",");
            sb.append("\"points\":")
                    .append(p.getPoints())
                    .append(",");
            sb.append("\"errorTokensRed\":")
                    .append(p.getErrorTokensRed())
                    .append(",");
            sb.append("\"errorTokensBlack\":")
                    .append(p.getErrorTokensBlack());
            sb.append("}");

            if (i < winners.size() - 1) {
                sb.append(",");
            }
        }

        sb.append("],");
        sb.append("\"count\":").append(winners.size());
        sb.append("}");

        return sb.toString();
    }

    /**
     * Escaped Sonderzeichen für JSON-Strings.
     *
     * @param s Eingabestring
     * @return Escapeter String
     */
    private String escape(String s) {
        return s == null
                ? ""
                : s.replace("\\", "\\\\")
                  .replace("\"", "\\\"");
    }

    /**
     * Sendet eine HTTP-JSON-Antwort.
     *
     * @param exchange HTTP-Kontext
     * @param status HTTP-Statuscode
     * @param body Antwortinhalt als JSON
     * @throws IOException Falls die Antwort nicht gesendet werden kann
     */
    private void sendResponse(
            HttpExchange exchange,
            int status,
            String body
    ) throws IOException {

        byte[] bytes = body.getBytes("UTF-8");

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}