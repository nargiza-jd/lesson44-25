package kg.attractor.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeAuth {
    private String id;
    private String email;
    private String password;
    private String fullName;

    private List<String> issuedBookIds;
    private List<String> historyBookIds;

    public EmployeeAuth(String id, String email, String password, String fullName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;

        this.issuedBookIds = new ArrayList<>();
        this.historyBookIds = new ArrayList<>();
    }

    public EmployeeAuth() {
        this.issuedBookIds = new ArrayList<>();
        this.historyBookIds = new ArrayList<>();
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
        if (issuedBookIds == null) {
            issuedBookIds = new ArrayList<>();
        }
        return issuedBookIds;
    }

    public List<String> getHistoryBookIds() {
        if (historyBookIds == null) {
            historyBookIds = new ArrayList<>();
        }
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

    public void setIssuedBookIds(List<String> issuedBookIds) {
        this.issuedBookIds = issuedBookIds;
    }

    public void setHistoryBookIds(List<String> historyBookIds) {
        this.historyBookIds = historyBookIds;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAuth that = (EmployeeAuth) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EmployeeAuth{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", issuedBookIds=" + issuedBookIds +
                ", historyBookIds=" + historyBookIds +
                '}';
    }
}