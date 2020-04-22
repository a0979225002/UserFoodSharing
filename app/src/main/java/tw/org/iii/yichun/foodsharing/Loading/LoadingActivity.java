package tw.org.iii.yichun.foodsharing.Loading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Global.VolleyApp;
import tw.org.iii.yichun.foodsharing.R;


/**
 * 讀取頁面
 */
public class LoadingActivity extends AppCompatActivity {
    SharedPreferences setting;//將帳密保存手機內
    private ACProgressFlower dialog;//進度框
    Intent intent;
    boolean ispermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        /**
         * 拿取權限
         * gps權限
         */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }else {
            ispermission =true;
            init();
        }

    }
    //執行完權限才做的事
    private void init(){
        loadingview();
        Auto_login();
    }



    /**
     *要求客戶需要有權限
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ispermission =true;
        }else {
                ispermission =false;
            Toast toast =  Toast.makeText(this,
                    "若不開啟GPS我們將無法幫您判斷您附近有多少剩食提供者", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
        }
        init();
    }
//    退後台home鍵效果
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    /**
     * 讀取框執行中
     */
    private void loadingview(){
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading....")
                .textSize(80)
                .sizeRatio(0.4f)//背景大小
                .bgAlpha(0.8f)//透明度
                .borderPadding(0.4f)//花瓣長度
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
    }

    /**
     * 自動拿取登入帳密
     * 表單key:data
     * 儲存帳密key:Auto_USERID
     */
    private void Auto_login(){
        setting = getSharedPreferences("data",MODE_PRIVATE);
        //有拿到以存取的帳密媽？
        if ((setting.getString("Auto_USERID",null))!=null){
            UserID();
//            if (){     //SQl判斷帳密是否正確
//                intent = new Intent(this, MainActivity.class);
//            }else {
//
//            }

        }else {
            dialog.dismiss();
            intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        }

    /**
     * 拿取以存取帳密到web端判斷
     */
    private void UserID(){
        String url = "";

        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        );
        VolleyApp.queue.add(request);
}

    /**
     *抓取web端查詢出來的sql帳密
     * @param jsonArray
     */
//    private void parseJSON(JSONArray jsonArray){
//            jsonArray = new JSONArray();
//            String account = jsonArray.getString();
//            String password = jsonArray.getString();
//
//
//    }




}
