package model;

import java.util.List; // QUAN TRỌNG: Cần thêm import này

public class VotingQuestion {
    private int id;
    private int meeting_id;
    private int member_id;
    private String question_text;
    
    // TẬN DỤNG: Danh sách các phương án cho câu hỏi này
    private List<VotingOption> options; 

    public VotingQuestion() {}

    public VotingQuestion(int id, int meeting_id, int member_id, String question_text) {
        this.id = id;
        this.meeting_id = meeting_id;
        this.member_id = member_id;
        this.question_text = question_text;
    }

    // Getters và Setters cho các thuộc tính cũ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMeeting_id() { return meeting_id; }
    public void setMeeting_id(int meeting_id) { this.meeting_id = meeting_id; }
    public int getMember_id() { return member_id; }
    public void setMember_id(int member_id) { this.member_id = member_id; }
    public String getQuestion_text() { return question_text; }
    public void setQuestion_text(String question_text) { this.question_text = question_text; }

    // THÊM Getter và Setter cho danh sách Options
    public List<VotingOption> getOptions() { return options; }
    public void setOptions(List<VotingOption> options) { this.options = options; }
}