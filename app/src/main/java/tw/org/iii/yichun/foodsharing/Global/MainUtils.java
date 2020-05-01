package tw.org.iii.yichun.foodsharing.Global;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    /**
     * 將照片轉成base64以便可以存入sql
     */
    public static String bitmaptoBase64(Bitmap bitmap){
        String result = null;
        ByteArrayOutputStream outputStream = null;

        try {
            if (bitmap != null) {
                outputStream = new ByteArrayOutputStream();

                //目前圖片並無壓縮,未來如要壓縮,可在參數2更改,數越小壓縮比例越多,比如參數80 = 壓縮20%
                //圖片實際大小並無改變,壓縮的是像素,所以圖檔抓出來後需將圖案大小縮小,不然圖案會變模糊
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

                outputStream.flush();
                outputStream.close();

                byte[] bitmapBytes = outputStream.toByteArray();

                //轉成字串的base64,第二個參數為Base64預設
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            Log.v("lipin",e.toString());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 將base64字串轉成bitmap
     * @param base64
     * @return
     */
    public static Bitmap base64Tobitmap(String base64){
        byte[] bytes = Base64.decode(base64,Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

}