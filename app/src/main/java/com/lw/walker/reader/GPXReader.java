package com.lw.walker.reader;

import android.net.Uri;

import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKInfo;
import com.lw.walker.bean.TRKItem;

import java.io.Closeable;

/**
 * Created by yjwfn on 16-3-22.
 */
public interface GPXReader extends Closeable{

    /**
     * 设置GPX文件
     * @param gpxUri
     */
    void  setSource(Uri gpxUri);

    /**
     * 读取GPX文件信息
     * @return
     */
    GPXInfo readGPXInfo();

    /**
     * 读取trk信息
     * @return
     */
    TRKInfo readTRKInfo();

    void skipTo(String key);

    void reset();

    /**
     * 读取trk item
     * @return
     *  返回null读取结束
     */
    TRKItem readTRKItem();

    void    addListener(GPXReaderListener listener);

    void    removeListener(GPXReaderListener listener);
}
