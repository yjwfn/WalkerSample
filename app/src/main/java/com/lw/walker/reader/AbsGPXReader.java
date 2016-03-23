package com.lw.walker.reader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjwfn on 16-3-22.
 */
abstract class AbsGPXReader implements GPXReader {

    private List<GPXReaderListener> mListener;

    @Override
    public void addListener(GPXReaderListener listener) {

        if(listener == null)
            return;

        if(mListener == null)
            mListener = new ArrayList<>();

        if(!mListener.contains(listener))
            mListener.add(listener);
    }

    @Override
    public void removeListener(GPXReaderListener listener) {
        if(mListener != null)
            mListener.remove(listener);
    }




    protected void dispatchError(Throwable throwable){
        if(mListener != null && !mListener.isEmpty()){
            for(GPXReaderListener listener : mListener)
                listener.onError(throwable);
        }
    }

}


