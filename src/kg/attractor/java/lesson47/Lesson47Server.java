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
import java.util.*;
import java.util.stream.Collectors;

public class Lesson47Server extends Lesson46Server {

    public Lesson47Server(String host, int port) throws IOException {
        super(host, port);

        SampleDataModel.reloadBooks();

        registerGet("/books",        this::booksPage);
        registerGet("/book",         this::showBook);
        registerGet("/books/take",   this::takeBook);
        registerGet("/books/return", this::returnBook);
        registerGet("/mybooks",      this::myBooksPage);
        registerGet("/history",      this::historyPage);
        registerGet("/query",        this::handleQueryRequest);

        registerPost("/login",       this::loginPost);
    }

    private void booksPage(HttpExchange ex) {
        SampleDataModel.reloadBooks();
        Map<String,Object> data = new HashMap<>();
        data.put("books", SampleDataModel.getBooks());

        String errorParam = getQueryParam(ex, "error");
        if (errorParam != null && !errorParam.isEmpty()) {
            data.put("error", errorParam);
        }

        String successParam = getQueryParam(ex, "success");
        if (successParam != null && !successParam.isEmpty()) {
            data.put("success", successParam);
        }

        renderTemplate(ex, "books.ftlh", data);
    }

    private void myBooksPage(HttpExchange ex) {
        EmployeeAuth current = findUserBySession(ex);
        if (current == null) { redirect303(ex, "/login"); return; }

        SampleDataModel.reloadBooks();

        List<Book> mine = SampleDataModel.getBooks().stream()
                .filter(b -> current.getIssuedBookIds().contains(b.getId()))
                .collect(Collectors.toList());

        renderTemplate(ex, "mybooks.ftlh", Map.of("books", mine));
    }

    private void historyPage(HttpExchange ex) {
        EmployeeAuth current = findUserBySession(ex);
        if (current == null) { redirect303(ex, "/login"); return; }

        SampleDataModel.reloadBooks();

        List<String> userHistoryIds = current.getHistoryBookIds();

        List<Book> history = SampleDataModel.getBooks().stream()
                .filter(b -> userHistoryIds.contains(b.getId()))
                .toList();

        renderTemplate(ex, "history.ftlh", Map.of("books", history));
    }

    private void showBook(HttpExchange ex) {
        EmployeeAuth current = findUserBySession(ex);
        if (current == null) {
            redirect303(ex, "/login");
            return;
        }

        String id   = getQueryParam(ex, "id");

        SampleDataModel.reloadBooks();

        Book  book  = SampleDataModel.getBookById(id);
        if (book == null) {
            redirect303(ex, "/books");
            return;
        }

        boolean wasReturned =
                "1".equals(Utils.parseUrlEncoded(
                        Optional.ofNullable(
                                ex.getRequestURI().getRawQuery()
                        ).orElse(""),
                        "&"
                ).get("returned"));

        Map<String,Object> data = new HashMap<>();
        data.put("book",  book);
        data.put("email", current.getEmail());
        if (wasReturned) data.put("returned", true);

        renderTemplate(ex, "book.ftlh", data);
    }

    private void takeBook(HttpExchange ex) {
        EmployeeAuth u = findUserBySession(ex);
        if (u == null) { redirect303(ex, "/login"); return; }

        String id  = getQueryParam(ex, "id");
        Book  book = SampleDataModel.getBookById(id);
        if (book == null || book.getStatus() != BookStatus.AVAILABLE) {
            redirect303(ex, "/books"); return;
        }
        if (u.getIssuedBookIds().size() >= 2) {
            redirect303(ex, "/books?error=max2"); return;
        }
        if (u.getIssuedBookIds().contains(id)) {
            redirect303(ex, "/books?error=alreadyTaken"); return;
        }

        u.getIssuedBookIds().add(id);
        book.setStatus(BookStatus.ISSUED);
        book.setHolderEmail(u.getEmail());

        SampleDataModel.saveAuthEmployees();
        SampleDataModel.saveBooksToJson();

        redirect303(ex, "/books?success=1");
    }

    protected void returnBook(HttpExchange ex) {
        EmployeeAuth u = findUserBySession(ex);
        if (u == null) { redirect303(ex, "/login"); return; }

        String id  = getQueryParam(ex, "id");
        Book  book = SampleDataModel.getBookById(id);
        if (book == null) { redirect303(ex, "/books"); return; }

        if (u.getIssuedBookIds().remove(id)) {
            System.out.println("Удаляем из выданных и добавляем в историю: " + id);
            u.getHistoryBookIds().add(id);
            book.setStatus(BookStatus.AVAILABLE);
            book.setHolderEmail(null);

            SampleDataModel.saveAuthEmployees();
            SampleDataModel.saveBooksToJson();
        }

        redirect303(ex, "/book?id=" + id + "&returned=1");
    }

    @Override
    protected String getQueryParam(HttpExchange ex, String key) {
        return super.getQueryParam(ex, key);
    }

    private void handleQueryRequest(HttpExchange ex) {
        String q = ex.getRequestURI().getRawQuery();
        Map<String,String> params = Utils.parseUrlEncoded(q == null ? "" : q, "&");
        renderTemplate(ex, "query.ftlh", Map.of("params", params));
    }

    @Override
    protected void loginPost(HttpExchange ex) {
        Map<String,String> form = parseFormData(body(ex));
        String email = form.get("email");
        String pass  = form.get("password");

        System.out.println("--- Login Attempt ---");
        System.out.println("Введенный email: " + email);
        System.out.println("Введенный пароль: " + pass);

        SampleDataModel.loadAuthEmployees();

        System.out.println("Проверка загруженных пользователей из SampleDataModel:");
        if (SampleDataModel.getAuthEmployees().isEmpty()) {
            System.out.println("Список authEmployees пуст после loadAuthEmployees!");
        } else {
            SampleDataModel.getAuthEmployees().forEach(e -> {
                System.out.println("  Загруженный из списка: " + e.getEmail() + " | " + e.getPassword());
            });
        }


        EmployeeAuth user = SampleDataModel.getAuthEmployees().stream()
                .filter(u -> {
                    boolean emailMatch = u.getEmail().equalsIgnoreCase(email);
                    boolean passwordMatch = u.getPassword().equals(pass);
                    System.out.println("  Сравнение: Email (" + u.getEmail() + " == " + email + ") -> " + emailMatch +
                            ", Пароль (" + u.getPassword() + " == " + pass + ") -> " + passwordMatch);
                    return emailMatch && passwordMatch;
                })
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("Пользователь не найден или пароль не совпадает. Перенаправление на /login?error=1");
            redirect303(ex, "/login?error=1"); return;
        }

        System.out.println("Успешный вход пользователя: " + user.getEmail());
        String sid  = UUID.randomUUID().toString();
        sessions.put(sid, user.getEmail());

        Cookie<String> c = Cookie.of("SID", sid)
                .maxAge(600)
                .httpOnly()
                .path("/");
        setCookie(ex, c);

        redirect303(ex, "/books");
    }
}