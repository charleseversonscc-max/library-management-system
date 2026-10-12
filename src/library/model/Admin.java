package library.model;

public class Admin extends User {
    public Admin(int id, String username) { super(id, username); }
    public String getRole() { return "ADMIN"; }
    public String getHomePage() { return "/pages/admin.html"; }
}
