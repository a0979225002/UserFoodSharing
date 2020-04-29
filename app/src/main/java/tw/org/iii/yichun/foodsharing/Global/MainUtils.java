package tw.org.iii.yichun.foodsharing.Global;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.R;

/**
 *呼叫volley時,直接使用VolleyApp.queue
 */
public class MainUtils extends Application {
    public static RequestQueue queue;
    private static ACProgressFlower dialog;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);//獲得後端網路

    }
    //讀取框
    public static void showloading(Context context){
        dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading....")
                .textSize(80)
                .sizeRatio(0.3f)//背景大小
                .bgAlpha(0.8f)//透明度
                .borderPadding(0.3f)//花瓣長度
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
    }
    //讓讀取框消失
    public static  void dimissloading(){
        dialog.dismiss();
    }


}
