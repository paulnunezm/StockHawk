package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by paulnunez on 4/20/16.
 */
public class HistoricalActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_line_graph);
    }
}
