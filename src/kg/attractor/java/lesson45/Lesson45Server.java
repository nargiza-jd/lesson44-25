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

        registerGet("/", ex -> redirect303(ex, "/auth/login"));

        registerGet ("/auth/login", this::loginGet);
        registerPost("/auth/login", this::loginPost);

        registerGet("/books", ex -> {
            String html = "<h1>Добро пожаловать в библиотеку!</h1>";
            try {
                sendByteData(ex,
                        ResponseCodes.OK,
                        ContentType.TEXT_HTML,
                        html.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) {}
        });
    }

    private final Map<String, String> users = Map.of(
            "test@example.com",  "1234",
            "admin@mail.com",    "admin"
    );

    private void loginPost(HttpExchange ex) {
        Map<String,String> form = parseFormData(body(ex));

        String email    = form.get("email");
        String password = form.get("password");

        if (users.containsKey(email) && users.get(email).equals(password)) {

            redirect303(ex, "/books");
        } else {

            redirect303(ex, "/auth/login?error=1");
        }
    }

    private void loginGet(HttpExchange ex)  {
        Map<String,Object> data = new HashMap<>();

        String q = ex.getRequestURI().getQuery();
        if (q != null && q.contains("error=1")) {
            data.put("error", "Неверный email или пароль");
        }

        renderTemplate(ex, "auth/login.ftlh", data);
    }

    private Map<String, String> parseFormData(String raw) {
        Map<String,String> res = new HashMap<>();
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                String k = java.net.URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String v = java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                res.put(k, v);
            }
        }
        return res;
    }
}