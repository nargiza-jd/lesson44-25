package kg.attractor.java.model;

import java.util.List;

public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private List<String> issuedBookIds;

    public Employee(String id, String firstName, String lastName, List<String> issuedBookIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.issuedBookIds = issuedBookIds;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getIssuedBookIds() {
        return issuedBookIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIssuedBookIds(List<String> issuedBookIds) {
        this.issuedBookIds = issuedBookIds;
    }
}