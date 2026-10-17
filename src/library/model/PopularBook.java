package library.model;

public class PopularBook {
    private String title;
    private String author;
    private int times;

    public PopularBook(String title, String author, int times) {
        this.title = title;
        this.author = author;
        this.times = times;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTimes() { return times; }
}
