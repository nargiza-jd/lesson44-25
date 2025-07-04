package kg.attractor.java.lesson44;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kg.attractor.java.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class SampleDataModel {


    private static final String BOOKS_FILE = "data/json/books.json";
    private static final List<Book> books = new ArrayList<>();
    static { reloadBooks(); }

    public static void reloadBooks() {
        books.clear();
        books.addAll(loadBooks());
    }
    private static List<Book> loadBooks() {
        try (Reader r = new FileReader(BOOKS_FILE)) {
            Type t = new TypeToken<List<Book>>(){}.getType();
            List<Book> list = new Gson().fromJson(r, t);

            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Book> getBooks()            { return books; }
    public static Book getBookById(String id)      {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    public static void saveBooksToJson() {
        System.out.println("Попытка сохранения книг в JSON");
        try (FileWriter writer = new FileWriter("data/json/books.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(books, writer);
            System.out.println("Книги успешно сохранены в JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при сохранении книг в JSON: " + e.getMessage());
        }
    }


    private static final List<Employee> employees = loadEmployees();
    private static List<Employee> loadEmployees() {
        try (Reader r = new FileReader("data/json/employees.json")) {
            Type t = new TypeToken<List<Employee>>(){}.getType();
            List<Employee> list = new Gson().fromJson(r, t);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) { return new ArrayList<>(); }
    }
    public static List<Employee> getEmployees()            { return employees; }
    public static Employee getEmployeeById(String id)      {
        return employees.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }


    private static final String AUTH_FILE = "data/json/auth.json";
    private static final Gson   GSON      = new GsonBuilder().setPrettyPrinting().create();
    private static List<EmployeeAuth> authEmployees = new ArrayList<>();

    public static List<EmployeeAuth> getAuthEmployees() { return authEmployees; }
    public static void loadAuthEmployees() {
        try (Reader r = new FileReader(AUTH_FILE)) {
            Type t = new TypeToken<List<EmployeeAuth>>(){}.getType();
            List<EmployeeAuth> list = GSON.fromJson(r, t);
            authEmployees = list == null ? new ArrayList<>() : list;
        } catch (FileNotFoundException ignored) {
            authEmployees = new ArrayList<>();
        } catch (IOException ignored) {
            authEmployees = new ArrayList<>();
        }
    }
    public static void saveAuthEmployees() {
        try (Writer w = new FileWriter(AUTH_FILE)) {
            GSON.toJson(authEmployees, w);
        } catch (IOException ignored) {}
    }


    private final User user = new User("John", "Doe");
    private final LocalDateTime currentDateTime = LocalDateTime.now();
    private final List<User> customers = List.of(
            new User("Marco"),
            new User("Winston","Duarte"),
            new User("Amos","Burton","'Timmy'")
    );

    public User getUser()                     { return user; }
    public LocalDateTime getCurrentDateTime() { return currentDateTime; }
    public List<User> getCustomers()          { return customers; }

    public static class User {
        private String firstName, lastName, middleName, email;
        private boolean emailConfirmed;
        public User(String fn)                { this(fn,null,null); }
        public User(String fn,String ln)      { this(fn,ln,null);  }
        public User(String fn,String ln,String mn){
            firstName=fn; lastName=ln; middleName=mn;
            email = fn+"@test.mail";
        }

        public String  getFirstName()              { return firstName; }
        public void    setFirstName(String v)      { firstName = v; }
        public String  getLastName()               {
            return lastName; }
        public void    setLastName(String v)       { lastName = v; }
        public String  getMiddleName()             { return middleName; }
        public void    setMiddleName(String v)     { middleName = v; }
        public String  getEmail()                  { return email; }
        public void    setEmail(String v)          { email = v; }
        public boolean isEmailConfirmed()          { return emailConfirmed; }
        public void    setEmailConfirmed(boolean v){ emailConfirmed = v; }
    }
}

