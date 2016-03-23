package com.lw.walker.bean;

import java.util.List;

/**
 * Created by yjwfn on 16-3-22.
 */
public class TRKInfo {

    public String   name;
    public String   desc;
    public List<TRKItem>   trkseq;


    @Override
    public String toString() {
        return "TRKInfo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", trkseq=" + trkseq +
                '}';
    }
}
