package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {
  private static final String TAG = "StockWidget";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                              int appWidgetId) {

    CharSequence widgetText = context.getString(R.string.appwidget_text);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
//    views.setTextViewText(R.id.appwidget_text, widgetText);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    Log.i(TAG, "onUpdate");

    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {

      // Set up the intent that starts the StackViewService, which will
      // provide the views for this collection.
      Intent intent = new Intent(context, WidgetService.class);

      // Add the app widget ID to the intent extras.
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//      updateAppWidget(context, appWidgetManager, appWidgetId);

      // Instantiate the RemoteViews object to use a RemoteViews  adapter.
      RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

      // This adapter connects
//      rv.setRemoteAdapter(appWidgetId, R.id.widget_list_view, intent);
//      rv.setRemoteAdapter(appWidgetId,R.id.widget_list_view, intent);

      // set up collection
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        setRemoteAdapter(context, rv);
      } else {
        setRemoteAdapterV11(context, rv);
      }
      // This adapter connects
      // to a RemoteViewsService  through the specified intent.
      // This is how you populate the data.


      // The empty view is displayed when the collection has no items.
      // It should be in the same layout used to instantiate the RemoteViews
      // object above.
      rv.setEmptyView(R.id.widget_list_view, R.id.empty_view);

      // Instruct the widget manager to update the widget
      appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    Log.i(TAG, "onReceive"+intent.getAction());

    if(intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE") ||
        intent.getAction().equals("com.google.android.gms.gcm.ACTION_TASK_READY")){

      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }
  }


  /**
   * Sets the remote adapter used to fill in the list items
   *
   * @param views RemoteViews to set the RemoteAdapter
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  private void setRemoteAdapter(Context context, RemoteViews views) {
//        Log.v(LOG_TAG, "setRemoteAdapter, " + "context = [" + context + "], views = [" + views + "]");
    views.setRemoteAdapter(R.id.widget_list_view, new Intent(context, WidgetService.class));
  }

  /**
   * Sets the remote adapter used to fill in the list items
   *
   * @param views RemoteViews to set the RemoteAdapter
   */
  @SuppressWarnings("deprecation")
  private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
//        Log.v(LOG_TAG, "setRemoteAdapterV11, " + "context = [" + context + "], views = [" + views + "]");
    views.setRemoteAdapter(0, R.id.widget_list_view,
        new Intent(context, WidgetService.class));
  }

}

