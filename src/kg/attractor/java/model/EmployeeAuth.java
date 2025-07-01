package kg.attractor.java.model;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAuth {
    private String id;
    private String email;
    private String password;
    private String fullName;

    private final List<String> issuedBookIds = new ArrayList<>();
    private final List<String> historyBookIds = new ArrayList<>();

    public EmployeeAuth(String id, String email, String password, String fullName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getIssuedBookIds() {
        return issuedBookIds;
    }

    public List<String> getHistoryBookIds() {
        return historyBookIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}