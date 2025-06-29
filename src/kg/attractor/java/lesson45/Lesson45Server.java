package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {


    private final Map<String, Map<String, String>> users = new HashMap<>();

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        users.put("test@example.com", Map.of("password", "1234",  "fullname", "Тест Пользователь"));
        users.put("admin@mail.com",   Map.of("password", "admin", "fullname", "Администратор"));

        registerGet ("/",              ex -> redirect303(ex, "/auth/login"));
        registerGet ("/auth/login",    this::loginGet);
        registerPost("/auth/login",    this::loginPost);
        registerGet ("/register",      this::registerGet);
        registerPost("/register",      this::registerPost);

        registerGet("/books", ex -> sendByteData(
                ex, ResponseCodes.OK, ContentType.TEXT_HTML,
                "<h1>Добро пожаловать в библиотеку!</h1>".getBytes()));


        registerGet("/static/", ex -> serveStatic(ex, Path.of("data")));
    }

    private void loginGet(HttpExchange ex) {
        Map<String,Object> data = new HashMap<>();
        String q = ex.getRequestURI().getQuery();
        if (q != null) {
            if (q.contains("error=1"))          data.put("error",   "Неверный e-mail или пароль");
            if (q.contains("register=success")) data.put("success", "Регистрация прошла успешно. Войдите.");
        }
        renderTemplate(ex, "auth/login.ftlh", data);
    }

    private void loginPost(HttpExchange ex) {
        Map<String,String> f    = parseFormData(body(ex));
        String email            = f.get("email");
        String pass             = f.get("password");
        Map<String,String> usr  = users.get(email);

        if (usr != null && pass.equals(usr.get("password")))
            redirect303(ex, "/books");
        else
            redirect303(ex, "/auth/login?error=1");
    }

    private void registerGet(HttpExchange ex) {
        Map<String,Object> data = new HashMap<>();
        String q = ex.getRequestURI().getQuery();
        if (q != null && q.contains("error=1"))
            data.put("error", "Такой пользователь уже существует");
        renderTemplate(ex, "auth/register.ftlh", data);
    }

    private void registerPost(HttpExchange ex) {
        Map<String,String> f = parseFormData(body(ex));
        String email    = f.get("email");
        String pass     = f.get("password");
        String fullname = f.get("fullname");

        if (users.containsKey(email))
            redirect303(ex, "/register?error=1");
        else {
            users.put(email, Map.of("password", pass, "fullname", fullname));
            redirect303(ex, "/auth/login?register=success");
        }
    }

    private void serveStatic(HttpExchange ex, Path dataDir) {
        String reqPath = ex.getRequestURI().getPath();
        String relPath = reqPath.replaceFirst("^/static/?", "");
        Path   file    = dataDir.resolve("static").resolve(relPath);

        ContentType ct = fromMimeType(detectMimeType(file.toString()));
        try {
            if (Files.exists(file)) sendFile(ex, file, ct);
            else                    sendByteData(ex, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, "Not found".getBytes());
        } catch (IOException ioe) {
            try { sendByteData(ex, ResponseCodes.SERVER_ERROR, ContentType.TEXT_PLAIN, "Server error".getBytes()); }
            catch (IOException ignored) {}
        }
    }

    private Map<String,String> parseFormData(String raw) {
        Map<String,String> res = new HashMap<>();
        for (String pair : raw.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2)
                res.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return res;
    }

    private String detectMimeType(String name) {
        if (name.endsWith(".css"))                       return "text/css";
        if (name.endsWith(".png"))                       return "image/png";
        if (name.matches(".*\\.(jpe?g)$"))               return "image/jpeg";
        if (name.endsWith(".html"))                      return "text/html";
        return "text/plain";
    }

    private ContentType fromMimeType(String mt) {
        return switch (mt) {
            case "text/css"   -> ContentType.TEXT_CSS;
            case "image/png"  -> ContentType.IMAGE_PNG;
            case "image/jpeg" -> ContentType.IMAGE_JPEG;
            case "text/html"  -> ContentType.TEXT_HTML;
            default           -> ContentType.TEXT_PLAIN;
        };
    }
}