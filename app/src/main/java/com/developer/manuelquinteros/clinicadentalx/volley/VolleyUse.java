package com.developer.manuelquinteros.clinicadentalx.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyUse {
    private static VolleyUse mVolleyRP = null;
    private RequestQueue mRequestQueue;

    private VolleyUse(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyUse getInstance(Context context) {
        if (mVolleyRP == null) {
            mVolleyRP = new VolleyUse(context);
        }
        return mVolleyRP;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static void addToQueue(Request request, RequestQueue fRequestQueue, Context context, VolleyUse volley) {
        if (request != null) {
            request.setTag(context);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            fRequestQueue.add(request);
        }
    }
}
