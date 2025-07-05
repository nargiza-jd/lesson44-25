package kg.attractor.java.lesson44;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;
import kg.attractor.java.model.EmployeeAuth; // Явно импортируем EmployeeAuth
// import kg.attractor.java.model.AuthData; // ЭТОТ ИМПОРТ УДАЛЯЕМ, так как класса нет!

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class SampleDataModel {

    private static final Path BOOKS_FILE_PATH = Path.of("data", "json", "books.json");
    private static List<Book> books = new ArrayList<>();
    static {
        reloadBooks();
    }

    public static void reloadBooks() {
        books.clear();
        books.addAll(loadBooks());

    }

    private static List<Book> loadBooks() {
        try (Reader r = Files.newBufferedReader(BOOKS_FILE_PATH)) {
            Type t = new TypeToken<List<Book>>() {}.getType();
            List<Book> list = new Gson().fromJson(r, t);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Book> getBooks() {
        return books;
    }

    public static Book getBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    public static void saveBooksToJson() {
        try (Writer writer = Files.newBufferedWriter(BOOKS_FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final Path EMPLOYEES_FILE_PATH = Path.of("data", "json", "employees.json");
    private static List<Employee> employees = loadEmployees();

    private static List<Employee> loadEmployees() {
        try (Reader r = Files.newBufferedReader(EMPLOYEES_FILE_PATH)) {
            Type t = new TypeToken<List<Employee>>() {}.getType();
            List<Employee> list = new Gson().fromJson(r, t);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static Employee getEmployeeById(String id) {
        return employees.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }


    private static final Path AUTH_FILE_PATH = Path.of("data", "json", "auth.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<EmployeeAuth> authEmployees = new ArrayList<>();


    public static List<EmployeeAuth> getAuthEmployees() {
        return authEmployees;
    }

    public static void loadAuthEmployees() {
        try {
            Path authFilePath = Path.of("data", "json", "auth.json");

            String json = Files.readString(authFilePath);

            Type type = new TypeToken<List<EmployeeAuth>>() {}.getType();
            List<EmployeeAuth> loadedAuthEmployees = GSON.fromJson(json, type);

            authEmployees.clear();
            if (loadedAuthEmployees != null) {
                authEmployees.addAll(loadedAuthEmployees);
            }

            authEmployees.forEach(e -> System.out.println("Загруженный пользователь (из loadAuthEmployees): " + e.getEmail() + " | " + e.getPassword()));

        } catch (IOException e) {
            e.printStackTrace();
            authEmployees = new ArrayList<>();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            authEmployees = new ArrayList<>();
        }
    }


    public static void saveAuthEmployees() {
        try (Writer w = Files.newBufferedWriter(AUTH_FILE_PATH)) {
            GSON.toJson(authEmployees, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final User user = new User("John", "Doe");
    private final LocalDateTime currentDateTime = LocalDateTime.now();
    private final List<User> customers = List.of(
            new User("Marco"),
            new User("Winston", "Duarte"),
            new User("Amos", "Burton", "'Timmy'")
    );

    public User getUser() {
        return user;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public List<User> getCustomers() {
        return customers;
    }

    public static class User {
        private String firstName, lastName, middleName, email;
        private boolean emailConfirmed;

        public User(String fn) {
            this(fn, null, null);
        }

        public User(String fn, String ln) {
            this(fn, ln, null);
        }

        public User(String fn, String ln, String mn) {
            firstName = fn;
            lastName = ln;
            middleName = mn;
            email = fn + "@test.mail";
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String v) {
            firstName = v;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String v) {
            lastName = v;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String v) {
            middleName = v;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String v) {
            email = v;
        }

        public boolean isEmailConfirmed() {
            return emailConfirmed;
        }

        public void setEmailConfirmed(boolean v) {
            emailConfirmed = v;
        }
    }
}