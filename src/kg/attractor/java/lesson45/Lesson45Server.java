package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.IOException;
import java.nio.file.Path;

public class Lesson45Server extends Lesson44Server {
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/", exchange -> redirect303(exchange, "auth/login"));


        registerGet("/auth/login", this::loginGet);
        registerPost("/auth/login", this::loginPost);
    }

    private void loginPost(HttpExchange exchange) {
        String raw = body(exchange);

        String response = "<h1>Вы отправили:</h1><p>" + raw + "</p>";

        try {
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, response.getBytes());
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
}