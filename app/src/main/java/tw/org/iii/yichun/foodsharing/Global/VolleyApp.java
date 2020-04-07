package tw.org.iii.yichun.foodsharing.Global;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *呼叫volley時,直接使用VolleyApp.queue
 */
public class VolleyApp extends Application {
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }
}
