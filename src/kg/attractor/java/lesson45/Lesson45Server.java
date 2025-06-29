package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/", exchange -> redirect303(exchange, "auth/login"));


        registerGet("/auth/login", this::loginGet);
        registerPost("/auth/login", this::loginPost);

    }

    private final Map<String, String> users = Map.of(
            "test@example.com", "1234",
            "admin@mail.com", "admin"
    );

    private void loginPost(HttpExchange exchange) {
        String raw = body(exchange);
        Map<String, String> form = parseFormData(raw);

        String email = form.get("email");
        String password = form.get("password");

        String message;

        if (users.containsKey(email) && users.get(email).equals(password)) {
            message = "<h2>Добро пожаловать, " + email + "!</h2>";
        } else {
            message = "<h2 style='color:red'>Неверный email или пароль!</h2>";
        }

        try {
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String cType = exchange.getRequestHeaders()
//                .getOrDefault("Content-Type", List.of())
//                .get(0);
//
//        String raw = getBody(exchange);
//
//        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
//        String fmt = "<p>Необработанные данные: <b>%s</b></p>" +
//                "<p>Content-Type: <b>%s</b></p>" +
//                "<p>После обработки: <b>%s</b></p>";
//        String data = String.format(fmt, raw, cType, parsed);
//
//        try{
//            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data.getBytes());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }

    private void loginGet(HttpExchange exchange) throws IOException {
        Path path = makePath("auth", "login.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }


    private Map<String, String> parseFormData(String raw) {
        Map<String, String> result = new HashMap<>();
        for (String pair : raw.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }
}