package library.model;

public class Member extends User {
    public Member(int id, String username) { super(id, username); }
    public String getRole() { return "MEMBER"; }
    public String getHomePage() { return "/member/dashboard"; }
}
