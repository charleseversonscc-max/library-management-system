package library.model;

public class MemberInfo {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String joined;

    public MemberInfo(int id, String username, String fullName, String email, String phone, String joined) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.joined = joined;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getJoined() { return joined; }
}
