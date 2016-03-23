package com.lw.walker.reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.annotation.WorkerThread;
import android.util.Xml;

import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKInfo;
import com.lw.walker.bean.TRKItem;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by yjwfn on 16-3-22.
 */
@WorkerThread
public  class XMLPullReader extends AbsGPXReader {

    private XmlPullParser mParser;

    private Context mAppContext;

    private String  mCharset;

    private Uri  mUri;


    public XMLPullReader(Context mAppContext) {
        this(mAppContext, "utf-8");
    }

    public XMLPullReader(Context mAppContext, String mCharset) {
        this.mAppContext = mAppContext;
        this.mCharset = mCharset;
    }

    @Override
    public void setSource(Uri gpxUri) {
        this.mUri = gpxUri;
    }

    private void initParser() throws XmlPullParserException, IOException {
            if(mParser == null || mParser.getEventType() == XmlPullParser.END_DOCUMENT) {

                String scheme = mUri.getScheme();
                if (scheme.equals("asset")) {
                    String path = mUri.getPath();
                    path = path.substring(1, path.length());
                    AssetManager am = mAppContext.getAssets();

                    mParser = Xml.newPullParser();
                    mParser.setInput(am.open(path), mCharset);
                }
            }

    }

    public GPXInfo readGPXInfo(){
        GPXInfo gpxInfo = null;
        try {
            initParser();
            int eventType = mParser.getEventType();
            String tag;
            while (eventType != XmlPullParser.END_DOCUMENT) {


                if (eventType == XmlPullParser.START_TAG) {
                    tag = mParser.getName();
                    if (tag.equals("gpx")) {
                        gpxInfo = new GPXInfo();
                    } else if (tag.equals("name")) {
                        gpxInfo.name = mParser.nextText();
                    } else if (tag.equals("desc")) {
                        gpxInfo.desc = mParser.nextText();
                    } else if (tag.equals("author")) {
                        gpxInfo.author = mParser.nextText();
                    } else if (tag.equals("url")) {
                        gpxInfo.url = mParser.nextText();
                    } else if (tag.equals("distance")) {
                        gpxInfo.distance = mParser.nextText();
                    }else if(tag.equals("urlname")) {
                        gpxInfo.urlName = mParser.nextText();
                    }else if(tag.equals("keywords")) {
                        gpxInfo.keywords = mParser.nextText();
                    }else if(tag.equals("time")) {
                        gpxInfo.time = mParser.nextText();
                    }else if (tag.equals("trk")) {
                        //gpxInfo.trkInfo = mParser(parser);
                        break;
                    }
                 }

                eventType = mParser.next();
            }

        }catch (XmlPullParserException e){
            dispatchError(e);
        } catch (IOException e) {
            dispatchError(e);
        }

        return gpxInfo;
    }

    @Override
    public void reset() {
        if(mParser != null){
            try {
                mParser.setInput(null);
                mParser = null;
            } catch (XmlPullParserException e) {
                dispatchError(e);
            }
        }
    }

    @Override
    public void skipTo(String key){

        try {
            initParser();
            if(mParser != null){
                int eventType = mParser.getEventType();

                if(eventType == XmlPullParser.START_TAG){
                    if(mParser.getName().equals(key))
                        return;
                }


                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG){
                        if(key.equals(mParser.getName())){
                            return;
                        }
                    }
                    eventType = mParser.next();
                }

            }

        } catch (XmlPullParserException e) {
            dispatchError(e);
        } catch (IOException e) {
            dispatchError(e);
        }


    }


    public TRKInfo readTRKInfo(){

        TRKInfo trkInfo = null;
        try {
            initParser();
            int eventType = mParser.getEventType();
            String tag;
            while (eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_TAG){
                    tag = mParser.getName();
                    if(tag.equals("trk")) {
                        trkInfo = new TRKInfo();
                    }else if(tag.equals("name")){
                        trkInfo.name = mParser.nextText();
                    }else if(tag.equals("desc")){
                        trkInfo.desc = mParser.nextText();
                    }else if(tag.equals("trkseg")){
                         break;
                    }
                }

                eventType = mParser.next();
            }
        }catch (XmlPullParserException e){
            dispatchError(e);
        }catch (IOException io){
            dispatchError(io);
        }

        return trkInfo;
    }


    public TRKItem   readTRKItem(){
        TRKItem item = null;
        try{
            initParser();
            int eventType = mParser.getEventType();
            String tag;
            while (eventType != XmlPullParser.END_DOCUMENT){

                if(eventType == XmlPullParser.START_TAG){
                    tag = mParser.getName();
                    if(tag.equals("trkpt")){
                        item = new TRKItem();
                        item.latitude = mParser.getAttributeValue(null, "lat");
                        item.longitude = mParser.getAttributeValue(null, "lon");

                    }else if(tag.equals("time") && item != null){
                        item.time = mParser.nextText();
                    }
                }else if(eventType == XmlPullParser.END_TAG){
                    tag = mParser.getName();
                    if(tag.equals("trkpt")){
                        mParser.next();
                        break;
                    }

                }

                eventType = mParser.next();
            }
        }catch (IOException io){
            dispatchError(io);

        }catch (XmlPullParserException e){
            dispatchError(e);
        }

        return item;
    }


    @Override
    public void close() throws IOException {
        if(mParser != null){
            try {
                mParser.setInput(null);
            } catch (XmlPullParserException e) {
                dispatchError(e);
            }
        }
    }
}
