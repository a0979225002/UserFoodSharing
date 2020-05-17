package tw.org.iii.yichun.foodsharing.Global;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

/**
 *呼叫volley時,直接使用VolleyApp.queue
 */
public class MainUtils extends Application {
    public static RequestQueue queue;
    private static ACProgressFlower dialog;
    private static List<HashMap<String, Object>> list,giverlist;
    private static boolean gotoProfile;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);//獲得後端網路
    }

    public static boolean isGotoProfile() {
        return gotoProfile;
    }

    public static void setGotoProfile(boolean gotoProfile) {
        MainUtils.gotoProfile = gotoProfile;
    }

    public static List<HashMap<String, Object>> getGiverlist() {
        return giverlist;
    }

    public static void setGiverlist(List<HashMap<String, Object>> giverlist) {
        MainUtils.giverlist = giverlist;
    }

    public static List<HashMap<String, Object>> getList() {
        return list;
    }

    public static void setList(List<HashMap<String, Object>> list) {
        MainUtils.list = list;
    }

    //讀取框
    public static void showloading(Context context){
        dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading....")
                .textSize(50)
                .sizeRatio(0.3f)//背景大小
                .bgAlpha(0.8f)//透明度
                .borderPadding(0.55f)//花瓣長度
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
                bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);

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
     * 官方源碼：https://www.jianshu.com/p/c545f2a6cafc
     * @param base64
     * @return
     */
    public static Bitmap base64Tobitmap(String base64){
        byte[] bytes = Base64.decode(base64,Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        options.inSampleSize = calculateInSampleSize(options, imageWidth, imageHeight);


        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     *
     * 網址： String url = "http://"+Utils.ip+"/FoodSharing_war/Sql_getUser";
     * 需要使用後端網路volley,使這個方法可以拿取值
     * 將值傳給static,方便以後在任何地方都能拿取
     * @param response
     */
    public static void setUser(String response) {

        try {
            JSONArray root = new JSONArray(response);
            for (int i = 0 ;i<root.length();i++){
                JSONObject row = root.getJSONObject(i);

                User.setId(row.optString("id",null));
                User.setAccount(row.optString("account",null));
                User.setName( row.optString("name",null));
                User.setPhone(row.optString("phone",null));
                User.setEmail(row.optString("email",null));
                User.setUserimg(row.optString("img",null));
                User.setAddress(row.optString("address",null));
                User.setCity(row.optString("city",null));
                User.setDist(row.optString("dist",null));
                User.setToken(row.optString("token",null));
                User.setFraction(Double.valueOf(row.optString("fraction",null)));

            }
        }catch (Exception e){
            Log.v("lipin",e.toString());
        }
    }

    }

