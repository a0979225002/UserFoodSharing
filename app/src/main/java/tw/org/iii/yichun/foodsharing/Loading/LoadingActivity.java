package tw.org.iii.yichun.foodsharing.Loading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.MainActivity;
import tw.org.iii.yichun.foodsharing.R;


/**
 * 讀取頁面
 */
public class LoadingActivity extends AppCompatActivity {

    private String account;
    private String password;
    private int intcount;
    private Location mLastLocation;//取得現在位置(經緯度)
    private GoogleApiClient mGoogleApiClient;
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
        MainUtils.showloading(this);
        getaccount();//拿取存在手機內的帳號密碼
        Log.v("lipin",account+password);
        if (account!=null) {
            Log.v("lipin","有執行媽01");
            //如果是二次登入者,讓他存取該user的資訊,然後Auto_login()方法比對帳密是否正確要跳轉哪個頁面
            User();//將user資訓存到靜態類
        }else {
            Log.v("lipin","有執行媽02");

            //如果是第一次登入者,無帳號,或有帳號密碼錯誤者
            Auto_login();//讓他判斷要跳轉哪個頁面
        }
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


    private void getaccount(){
        account = getSharedPreferences("FoodSharing_User",MODE_PRIVATE)
                .getString("account",null);
        password = getSharedPreferences("FoodSharing_User",MODE_PRIVATE)
                .getString("password",null);
    }
    /**
     * 判斷自動哪取出來的帳號密碼
     * 表單key:FoodSharing_User
     */
    private void Auto_login(){
         if (account!=null&&password!=null) {
             VerifyID();
         }else {
            intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            MainUtils.dimissloading();
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
                            Log.v("lipin", account + "::" + password + "::" + response);

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
        MainUtils.queue.add(request);
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
            MainUtils.dimissloading();

        }else {
            intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            MainUtils.dimissloading();
        }
    }

    /**
     * 拿取user全部資料
     */
    private void User(){
        String url = "http://"+Utils.ip+"/FoodSharing_war/Sql_getUser";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MainUtils.setUser(response);
                        Auto_login();
                        Log.v("lipin","執行完了");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("account",account);
                params.put("passwd",password);

                Log.v("lipin",account+"::::::::"+password);

                return params;
            }
        };
        MainUtils.queue.add(request);
    }
    /**
     * 拿取現在位置
     */
//    private String position(){
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            textLastLocation.setText(
//                    String.valueOf(mLastLocation.getLatitude()) + "\n"
//                            + String.valueOf(mLastLocation.getLongitude()));
//        }else{
//
//        }
//        return
//    }

}
