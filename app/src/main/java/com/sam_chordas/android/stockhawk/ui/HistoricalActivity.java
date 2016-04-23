package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.entities.HistoricalDeserializer;
import com.sam_chordas.android.stockhawk.entities.StockHistory;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by paulnunez on 4/20/16.
 */
public class HistoricalActivity extends AppCompatActivity {
    private static final String TAG = "HistoricalActivity";

    DetailStockView prevClose;
    DetailStockView high;
    DetailStockView volume;
    DetailStockView open;
    DetailStockView low;
    DetailStockView mktCap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        initalizeViews();

        String testUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22GOOGL%22%20and%20startDate%20%3D%20%222015-09-11%22%20and%20endDate%20%3D%20%222016-03-10%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(testUrl)
                .build();

        client.networkInterceptors().add(new StethoInterceptor());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                Reader json = response.body().charStream();

                Log.d(TAG, "onResponse: "+json);

                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(StockHistory.class, new HistoricalDeserializer())
                            .create();

                    StockHistory history = gson.fromJson(json, StockHistory.class);


                    ArrayList<StockHistory.Values> values =history.getHistory();

                    final StockHistory.Values todayValues = values.get(values.size()-1);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTodaysValues(todayValues);
                        }
                    });

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initalizeViews(){
        prevClose  = (DetailStockView) findViewById(R.id.prev_close);
        high       = (DetailStockView) findViewById(R.id.high);
        volume     = (DetailStockView) findViewById(R.id.volume);
        open       = (DetailStockView) findViewById(R.id.open);
        low        = (DetailStockView) findViewById(R.id.low);
        mktCap     = (DetailStockView) findViewById(R.id.mkt_cap);
    }

    private void setTodaysValues(StockHistory.Values todaysValues){

        prevClose.setValueText(String.format(Locale.US,"%.2f", todaysValues.getAdj_Close()));
        high.setValueText(String.format(Locale.US,"%.2f", todaysValues.getHigh()));
        volume.setValueText(String.format(Locale.US,"%.2f", todaysValues.getVolume()));
        open.setValueText(String.format(Locale.US,"%.2f", todaysValues.getOpen()));
        low.setValueText(String.format(Locale.US,"%.2f", todaysValues.getLow()));
        mktCap.setValueText(String.format(Locale.US,"%.2f", todaysValues.getClose()));
    }

}
