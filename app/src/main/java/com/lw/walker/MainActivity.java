package com.lw.walker;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.lw.walker.bean.GPXInfo;
import com.lw.walker.bean.TRKItem;
import com.lw.walker.reader.GPXReader;
import com.lw.walker.reader.GPXReaderListener;
import com.lw.walker.reader.XMLPullReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GPXReaderListener{

    private static final String TAG = "MainActivity";

    private MapView mMapView;

    private Dialog  mWaitingDialog;

    private ParserThread  mParserThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapView = (MapView) findViewById(R.id.map_view);
        setRouteOverlay();
    }

    @Override
    public void onError(final Throwable msg) {
        if(msg != null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, msg.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            msg.printStackTrace();
        }


    }

    private void setRouteOverlay(){


        mWaitingDialog = ProgressDialog.show(this, "" , "正在解析...");
        mWaitingDialog.setCanceledOnTouchOutside(false);
        mWaitingDialog.setCancelable(true);
        mWaitingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(mParserThread != null)
                    mParserThread.interrupt();
            }
        });

        Uri file = Uri.parse("asset:///demo.gpx");
        GPXReader   reader = new XMLPullReader(getApplicationContext(), "utf-8");
        reader.addListener(this);
        reader.setSource(file);

        mParserThread = new ParserThread();
        mParserThread.reader = reader;
        mParserThread.start();
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

        if(mParserThread != null){
            mParserThread.interrupt();
            mParserThread = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    class ParserThread extends Thread{

        GPXReader reader;

        @Override
        public void run() {

            GPXInfo gpxInfo = reader.readGPXInfo();
            gpxInfo.trkInfo = reader.readTRKInfo();

            TRKItem item = reader.readTRKItem();

            //定位到开始位置
            if(item != null){

                LatLng latLng = new LatLng(Double.parseDouble(item.latitude), Double.parseDouble(item.longitude));
                BaiduMap baiduMap = mMapView.getMap();
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 5);//设置缩放比例
                baiduMap.animateMapStatus(u);

                List<LatLng> points = new ArrayList<>();
                PolylineOptions options = new PolylineOptions()
                        .color(Color.RED);

                LatLng tmp;
                while (item != null && !isInterrupted()){
                    tmp = new LatLng(Double.parseDouble(item.latitude)
                            ,Double.parseDouble(item.longitude));
                    points.add(tmp);
                    item = reader.readTRKItem();
                }

                if(points.size() > 2) {
                    options.points(points);
                    mMapView.getMap().addOverlay(options);
                }

            }


            mWaitingDialog.dismiss();
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            reader = null;
        }
    }
}


