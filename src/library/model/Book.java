package library.model;

public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int copiesTotal;
    private int copiesAvailable;

    public Book(int id, String isbn, String title, String author, String category, int copiesTotal, int copiesAvailable) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesAvailable;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getCopiesTotal() { return copiesTotal; }
    public int getCopiesAvailable() { return copiesAvailable; }
}
