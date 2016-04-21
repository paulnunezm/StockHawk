package com.sam_chordas.android.stockhawk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by paulnunez on 4/17/16.
 */
public class DetailStockView extends LinearLayout {

    private AppCompatImageView image;
    private TextView title;
    private TextView value;
    private Context context;
    private AttributeSet attr;


    public DetailStockView(Context context) {
        super(context);
    }

    public DetailStockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) init(context, attrs);

    }

    public DetailStockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(!isInEditMode()) init(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DetailStockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if(!isInEditMode()) init(context, attrs);

    }


    // Initialize views
    private void init(Context context, AttributeSet attrs){

        //Inflate xml resource, pass "this" as the parent, we use <merge> tag in xml to avoid
        //redundant parent, otherwise a LinearLayout will be added to this LinearLayout ending up
        //with two view groups

        LayoutInflater inflater = (LayoutInflater) context
                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.detail_stock_view, this);

        this.attr = attrs;
        this.context = context;

        // get the references
        image = (AppCompatImageView) findViewById(R.id.stock_icon);
        title = (TextView)  findViewById(R.id.title);
        value = (TextView)  findViewById(R.id.value);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attr,
                R.styleable.DetailStockView,
                0, 0);

        // set the corresponding image
        Drawable drawable =  a.getDrawable(R.styleable.DetailStockView_image_source);
        image.setImageDrawable(drawable);
        int color =  a.getColor(R.styleable.DetailStockView_icon_tint, Color.DKGRAY);
        image.setColorFilter(color);

        // set the title text
        setTitleText(a.getString(R.styleable.DetailStockView_stock_detail_title));
    }




    public void setValueText(String text){
        value.setText(text);
    }

    public void setTitleText(String text){
        title.setText(text);
    }

}
