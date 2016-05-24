package com.sam_chordas.android.stockhawk.entities;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by paulnunez on 4/22/16.
 */
public class HistoricalDeserializer implements JsonDeserializer<StockHistory> {
    @Override
    public StockHistory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // Get the "content" element from the parsed JSON
        JsonElement query = json.getAsJsonObject().get("query");
        JsonElement results = query.getAsJsonObject().get("results");


        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(results, StockHistory.class);
    }
}
