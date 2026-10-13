package library.model;

public class LoanInfo {
    private int id;
    private String memberName;
    private String title;
    private String borrowDate;
    private String dueDate;
    private int overdueDays;

    public LoanInfo(int id, String memberName, String title, String borrowDate, String dueDate, int overdueDays) {
        this.id = id;
        this.memberName = memberName;
        this.title = title;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.overdueDays = overdueDays;
    }

    public int getId() { return id; }
    public String getMemberName() { return memberName; }
    public String getTitle() { return title; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public int getOverdueDays() { return overdueDays; }
}
