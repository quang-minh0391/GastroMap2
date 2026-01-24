package model;

public class VotingOption {
    private int id;
    private int question_id;
    private int member_id; 
    private String option_text;
    private int vote_count; // THÊM DÒNG NÀY để lưu số phiếu

    public VotingOption() {}

    // Constructor cập nhật thêm tham số vote_count
    public VotingOption(int id, int question_id, int member_id, String option_text, int vote_count) {
        this.id = id;
        this.question_id = question_id;
        this.member_id = member_id;
        this.option_text = option_text;
        this.vote_count = vote_count;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuestion_id() { return question_id; }
    public void setQuestion_id(int question_id) { this.question_id = question_id; }

    public int getMember_id() { return member_id; }
    public void setMember_id(int member_id) { this.member_id = member_id; }

    public String getOption_text() { return option_text; }
    public void setOption_text(String option_text) { this.option_text = option_text; }

    // PHẢI CÓ HÀM NÀY ĐỂ JSP KHÔNG BỊ LỖI
    public int getVote_count() { return vote_count; }
    public void setVote_count(int vote_count) { this.vote_count = vote_count; }
}