package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.entities.Quote;

import java.util.ArrayList;

/**
 * Gets the data to populate the views.
 * The remove views factory acts like the adapter for the remote views.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory{
  private static final String TAG = "WidgetDataProvider";

  private Context context;
  private Intent intent;
  private Cursor quotesCursor;
  private ArrayList<Quote> quotes = new ArrayList<>();

  public WidgetDataProvider(Context context, Intent intent) {
    this.context = context;
    this.intent = intent;
  }

  public void initData(){
    quotes.clear();

    quotesCursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
        new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.NAME,
            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
        QuoteColumns.ISCURRENT + " = ?",
        new String[]{"1"},
        null);


    while (quotesCursor.moveToNext()){
      Quote quote = new Quote(
          quotesCursor.getString(quotesCursor.getColumnIndex(QuoteColumns.SYMBOL)),
          quotesCursor.getString(quotesCursor.getColumnIndex(QuoteColumns.NAME)),
          quotesCursor.getString(quotesCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)),
          quotesCursor.getString(quotesCursor.getColumnIndex(QuoteColumns.ISUP))
      );
      quotes.add(quote);
    }

//    for (int i = 0; i < 5; i++) {
//      Quote quote = new Quote("GOOG","google","40", "100");
//      quotes.add(quote);
//    }


//    quotesCursor.close();

  }

  @Override
  public void onCreate() {

    Log.d(TAG, "onCreate: ");

    /** This is where you set up any connections and/or cursors to your data source. */
    initData();
  }

  @Override
  public void onDataSetChanged() {

    /** https://groups.google.com/forum/#!topic/android-developers/yj4xEkZDWhQ  */
    // Revert back to our process' identity so we can work with our
    // content provider
    final long identityToken = Binder.clearCallingIdentity();

    // Update your cursor or whaterver here
    initData();
    // Restore the identity - not sure if it's needed since we're going
    // to return right here, but it just *seems* cleaner
    Binder.restoreCallingIdentity(identityToken);

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public int getCount() {
    return quotes.size();
  }

  @Override
  public RemoteViews getViewAt(int position) {

    Log.d(TAG, "getViewAt: ");

    // Construct a remote views item based on the app widget item XML file,
    // and set the text based on the position.

    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item_quote);

    String symbol = quotes.get(position).getSymbol();
    String name = quotes.get(position).getName();
    String change = quotes.get(position).getChange();

    Resources resources = context.getResources();

    rv.setTextViewText(R.id.stock_symbol, symbol);
    rv.setTextViewText(R.id.stock_name, name);
    rv.setTextViewText(R.id.change, change);

    //Accessibility
    rv.setContentDescription(R.id.stock_symbol, String.format(resources.getString(R.string.a11y_stock_name), symbol));
    rv.setContentDescription(R.id.stock_name, String.format(resources.getString(R.string.a11y_stock_name), name));
    rv.setContentDescription(R.id.change, String.format(resources.getString(R.string.a11y_stock_change), change));

    if (quotes.get(position).getIsUp().equals("1")){
      rv.setTextColor(R.id.change, resources.getColor(R.color.light_green));
    } else{
      rv.setTextColor(R.id.change, resources.getColor(R.color.wine_red));
    }

    // Return the remote views object.
    return rv;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
