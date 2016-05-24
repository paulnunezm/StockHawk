package com.sam_chordas.android.stockhawk.ui;

import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.entities.HistoricalDeserializer;
import com.sam_chordas.android.stockhawk.entities.StockHistory;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by paulnunez on 4/20/16.
 */
public class
HistoricalActivity extends AppCompatActivity implements Callback,
  View.OnClickListener{
  private static final String TAG = "HistoricalActivity";

  private DetailStockView prevClose;
  private DetailStockView high;
  private DetailStockView volume;
  private DetailStockView open;
  private DetailStockView low;
  private DetailStockView mktCap;
  private LineChartView linechart;
  private TextView  mStockName;
  private TextView  mStockSymbol;
  private TextView  mOneDaySelector;
  private TextView  mSevenDaysSelector;
  private TextView  mOneMonthSelector;
  private TextView  mSixMonthSelector;
  private TextView  mOneYearSelector;

  private ArrayList<TextView> chartRange;
  private Tooltip mTip;
  private String stockName;
  private String stockSymbol;
  private String mUrl;
  private int mActiveRange;
  // Dates
  DateTime today;
  DateTime aWeekAgo;
  DateTime aMonthAgo;
  DateTime sixMonthsAgo;
  DateTime aYearAgo;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_line_graph);

    Intent intent = getIntent();

    today = new DateTime();
    aWeekAgo = today.minusDays(7);
    aMonthAgo = today.minusMonths(1);
    sixMonthsAgo = today.minusMonths(6);
    aYearAgo = today.minusYears(1);

    initalizeViews();
    setRangeOnClickListeners();

    if (intent != null) {
      Bundle extras = intent.getExtras();
      stockName = extras.getString(MyStocksActivity.INTENT_EXTRA_NAME);
      stockSymbol = extras.getString(MyStocksActivity.INTENT_EXTRA_SYMBOL).toUpperCase();

      mStockName.setText(stockName);
      mStockSymbol.setText(stockSymbol);

      mUrl = buildHistoricalUrlRequest(stockSymbol, today);

      OkHttpClient client = new OkHttpClient();
      Log.d(TAG, "onCreate: "+mUrl);
      final Request request = new Request.Builder()
          .url(mUrl)
          .build();

      client.networkInterceptors().add(new StethoInterceptor());
      client.newCall(request).enqueue(this);

    } else {
      //TODO: show error message


    }



    mActiveRange = 0;

//    String testUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22GOOGL%22%20and%20startDate%20%3D%20%222016-03-01%22%20and%20endDate%20%3D%20%222016-03-10%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

    if(savedInstanceState==null){

    }

  }


  private void initalizeViews() {
    mStockName = (TextView) findViewById(R.id.stock_name);
    mStockSymbol = (TextView) findViewById(R.id.stock_symbol);
    mOneDaySelector = (TextView) findViewById(R.id.one_day);
    mSevenDaysSelector = (TextView) findViewById(R.id.seven_days);
    mOneMonthSelector = (TextView) findViewById(R.id.one_moth);
    mSixMonthSelector = (TextView) findViewById(R.id.six_months);
    mOneYearSelector = (TextView) findViewById(R.id.one_year);
    prevClose = (DetailStockView) findViewById(R.id.prev_close);
    high = (DetailStockView) findViewById(R.id.high);
    volume = (DetailStockView) findViewById(R.id.volume);
    open = (DetailStockView) findViewById(R.id.open);
    low = (DetailStockView) findViewById(R.id.low);
    mktCap = (DetailStockView) findViewById(R.id.mkt_cap);
    linechart = (LineChartView) findViewById(R.id.chart);
    mTip = new Tooltip(this, R.layout.linechart_tooltip, R.id.value);

    chartRange = new ArrayList<>(5);
    chartRange.add(mOneDaySelector);
    chartRange.add(mSevenDaysSelector);
    chartRange.add(mOneMonthSelector);
    chartRange.add(mSixMonthSelector);
    chartRange.add(mOneYearSelector);
  }

  private void hideLoading() {

  }

  private void showErrorMessage() {
  }

  private void setRangeOnClickListeners(){
    mOneDaySelector.setOnClickListener(this);
    mSevenDaysSelector.setOnClickListener(this);
    mOneMonthSelector.setOnClickListener(this);
    mSixMonthSelector.setOnClickListener(this);
    mOneYearSelector.setOnClickListener(this);
  }

  private void changeRange(int selectedRange){
    TextView actualRange = chartRange.get(mActiveRange);
    actualRange.setBackgroundColor(getResources().getColor(R.color.transparent));
    actualRange.setTextColor(getResources().getColor(R.color.light_green));

    TextView selected = chartRange.get(selectedRange);
    selected.setBackgroundColor(getResources().getColor(R.color.light_green));
    selected.setTextColor(getResources().getColor(R.color.white));

    mActiveRange = selectedRange;
  }

  private String buildHistoricalUrlRequest(String symbol, DateTime startingDate) {

    String startDate = String.valueOf(today.getYear())+"-"+String.valueOf(today.getMonthOfYear())
        + "-" + String.valueOf(today.getDayOfMonth());

    String endDate = startDate;

    String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo." +
        "finance.historicaldata%20where%20symbol%20%3D%20%22" + symbol + "%22%20and%20" +
        "startDate%20%3D%20%22" + startDate + "%22%20and%20endDate%20%3D%20%22" + endDate + "%22&" +
        "format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org" +
        "%2Falltableswithkeys&callback=";

    url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%2220016-04-27%22%20and%20endDate%20%3D%20%222016-04-27%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
    url = URLEncoder.encode(url);
    return url;
  }

  private void setTodaysValues(StockHistory.Values todaysValues) {
    mStockName.setText(stockName);
    mStockSymbol.setText(stockSymbol);
    prevClose.setValueText(String.format(Locale.US, "%.2f", todaysValues.getAdj_Close()));
    high.setValueText(String.format(Locale.US, "%.2f", todaysValues.getHigh()));
    volume.setValueText(String.format(Locale.US, "%.2f", todaysValues.getVolume()));
    open.setValueText(String.format(Locale.US, "%.2f", todaysValues.getOpen()));
    low.setValueText(String.format(Locale.US, "%.2f", todaysValues.getLow()));
    mktCap.setValueText(String.format(Locale.US, "%.2f", todaysValues.getClose()));
  }

  private void setChartValues(ArrayList<StockHistory.Values> mValues) {

    ArrayList<Float> points = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();

    for (StockHistory.Values values : mValues) {
      points.add(values.getClose());
      labels.add(values.getDate());
    }

    float[] pointsArray = new float[points.size()];
    String[] labelsArray = new String[labels.size()];

    for (int i = 0; i < points.size(); i++) {
      float point = points.get(i);
      pointsArray[i] = point;
      labelsArray[i] = " ";
    }

    prepareTooltip();

    LineSet dataset = new LineSet(labelsArray, pointsArray);
    dataset.setColor(getResources().getColor(R.color.lighter_green))
        .setSmooth(true)
//                .setFill(getResources().getColor(R.color.alpha_green))
        .setDotsColor(getResources().getColor(R.color.light_blue))
        .setGradientFill(new int[]{Color.parseColor("#364d5a"), Color.parseColor("#3f7178")}, null)
        .setThickness(5)
        .setDotsRadius(15);

    linechart.addData(dataset);
    linechart.setXAxis(false);
    linechart.setYAxis(false);
    linechart.setYLabels(AxisController.LabelPosition.NONE);
    linechart.setXLabels(AxisController.LabelPosition.NONE);
    linechart.setTooltips(mTip);
    linechart.show();
  }

  private void prepareTooltip() {
    mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
    mTip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

      mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
          PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

      mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
          PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

      mTip.setPivotX(Tools.fromDpToPx(65) / 2);
      mTip.setPivotY(Tools.fromDpToPx(25));
    }
  }


  // okhttp callback implementation
  @Override
  public void onFailure(Request request, IOException e) {
    showErrorMessage();
  }

  @Override
  public void onResponse(Response response) throws IOException {
    String json = response.body().string();

    try {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(StockHistory.class, new HistoricalDeserializer())
          .create();

      StockHistory history = gson.fromJson(json, StockHistory.class);
      final ArrayList<StockHistory.Values> values = history.getHistory();
      final StockHistory.Values todayValues = values.get(values.size() - 1);

      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          setTodaysValues(todayValues);
          setChartValues(values);
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  // Views on click listener;
  @Override
  public void onClick(View v) {

    switch (v.getId()){
      case R.id.one_day:
        changeRange(0);
        break;
      case R.id.seven_days:
        changeRange(1);
        break;
      case R.id.one_moth:
        changeRange(2);
        break;
      case R.id.six_months:
        changeRange(3);
        break;
      case R.id.one_year:
        changeRange(4);
        break;
      case R.id.prev_close:
        changeRange(5);
        break;
    }

  }
}
