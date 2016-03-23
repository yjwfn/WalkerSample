package com.lw.walker.reader;

import android.net.Uri;

import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKInfo;
import com.lw.walker.bean.TRKItem;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yjwfn on 16-3-23.
 */
public class RxReader {

    private GPXReader   mReader;

    public RxReader(GPXReader mReader) {
        this.mReader = mReader;
    }

    public void setSource(Uri gpxUri) {
        mReader.setSource(gpxUri);
    }


    public Observable<GPXInfo> readGPXInfo() {
        return Observable.create(new Observable.OnSubscribe<GPXInfo>() {
            @Override
            public void call(Subscriber<? super GPXInfo> subscriber) {
                GPXInfo info = mReader.readGPXInfo();
                subscriber.onNext(info);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
                ;
    }

    public Observable<TRKInfo> readTRKInfo() {
        return Observable.create(new Observable.OnSubscribe<TRKInfo>() {
            @Override
            public void call(Subscriber<? super TRKInfo> subscriber) {
                mReader.skipTo("trk");
                TRKInfo info = mReader.readTRKInfo();
                subscriber.onNext(info);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
                ;
    }

    public Observable<TRKItem> readTRKItem() {
        return Observable.create(new Observable.OnSubscribe<TRKItem>() {
            @Override
            public void call(Subscriber<? super TRKItem> subscriber) {
                mReader.skipTo("trkseg");
                TRKItem item = mReader.readTRKItem();
                while (item != null && !subscriber.isUnsubscribed()){
                    subscriber.onNext(item);
                    item = mReader.readTRKItem();
                }

                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
                ;
    }

    public void addListener(GPXReaderListener listener) {
        mReader.addListener(listener);
    }

    public void removeListener(GPXReaderListener listener) {
        mReader.addListener(listener);
    }

    public void close() throws IOException {
        mReader.close();
    }

}
