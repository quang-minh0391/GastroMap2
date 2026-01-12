package model;

public class VotingResult {
    private int id;
    private int member_id;
    private int option_id;
    private String voted_at;

    public VotingResult() {}

    public VotingResult(int id, int member_id, int option_id, String voted_at) {
        this.id = id;
        this.member_id = member_id;
        this.option_id = option_id;
        this.voted_at = voted_at;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMember_id() { return member_id; }
    public void setMember_id(int member_id) { this.member_id = member_id; }
    public int getOption_id() { return option_id; }
    public void setOption_id(int option_id) { this.option_id = option_id; }
    public String getVoted_at() { return voted_at; }
    public void setVoted_at(String voted_at) { this.voted_at = voted_at; }
}