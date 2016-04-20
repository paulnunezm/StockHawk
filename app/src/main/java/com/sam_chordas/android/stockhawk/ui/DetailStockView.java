package com.sam_chordas.android.stockhawk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by paulnunez on 4/17/16.
 */
public class DetailStockView extends LinearLayout {

    private ImageView image;
    private TextView title;
    private TextView value;


    public DetailStockView(Context context) {
        super(context);
    }

    public DetailStockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DetailStockView,
                0, 0);

        init(a.getDrawable(R.styleable.DetailStockView_image_source),
                a.getString(R.styleable.DetailStockView_stock_detail_title));
    }

    public DetailStockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DetailStockView,
                0, 0);

        init(a.getDrawable(R.styleable.DetailStockView_image_source),
                a.getString(R.styleable.DetailStockView_stock_detail_title));

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DetailStockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DetailStockView,
                0, 0);

        init(a.getDrawable(R.styleable.DetailStockView_image_source),
                a.getString(R.styleable.DetailStockView_stock_detail_title));

    }


    // Initialize views
    private void init(Drawable imageSource, String titleText){

        //Inflate xml resource, pass "this" as the parent, we use <merge> tag in xml to avoid
        //redundant parent, otherwise a LinearLayout will be added to this LinearLayout ending up
        //with two view groups
        inflate(getContext(), R.layout.detail_stock_view, null);

        // get the references
        image = (ImageView) findViewById(R.id.stock_icon);
        title = (TextView)  findViewById(R.id.title);
        value = (TextView)  findViewById(R.id.value);

        // set the corresponding image
        image.setImageDrawable(imageSource);

        // set the title text
        setTitleText(titleText);

    }

    public void setValueText(String text){
        value.setText(text);
    }

    public void setTitleText(String text){
        title.setText(text);
    }

}
