package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.server.Cookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson46Server extends Lesson45Server {
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/cookie", this::cookieHandler);
    }

    private void cookieHandler(HttpExchange exchange) {
        Cookie sessionCookie = Cookie.make("useId", "12@3");
        setCookie(exchange, sessionCookie);

        Cookie c1 = Cookie.make("user%Id", "12@3");
        setCookie(exchange, c1);

        Cookie c2 = Cookie.make("email", "qwe@qwe.qwe");
        setCookie(exchange, c2);

        Cookie c3 = Cookie.make("restricted_()<>{}[],;:\\\"|?=", "()<>{}[],;:\\\"|?=");
        setCookie(exchange, c3);

        String cookieReceived = getCookies(exchange);

        Map<String, String> cookies = Cookie.parse(cookieReceived);

        Map<String, Object> data = new HashMap<>();
        int times = 42;
        data.put("times", times);
        data.put("cookie", cookies);
        renderTemplate(exchange, "cookie.ftlh", data);
    }
}
