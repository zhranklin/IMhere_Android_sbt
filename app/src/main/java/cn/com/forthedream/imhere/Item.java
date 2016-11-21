package cn.com.forthedream.imhere;

import java.util.Date;

/**
 * Created by 晗涛 on 2016/10/5.
 */
public class Item {
    private String Type;
    private String Title;
    private Date Date;
    private String Maintain;
    private String Location;
    private  boolean Tuisong;

    public Item(String type, String title, Date date, String maintain, String location, boolean tuisong) {
        Type = type;
        Title = title;
        this.Date = date;
        Maintain = maintain;
        this.Location = location;
        this.Tuisong = tuisong;
    }

    public String getType() {
        return Type;
    }

    public String getTitle() {
        return Title;
    }

    public Date getDate() {
        return Date;
    }

    public String getMaintain() {
        return Maintain;
    }

    public boolean isTuisong() {
        return Tuisong;
    }

    public String getLocation() {
        return Location;
    }
}
