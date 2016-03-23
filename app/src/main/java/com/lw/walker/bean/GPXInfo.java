package com.lw.walker.bean;

/**
 * Created by yjwfn on 16-3-22.
 */
public class GPXInfo {
    public String   name;
    public String   desc;
    public String   url;
    public String   urlName;
    public String   author;
    public String   distance;
    public String   keywords;
    public String   time;
    public TRKInfo  trkInfo;


    @Override
    public String toString() {
        return "GPXInfo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", urlName='" + urlName + '\'' +
                ", author='" + author + '\'' +
                ", distance='" + distance + '\'' +
                ", keywords='" + keywords + '\'' +
                ", time='" + time + '\'' +
                ", trkInfo=" + trkInfo +
                '}';
    }
}
