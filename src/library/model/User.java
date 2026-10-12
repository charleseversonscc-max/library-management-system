package library.model;

public abstract class User {
    private int id;
    private String username;

    protected User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }

    public abstract String getRole();
    public abstract String getHomePage();
}
