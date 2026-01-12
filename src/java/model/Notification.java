package model;

public class Notification {
    private int id;
    private int member_id;
    private String type;          // 'MEETING' hoặc 'VOTE'
    private int reference_id;     // ID của cuộc họp hoặc ID câu hỏi biểu quyết
    private String message;
    private String status;
    private String created_at;

    public Notification() {}

    public Notification(int id, int member_id, String type, int reference_id, String message, String status, String created_at) {
        this.id = id;
        this.member_id = member_id;
        this.type = type;
        this.reference_id = reference_id;
        this.message = message;
        this.status = status;
        this.created_at = created_at;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMember_id() { return member_id; }
    public void setMember_id(int member_id) { this.member_id = member_id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getReference_id() { return reference_id; }
    public void setReference_id(int reference_id) { this.reference_id = reference_id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
}