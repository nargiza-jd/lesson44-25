package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.model.*;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.lesson44.SampleDataModel;

import java.io.IOException;
import java.util.*;

public class Lesson46Server extends Lesson45Server {

    protected final Map<String, EmployeeAuth> sessions = new HashMap<>();

    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);

        registerPost("/login", this::loginPost);

        registerGet("/profile", this::profile46);
        registerGet("/take",    this::takeBook);
        registerGet("/return",  this::returnBook);
        registerGet("/logout",  this::logout);
    }

    @Override
    protected void loginPost(HttpExchange ex) {
        Map<String, String> f = parseFormData(body(ex));
        String email = f.get("email");
        String pass = f.get("password");

        EmployeeAuth user = employees.stream()
                .filter(e -> e.getEmail().equalsIgnoreCase(email)
                        && e.getPassword().equals(pass))
                .findFirst()
                .orElse(null);

        if (user == null) {
            redirect303(ex, "/login?error=1");
            return;
        }

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);

        Cookie<String> sid = Cookie.of("SID", sessionId)
                .maxAge(600)
                .httpOnly();
        setCookie(ex, sid);

        redirect303(ex, "/profile");
    }


    private void profile46(HttpExchange ex) {
        EmployeeAuth user = findUserBySession(ex);
        if (user == null) {
            redirect303(ex, "/login");
            return;
        }

        Map<String,Object> data = new HashMap<>();
        data.put("email",    user.getEmail());
        data.put("fullname", user.getFullName());
        renderTemplate(ex, "profile.ftlh", data);
    }

    private void takeBook(HttpExchange ex) {
        EmployeeAuth u = findUserBySession(ex);
        if (u == null) { redirect303(ex, "/login"); return; }

        String id   = getQueryParam(ex, "id");
        Book  book  = SampleDataModel.getBookById(id);
        if (book == null || book.getStatus() != BookStatus.AVAILABLE) {
            redirect303(ex, "/books");
            return;
        }
        if (u.getIssuedBookIds().size() >= 2) {
            redirect303(ex, "/books?error=max2");
            return;
        }

        u.getIssuedBookIds().add(book.getId());
        book.setStatus(BookStatus.ISSUED);
        book.setHolderEmail(u.getEmail());

        redirect303(ex, "/books");
    }

    private void returnBook(HttpExchange ex) {
        EmployeeAuth u = findUserBySession(ex);
        if (u == null) { redirect303(ex, "/login"); return; }

        String id  = getQueryParam(ex, "id");
        Book  book = SampleDataModel.getBookById(id);
        if (book == null) { redirect303(ex, "/books"); return; }

        if (u.getIssuedBookIds().remove(book.getId())) {
            book.setStatus(BookStatus.AVAILABLE);
            book.setHolderEmail(null);
        }
        redirect303(ex, "/books");
    }

    private void logout(HttpExchange ex) {
        String sid = readSessionId(ex);
        if (sid != null) sessions.remove(sid);

        setCookie(ex, Cookie.of("SID", "deleted").maxAge(0).httpOnly());
        redirect303(ex, "/login");
    }

    private String readSessionId(HttpExchange ex) {
        return Cookie.parse(getCookies(ex)).get("SID");
    }

    protected EmployeeAuth findUserBySession(HttpExchange ex) {
        String sid = readSessionId(ex);
        return sid == null ? null : sessions.get(sid);
    }


    protected String getQueryParam(HttpExchange ex, String key) {
        String q = ex.getRequestURI().getQuery();
        if (q == null) return null;
        for (String p : q.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return null;
    }

    protected String getQueryParams(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery == null ? "" : rawQuery;
    }
}