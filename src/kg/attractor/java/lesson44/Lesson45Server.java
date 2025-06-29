package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;
import kg.attractor.java.utils.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class Lesson45Server extends Lesson44Server{
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
        registerPost("/login", (HttpHandler) this::loginPost);
    }

    private void loginPost(HttpExchange exchange) {
//        String cType = getContentType(exchange);
//        String raw = getBody(exchange);
//
//        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
//
//        String fmt = "<p>Необработанные данные: <b>%s</b></p>" +
//                "<p>Content-Type: <b>%s</b></p>" +
//                "<p>После обработки: <b>%s</b></p>";
//        String data = String.format(fmt, raw, cType, parsed);
//
//        try {
//            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        redirect303(exchange, "/");
    }

    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }
}
