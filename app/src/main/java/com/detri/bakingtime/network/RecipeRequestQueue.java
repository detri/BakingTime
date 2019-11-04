package com.detri.bakingtime.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RecipeRequestQueue {
    private RequestQueue requestQueue;
    private static RecipeRequestQueue instance;

    private RecipeRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static RecipeRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRequestQueue(context);
        }
        return instance;
    }

    public void add(Request request) {
        requestQueue.add(request);
    }
}
