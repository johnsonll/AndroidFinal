package tw.org.iii.androidgame;

import java.util.ArrayList;

/**
 * Created by iii on 2017/4/12.
 */

public class Style {

    public Style(int member_id, int item_id) {
        this.member_id = member_id;
        this.item_id = item_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    private int member_id;
    private int item_id;
}
