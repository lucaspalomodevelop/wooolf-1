package main.java.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import main.java.controller.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;


public class GameServer {

    private final HttpServer server;
    private List<Player> players;

    public GameServer(int port, List<Player> players) throws IOException {
        this.players = players;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        this.server.createContext("/api/winner", new WinnerHandler());

        this.server.createContext("/", new FallbackHandler());

        this.server.setExecutor(null);
    }

    public void setPlayers(List<Player> players) {this.players = players;}

    public void start() {
        server.start();
        System.out.println("GameServer läuft auf Port " + server.getAddress().getPort());
    }

    public void stop() {server.stop(0);}

    private class WinnerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            // Fall: Spiel hat noch nicht gestartet oder keine Spieler vorhanden
            if (players == null || players.isEmpty()) {
                sendResponse(exchange, 200, "{\"status\":\"waiting\", \"message\":\"No players registered yet.\"}");
                return;
            }

            List<Player> winners = determineWinners(players);
            String json = buildWinnerJson(winners);
            sendResponse(exchange, 200, json);
        }
    }

    private class FallbackHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"error\":\"Not Found\", \"path\":\"" + exchange.getRequestURI().getPath() + "\"}";
            sendResponse(exchange, 404, response);
        }
    }

    static List<Player> determineWinners(List<Player> players) {
        int maxPoints = players.stream()
                .mapToInt(Player::getPoints)
                .max()
                .orElse(0);

        List<Player> topPlayers = players.stream()
                .filter(p -> p.getPoints() == maxPoints)
                .toList();

        if (topPlayers.size() == 1) {
            return topPlayers;
        }

        int minErrors = topPlayers.stream()
                .mapToInt(Player::getErrorTokensRed)
                .min()
                .orElse(0);

        return topPlayers.stream()
                .filter(p -> p.getErrorTokensRed() == minErrors || p.getErrorTokensBlack() == minErrors)
                .toList();
    }

    private String buildWinnerJson(List<Player> winners) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"winners\":[");

        for (int i = 0; i < winners.size(); i++) {
            Player p = winners.get(i);
            sb.append("{");
            sb.append("\"id\":").append(p.getUid()).append(",");
            sb.append("\"name\":\"").append(escape(p.getName())).append("\",");
            sb.append("\"points\":").append(p.getPoints()).append(",");
            sb.append("\"errorTokensRed\":").append(p.getErrorTokensRed()).append(",");
            sb.append("\"errorTokensBlack\":").append(p.getErrorTokensBlack());
            sb.append("}");
            if (i < winners.size() - 1) sb.append(",");
        }

        sb.append("],");
        sb.append("\"count\":").append(winners.size());
        sb.append("}");
        return sb.toString();
    }

    private String escape(String s) {return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");}

    private void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}