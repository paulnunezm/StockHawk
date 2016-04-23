package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by paulnunez on 4/20/16.
 */
public class HistoricalActivity extends AppCompatActivity {
    private static final String TAG = "HistoricalActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);


        // Being here means we are in animation mode
//        supportPostponeEnterTransition();
//        supportStartPostponedEnterTransition();
    }

}
