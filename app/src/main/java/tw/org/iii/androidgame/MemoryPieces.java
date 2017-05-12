package tw.org.iii.androidgame;

/**
 * Created by minmin on 2017/4/25.
 */

public class MemoryPieces {

    public MemoryPieces(String member_id, String timeshard_num) {
        this.member_id = member_id;
        this.timeshard_num = timeshard_num;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getTimeshard_num() {
        return timeshard_num;
    }

    public void setTimeshard_num(String timeshard_num) {
        this.timeshard_num = timeshard_num;
    }

    private String member_id = "";
    private String timeshard_num = "";
}
