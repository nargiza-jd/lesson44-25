package kg.attractor.java.model;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String id;
    private String name;
    private List<String> currentBooks;
    private List<String> pastBooks;

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
        this.currentBooks = new ArrayList<>();
        this.pastBooks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getCurrentBooks() {
        return currentBooks;
    }

    public List<String> getPastBooks() {
        return pastBooks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentBooks(List<String> currentBooks) {
        this.currentBooks = currentBooks;
    }

    public void setPastBooks(List<String> pastBooks) {
        this.pastBooks = pastBooks;
    }

    public void addCurrentBook(String bookId) {
        currentBooks.add(bookId);
    }

    public void returnBook(String bookId) {
        currentBooks.remove(bookId);
        pastBooks.add(bookId);
    }
}