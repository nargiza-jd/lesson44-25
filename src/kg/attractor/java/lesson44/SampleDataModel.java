package kg.attractor.java.lesson44;

import kg.attractor.java.model.Book;
import kg.attractor.java.model.BookStatus;
import kg.attractor.java.model.Employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleDataModel {
    private User user = new User("John", "Doe");
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private List<User> customers = new ArrayList<>();

    private static final List<Book> books = new ArrayList<>();

    private static final List<Employee> employees = new ArrayList<>();

    static {
        employees.add(new Employee("1", "John", "Doe", List.of("2")));
        employees.add(new Employee("2", "Anna", "Smith", List.of("4")));
        employees.add(new Employee("3", "Tom", "Brown", List.of()));
    }

    static {
        books.add(new Book("1", "Clean Code", "Robert Martin", "clean_code.jpg", BookStatus.AVAILABLE, null));
        books.add(new Book("2", "Effective Java", "Joshua Bloch", "effective_java.jpg", BookStatus.ISSUED, "1"));
        books.add(new Book("3", "Java Concurrency in Practice", "Brian Goetz", "concurrency.jpg", BookStatus.AVAILABLE, null));
        books.add(new Book("4", "Head First Java", "Kathy Sierra", "head_first.jpg", BookStatus.ISSUED, "2"));
        books.add(new Book("5", "Spring in Action", "Craig Walls", "spring_in_action.jpg", BookStatus.AVAILABLE, null));
        books.add(new Book("6", "Java: The Complete Reference", "Herbert Schildt", "reference.jpg", BookStatus.AVAILABLE, null));
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