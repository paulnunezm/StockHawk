package com.sam_chordas.android.stockhawk.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by paulnunez on 4/22/16.
 */
public class StockHistory implements Serializable {


    @SerializedName("quote")
    ArrayList<Values> history;

    public ArrayList<Values> getHistory() {
        return history;
    }




    public class Values implements Serializable{
        String Date;
        Float Open;
        Float High;
        Float Low;
        Float Close;
        Float Volume;
        Float Adj_Close;

        public String getDate() {
            return Date;
        }

        public Float getOpen() {
            return Open;
        }

        public Float getHigh() {
            return High;
        }

        public Float getLow() {
            return Low;
        }

        public Float getClose() {
            return Close;
        }

        public Float getVolume() {
            return Volume;
        }

        public Float getAdj_Close() {
            return Adj_Close;
        }
    }



}
