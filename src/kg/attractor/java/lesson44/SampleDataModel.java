package kg.attractor.java.lesson44;

import kg.attractor.java.model.Book;
import kg.attractor.java.model.BookStatus;
import kg.attractor.java.model.Employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;



public class SampleDataModel {
    private User user = new User("John", "Doe");
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private List<User> customers = new ArrayList<>();


    private static final List<Book> books = loadBooksFromJson();
    private static final List<Employee> employees = loadEmployeesFromJson();

    public static List<Employee> getEmployees() {
        return employees;
    }


    public static List<Employee> loadEmployeesFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("data/json/employees.json")) {
            Type empListType = new TypeToken<List<Employee>>() {}.getType();
            return gson.fromJson(reader, empListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public SampleDataModel() {
        customers.add(new User("Marco"));
        customers.add(new User("Winston", "Duarte"));
        customers.add(new User("Amos", "Burton", "'Timmy'"));
        customers.get(1).setEmailConfirmed(true);
    }

    public static Employee getEmployeeById(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return employee;
            }
        }
        return null;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public List<User> getCustomers() {
        return customers;
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }

    public static List<Book> getBooks() {
        return books;
    }

    public static Book getBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public static List<Book> loadBooksFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("data/json/books.json")) {
            Type bookListType = new TypeToken<List<Book>>() {}.getType();
            return gson.fromJson(reader, bookListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static class User {
        private String firstName;
        private String lastName;
        private String middleName = null;
        private boolean emailConfirmed = false;
        private String email;

        public User(String firstName) {
            this(firstName, null, null);
        }

        public User(String firstName, String lastName) {
            this(firstName, lastName, null);
        }

        public User(String firstName, String lastName, String middleName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.middleName = middleName;
            this.email = firstName + "@test.mail";
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public boolean isEmailConfirmed() {
            return emailConfirmed;
        }

        public void setEmailConfirmed(boolean emailConfirmed) {
            this.emailConfirmed = emailConfirmed;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }




}