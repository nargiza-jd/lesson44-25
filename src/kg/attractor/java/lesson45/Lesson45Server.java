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
import java.util.*;


public class Lesson45Server extends Lesson44Server {

    private final Path authPath = Path.of("data/json/auth.json");

    protected final List<EmployeeAuth> employees = new ArrayList<>();

    protected EmployeeAuth currentUser;

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        loadUsersFromFile();
        addTestUserIfAbsent("test@example.com", "1234",  "Тест Пользователь");
        addTestUserIfAbsent("admin@mail.com",   "admin", "Администратор");

        registerGet("/",            ex -> redirect303(ex, "/login"));
        registerGet("/login",       this::loginGet);
//        registerPost("/login",      this::loginPost);
        registerGet("/register",    this::registerGet);
        registerPost("/register",   this::registerPost);
        registerGet("/profile",     this::profileGet);

        registerGet("/static/", ex -> serveStatic(ex, Path.of("data")));
    }

    private void loginGet(HttpExchange ex) {
        Map<String,Object> data = new HashMap<>();
        String q = ex.getRequestURI().getQuery();
        if (q != null) {
            if (q.contains("error=1"))          data.put("error",   "Неверный e-mail или пароль");
            if (q.contains("register=success")) data.put("success", "Регистрация прошла успешно, войдите.");
        }
        renderTemplate(ex, "auth/login.ftlh", data);
    }

    protected void loginPost(HttpExchange ex) {
        Map<String,String> f = parseFormData(body(ex));
        String email = f.get("email");
        String pass  = f.get("password");

        currentUser = employees.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPassword().equals(pass))
                .findFirst()
                .orElse(null);

        redirect303(ex, currentUser == null ? "/login?error=1"
                : "/profile");
    }

    protected void registerGet(HttpExchange ex) {
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

        if (employees.stream().anyMatch(e -> e.getEmail().equalsIgnoreCase(email))) {
            redirect303(ex, "/register?error=1");
            return;
        }
        employees.add(new EmployeeAuth(email, fullname, pass));
        saveUsersToFile();
        redirect303(ex, "/login?register=success");
    }

    private void profileGet(HttpExchange ex) {
        Map<String,Object> data = new HashMap<>();
        if (currentUser != null) {
            data.put("email",    currentUser.getEmail());
            data.put("fullname", currentUser.getFullName());
        } else {
            data.put("email",    "anonymous@office");
            data.put("fullname", "Некий пользователь");
        }
        renderTemplate(ex, "profile.ftlh", data);
    }

    private void serveStatic(HttpExchange ex, Path baseDir) {
        String reqPath = ex.getRequestURI().getPath();
        String relPath = reqPath.replaceFirst("^/static/?", "");
        Path   file    = baseDir.resolve("static").resolve(relPath);

        ContentType ct = fromMimeType(detectMimeType(file.toString()));
        try {
            if (Files.exists(file))  sendFile(ex, file, ct);
            else                     sendByteData(ex, ResponseCodes.NOT_FOUND,
                    ContentType.TEXT_PLAIN, "Not found".getBytes());
        } catch (IOException ioe) {
            try { sendByteData(ex, ResponseCodes.SERVER_ERROR,
                    ContentType.TEXT_PLAIN, "Server error".getBytes()); }
            catch (IOException ignored) {}
        }
    }

    protected Map<String,String> parseFormData(String raw) {
        Map<String,String> res = new HashMap<>();
        for (String p : raw.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2)
                res.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return res;
    }

    private String detectMimeType(String name) {
        return name.endsWith(".css")  ? "text/css"   :
                name.endsWith(".png")  ? "image/png"  :
                        name.matches(".*\\.(jpe?g)$") ? "image/jpeg" :
                                name.endsWith(".html") ? "text/html"  :
                                        "text/plain";
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
        if (Files.notExists(authPath)) return;
        try {
            String json = Files.readString(authPath);
            Type type = new TypeToken<List<EmployeeAuth>>() {}.getType();
            List<EmployeeAuth> list = new Gson().fromJson(json, type);
            if (list != null) employees.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try {
            Files.createDirectories(authPath.getParent());
            String json = new GsonBuilder().setPrettyPrinting().create()
                    .toJson(employees);
            Files.writeString(authPath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTestUserIfAbsent(String email, String pw, String fn) {
        if (employees.stream().noneMatch(u -> u.getEmail().equalsIgnoreCase(email)))
            employees.add(new EmployeeAuth(email, fn, pw));
    }
}