package com.lw.walker.bean;

/**
 * Created by yjwfn on 16-3-22.
 */
public class TRKItem {

    public String latitude;
    public String longitude;
    public String   time;


    @Override
    public String toString() {
        return "TRKItem{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
