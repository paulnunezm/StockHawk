package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.touch_helper.ItemTouchHelperAdapter;
import com.sam_chordas.android.stockhawk.touch_helper.ItemTouchHelperViewHolder;

/**
 * Created by sam_chordas on 10/6/15.
 *  Credit to skyfishjy gist:
 *    https://gist.github.com/skyfishjy/443b7448f59be978bc59
 * for the code structure
 */
public class QuoteCursorAdapter extends CursorRecyclerViewAdapter<QuoteCursorAdapter.ViewHolder>
    implements ItemTouchHelperAdapter{

  private static Context mContext;
  private static Typeface robotoLight;
  private static Typeface robotoRegular;
  private boolean isPercent;
  public QuoteCursorAdapter(Context context, Cursor cursor){
    super(context, cursor);
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    robotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
    robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_quote, parent, false);
    ViewHolder vh = new ViewHolder(itemView);
    return vh;
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor){


    String symbol = cursor.getString(cursor.getColumnIndex("symbol"));
    String name = cursor.getString(cursor.getColumnIndex("name"));
    String bid_price  = cursor.getString(cursor.getColumnIndex("bid_price"));
    String change = cursor.getString(cursor.getColumnIndex("change"));
    String percent_change = cursor.getString(cursor.getColumnIndex("percent_change"));

    viewHolder.symbol.setText(symbol);
    viewHolder.bidPrice.setText(bid_price);
    viewHolder.name.setText(name);

    Resources resources = mContext.getResources();
    int sdk = Build.VERSION.SDK_INT;

    if (cursor.getInt(cursor.getColumnIndex("is_up")) == 1){
      if (sdk < Build.VERSION_CODES.JELLY_BEAN){
        viewHolder.change.setBackgroundDrawable(
            resources.getDrawable(R.drawable.percent_change_pill_green));
      }else {
        viewHolder.change.setBackground(
            resources.getDrawable(R.drawable.percent_change_pill_green));
      }
    } else{
      if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
        viewHolder.change.setBackgroundDrawable(
            resources.getDrawable(R.drawable.percent_change_pill_red));
      } else{
        viewHolder.change.setBackground(
            resources.getDrawable(R.drawable.percent_change_pill_red));
      }
    }
    if (Utils.showPercent){
      viewHolder.change.setText(percent_change);
    } else{
      viewHolder.change.setText(change);
    }

    // Accessibility
    viewHolder.symbol.setContentDescription(String.format(resources.getString(R.string.a11y_stock_symbol), symbol));
    viewHolder.name.setContentDescription(String.format(resources.getString(R.string.a11y_stock_name), name));
    viewHolder.bidPrice.setContentDescription(String.format(resources.getString(R.string.a11y_stock_bidprice), symbol));
    viewHolder.change.setContentDescription(String.format(resources.getString(R.string.a11y_stock_change), change));
  }

  @Override public void onItemDismiss(int position) {
    Cursor c = getCursor();
    c.moveToPosition(position);
    String symbol = c.getString(c.getColumnIndex(QuoteColumns.SYMBOL));
    mContext.getContentResolver().delete(QuoteProvider.Quotes.withSymbol(symbol), null, null);
    notifyItemRemoved(position);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
      implements ItemTouchHelperViewHolder, View.OnClickListener{
    public final TextView symbol;
    public final TextView name;
    public final TextView bidPrice;
    public final TextView change;
    public ViewHolder(View itemView){
      super(itemView);
      symbol = (TextView) itemView.findViewById(R.id.stock_symbol);
      symbol.setTypeface(robotoLight);
      name = (TextView) itemView.findViewById(R.id.stock_name);
      name.setTypeface(robotoLight);
      bidPrice = (TextView) itemView.findViewById(R.id.bid_price);
      bidPrice.setTypeface(robotoLight);
      change = (TextView) itemView.findViewById(R.id.change);
      change.setTypeface(robotoRegular);
    }

    @Override
    public void onItemSelected(){
      itemView.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
    }

    @Override
    public void onItemClear(){
      itemView.setBackgroundColor(0);
    }

    @Override
    public void onClick(View v) {

    }
  }
}
