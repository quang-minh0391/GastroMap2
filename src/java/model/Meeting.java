package model;

public class Meeting {
    private int id;
    private String title;
    private String description;
    private String meeting_date;
    private String location;
    private int member_id; // ID của người tạo cuộc họp

    public Meeting() {}

    public Meeting(int id, String title, String description, String meeting_date, String location, int member_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.meeting_date = meeting_date;
        this.location = location;
        this.member_id = member_id;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMeeting_date() { return meeting_date; }
    public void setMeeting_date(String meeting_date) { this.meeting_date = meeting_date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getMember_id() { return member_id; }
    public void setMember_id(int member_id) { this.member_id = member_id; }
}