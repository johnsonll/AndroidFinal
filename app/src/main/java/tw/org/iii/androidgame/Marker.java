package tw.org.iii.androidgame;

/**
 * Created by jerry on 2017/4/15.
 */

public class Marker {
    public String supplystation_name;
    public String supplystation_longitude;
    public String supplystation_latitude;
    public int coin;

    public Marker(String supplystation_name, String supplystation_longitude, String supplystation_latitude, int coin) {
        this.supplystation_name = supplystation_name;
        this.supplystation_longitude = supplystation_longitude;
        this.supplystation_latitude = supplystation_latitude;
        this.coin = coin;
    }

    public String getSupplystation_name() {
        return supplystation_name;
    }

    public void setSupplystation_name(String supplystation_name) {
        this.supplystation_name = supplystation_name;
    }

    public String getSupplystation_longitude() {
        return supplystation_longitude;
    }

    public void setSupplystation_longitude(String supplystation_longitude) {
        this.supplystation_longitude = supplystation_longitude;
    }

    public String getSupplystation_latitude() {
        return supplystation_latitude;
    }

    public void setSupplystation_latitude(String supplystation_latitude) {
        this.supplystation_latitude = supplystation_latitude;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
