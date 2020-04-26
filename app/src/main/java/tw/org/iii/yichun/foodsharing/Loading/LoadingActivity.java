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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Global.VolleyApp;
import tw.org.iii.yichun.foodsharing.MainActivity;
import tw.org.iii.yichun.foodsharing.R;


/**
 * 讀取頁面
 */
public class LoadingActivity extends AppCompatActivity {

    private String account;
    private String password;
    private int intcount;

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
                .sizeRatio(0.3f)//背景大小
                .bgAlpha(0.8f)//透明度
                .borderPadding(0.3f)//花瓣長度
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
    }

    /**
     * 自動拿取登入帳密
     * 表單key:FoodSharing_User
     */
    private void Auto_login(){
         account = getSharedPreferences("FoodSharing_User",MODE_PRIVATE)
                 .getString("account",null);
         password = getSharedPreferences("FoodSharing_User",MODE_PRIVATE)
                 .getString("password",null);

         if (account!=null&&password!=null) {
             VerifyID();
         }else {
            intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            dialog.dismiss();
        }
        }

    /**
     * 拿取以存取帳密到web端判斷
     */
    private void VerifyID(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_Login_Verify";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getVerify(response);
                        Verify();
                        Log.v("lipin",account+"::"+password+"::"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("account",account);
                params.put("passwd",password);
                return params;
            }
        };
        VolleyApp.queue.add(request);
    }

    /**
     * 拿取驗證結果 正確為1 不正確為0
     * @param respons
     */
    private void getVerify(String respons){
        String count = respons.substring(0,1);
        intcount = Integer.valueOf(count);
    }

    /**
     * 確認是否為正確的帳密
     */
    private void Verify(){
        if (intcount ==1){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            dialog.dismiss();

        }else {
            intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            dialog.dismiss();
        }
    }
}
