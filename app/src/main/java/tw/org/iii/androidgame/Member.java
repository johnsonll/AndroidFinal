package tw.org.iii.androidgame;

/**
 * Created by iii on 2017/4/17.
 */

public class Member {

    public Member(int member_id, String email, String password, int money, String name, String tel) {

        this.member_id = member_id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.name = name;
        this.tel = tel;
    }

    public Member() {

    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    int member_id;
    String email;
    String password;
    int money;
    String name;
    String tel;

}
