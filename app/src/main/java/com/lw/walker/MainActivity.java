package com.lw.walker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKInfo;
import com.lw.walker.bean.TRKItem;
import com.lw.walker.reader.GPXReader;
import com.lw.walker.reader.RxReader;
import com.lw.walker.reader.XMLPullReader;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MapView mMapView;



    private RxReader    mRxReader;

    private Subscription    mTmpSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initReader();
        mMapView = (MapView) findViewById(R.id.map_view);
        addRouteOverlay();
    }


    private void addRouteOverlay(){



    }


    private void initReader(){
        Uri file = Uri.parse("asset:///demo.gpx");

        GPXReader   reader = new XMLPullReader(getApplicationContext(), "utf-8");
        mRxReader = new RxReader(reader);
        mRxReader.setSource(file);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if(mTmpSubscription != null){
            mTmpSubscription.unsubscribe();
            mTmpSubscription = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
}
