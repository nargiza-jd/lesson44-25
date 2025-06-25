package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.server.ContentType;

import java.io.IOException;
import java.nio.file.Path;

public class Lesson45Server extends Lesson44Server{
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
    }

    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }
}
