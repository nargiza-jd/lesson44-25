package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.server.Cookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Lesson46Server extends Lesson45Server {
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/cookie", this::cookieHandler);
    }

    private void cookieHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();

        String name = "times";
        String cookieReceived = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookieReceived);

        String cookieValue = cookies.getOrDefault(name, "0");
        int times = Integer.parseInt(cookieValue) + 1;

        Cookie responseCookie = new Cookie(name, times);

        setCookie(exchange, responseCookie);

        Cookie cookieId = new Cookie("uuid", UUID.randomUUID().toString());
        setCookie(exchange, cookieId);

        data.put(name, times);
        data.put("cookies", cookies);


        renderTemplate(exchange, "cookie.ftlh", data);
    }
}
