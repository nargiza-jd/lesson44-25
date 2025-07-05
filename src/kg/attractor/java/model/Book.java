package kg.attractor.java.model;

import kg.attractor.java.lesson44.SampleDataModel;

public class Book {
    private String id;
    private String title;
    private String author;
    private String image;
    private String description;
    private BookStatus status;
    private String issuedTo;
    private String holderEmail;

    public Book(String id, String title, String author, String image, String description, BookStatus status, String issuedTo) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.description = description;
        this.status = status;
        this.issuedTo = issuedTo;
    }

    public String getHolderEmail() {
        return holderEmail;
    }

    public void setHolderEmail(String holderEmail) {
        this.holderEmail = holderEmail;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public BookStatus getStatus() {
        return status;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(BookStatus status) {
        this.status = status;

    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", issuedTo='" + issuedTo + '\'' +
                '}';
    }
}