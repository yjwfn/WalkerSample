package com.lw.walker;

import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKInfo;
import com.lw.walker.bean.TRKItem;
import com.lw.walker.reader.GPXReader;
import com.lw.walker.reader.GPXReaderListener;
import com.lw.walker.reader.XMLPullReader;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Created by yjwfn on 16-3-22.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestGPXReader implements GPXReaderListener{


    private static final String TAG = "GPXReader";

    @Test
    public void read() throws InterruptedException, IOException {
        GPXReader reader = new XMLPullReader(InstrumentationRegistry.getContext(), "utf-8");
        reader.addListener(this);
        Uri file = Uri.parse("asset:///demo.gpx");
        reader.setSource(file);

        GPXInfo gpxInfo = reader.readGPXInfo();
        TRKInfo trkInfo = reader.readTRKInfo();


        TRKItem item;
        while ( (item = reader.readTRKItem()) != null){
            Log.d(TAG, item.toString());
        }

        Log.d(TAG, gpxInfo.toString());

        Assert.assertNotNull(gpxInfo);
        Assert.assertNotNull(trkInfo);

        Log.d(TAG, gpxInfo.toString());
        Log.d(TAG, trkInfo.toString());


        reader.close();
        reader.removeListener(this);
    }



    @Override
    public void onError(Throwable msg) {
        throw new RuntimeException(msg);
    }
}
