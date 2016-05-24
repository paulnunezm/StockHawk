package com.sam_chordas.android.stockhawk;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.Calendar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
  private static final String TAG = "ApplicationTest";

  public ApplicationTest() {
    super(Application.class);
  }

  public void testDates(){
    Calendar calendar = Calendar.getInstance();
    int date = calendar.get(Calendar.DATE);
    Log.d(TAG, "testDates: date");
  }
}