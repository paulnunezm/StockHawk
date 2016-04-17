package com.sam_chordas.android.stockhawk;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by paulnunez on 4/17/16.
 */
public class StockHawk extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
