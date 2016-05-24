package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by paulnunez on 5/16/16.
 */
public class WidgetService extends RemoteViewsService {
  private static final String TAG = "WidgetService";



  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    // return the remote view factory here

    Log.d(TAG, "onGetViewFactory: ");

    return new WidgetDataProvider(this, intent);
  }
}
