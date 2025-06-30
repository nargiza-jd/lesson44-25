package kg.attractor.java.lesson45;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.model.EmployeeAuth;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {


    private final Map<String, Map<String, String>> users = new HashMap<>();

    private final Path authPath = Path.of("data/json/auth.json");
    private List<EmployeeAuth> employees = new ArrayList<>();


    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        loadUsersFromFile();

        users.put("test@example.com", Map.of("password", "1234",  "fullname", "Тест Пользователь"));
        users.put("admin@mail.com",   Map.of("password", "admin", "fullname", "Администратор"));

        registerGet ("/",              exchange -> redirect303(exchange, "/auth/login"));
        registerGet ("/auth/login",    this::loginGet);
        registerPost("/auth/login",    this::loginPost);
        registerGet ("/register",      this::registerGet);
        registerPost("/register",      this::registerPost);


        registerGet("/static/", exchange -> serveStatic(exchange, Path.of("data")));
    }

    private void loginGet(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        String q = exchange.getRequestURI().getQuery();
        if (q != null) {
            if (q.contains("error=1"))          data.put("error",   "Неверный e-mail или пароль");
            if (q.contains("register=success")) data.put("success", "Регистрация прошла успешно. Войдите.");
        }
        renderTemplate(exchange, "auth/login.ftlh", data);
    }

    private void loginPost(HttpExchange exchange) {
        Map<String,String> f = parseFormData(body(exchange));
        String email = f.get("email");
        String pass  = f.get("password");

        boolean match = employees.stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email)
                        && e.getPassword().equals(pass));

        if (match) {
            redirect303(exchange, "/books");
        } else {
            redirect303(exchange, "/auth/login?error=1");
        }
    }

    private void registerGet(HttpExchange exchange) {
        Map<String,Object> data = new HashMap<>();
        String q = exchange.getRequestURI().getQuery();
        if (q != null && q.contains("error=1"))
            data.put("error", "Такой пользователь уже существует");
        renderTemplate(exchange, "auth/register.ftlh", data);
    }

    private void registerPost(HttpExchange exchange) {
        Map<String,String> f = parseFormData(body(exchange));
        String email    = f.get("email");
        String pass     = f.get("password");
        String fullname = f.get("fullname");


        boolean exists = employees.stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email));

        if (exists) {
            redirect303(exchange, "/register?error=1");
            return;
        }

        EmployeeAuth newUser = new EmployeeAuth(email, fullname, pass);
        employees.add(newUser);
        saveUsersToFile();

        redirect303(exchange, "/auth/login?register=success");
    }

    private void serveStatic(HttpExchange exchange, Path dataDir) {
        String reqPath = exchange.getRequestURI().getPath();
        String relPath = reqPath.replaceFirst("^/static/?", "");
        Path file = dataDir.resolve(relPath);

        ContentType ct = fromMimeType(detectMimeType(file.toString()));
        try {
            if (Files.exists(file)) sendFile(exchange, file, ct);
            else sendByteData(exchange, ResponseCodes.NOT_FOUND,
                    ContentType.TEXT_PLAIN, "Not found".getBytes());
        } catch (IOException ioe) {
            try {
                sendByteData(exchange, ResponseCodes.SERVER_ERROR,
                        ContentType.TEXT_PLAIN, "Server error".getBytes());
            } catch (IOException ignored) {}
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


    private void loadUsersFromFile() {
        try {
            if (Files.exists(authPath)) {
                String json = Files.readString(authPath);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<EmployeeAuth>>() {}.getType();
                employees = gson.fromJson(json, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(employees);
            Files.writeString(authPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}