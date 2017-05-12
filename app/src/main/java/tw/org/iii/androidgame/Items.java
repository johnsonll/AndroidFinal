package tw.org.iii.androidgame;

/**
 * Created by iii on 2017/4/19.
 */

public class Items {

    public Items(int item_id,String itemUrl,int money_price){
        this.item_id = item_id;
        this.itemUrl = itemUrl;
        this.money_price = money_price;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public int getMoney_price() {
        return money_price;
    }

    public void setMoney_price(int money_price) {
        this.money_price = money_price;
    }

    private int item_id;
    private String itemUrl;
    private int money_price;
}
