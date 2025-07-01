package kg.attractor.java.lesson47;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.SampleDataModel;
import kg.attractor.java.lesson46.Lesson46Server;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.BookStatus;
import kg.attractor.java.model.EmployeeAuth;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Lesson47Server extends Lesson46Server {

    public Lesson47Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/query", this::handleQueryRequest);
        registerGet("/books/take", this::takeBook);
        registerGet("/books/return", this::returnBook);

        registerPost("/login", this::loginPost);
    }

    private void handleQueryRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getRawQuery();

        Map<String, String> params = Utils.parseUrlEncoded(query, "&");

        Map<String, Object> data = new HashMap<>();
        data.put("params", params);

        renderTemplate(exchange, "query.ftlh", data);
    }

    private void takeBook(HttpExchange ex) {
        EmployeeAuth u = findUserBySession(ex);
        if (u == null) {
            redirect303(ex, "/login");
            return;
        }

        String id = getQueryParam(ex, "id");
        Book book = SampleDataModel.getBookById(id);
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
        if (u == null) {
            redirect303(ex, "/login");
            return;
        }

        String id = getQueryParam(ex, "id");
        Book book = SampleDataModel.getBookById(id);
        if (book == null) {
            redirect303(ex, "/books");
            return;
        }

        if (u.getIssuedBookIds().remove(book.getId())) {
            u.getHistoryBookIds().add(book.getId());
            book.setStatus(BookStatus.AVAILABLE);
            book.setHolderEmail(null);
        }

        redirect303(ex, "/books");
    }

    @Override
    protected void loginPost(HttpExchange ex) {
        Map<String, String> form = parseFormData(body(ex));
        String email = form.get("email");
        String pass = form.get("password");

        EmployeeAuth user = employees.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(pass))
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

        redirect303(ex, "/books");
    }
}