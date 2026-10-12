package library.model;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private int copiesAvailable;

    public Book(int id, String isbn, String title, String author, int copiesAvailable) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.copiesAvailable = copiesAvailable;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getCopiesAvailable() { return copiesAvailable; }

    @Override
    public String toString() {
        return id + ". " + title + " by " + author + " (" + copiesAvailable + " left)";
    }
}
