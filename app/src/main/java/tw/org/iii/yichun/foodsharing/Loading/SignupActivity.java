package tw.org.iii.yichun.foodsharing.Loading;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Global.VolleyApp;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 註冊頁面
 */
public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.passwd)
    EditText passwd;
    @BindView(R.id.passwd_verify)
    EditText passwdVerify;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.captcha)
    EditText captcha;
    @BindView(R.id.allview)
    LinearLayout allview;

    Snackbar snackbar;
    ACProgressFlower dialog;

    private String getaccount;
    private String getphone;
    private String getpassword;
    private String getpasswdVerify;
    private String getcaptcha;//驗證碼
    private String createTime;

    private boolean verify_account_OK;
    private boolean verify_phone_OK;
    private Intent intent;
    private Drawable drawableOK;


//    //驗證碼
//    private FirebaseAuth mAuth;
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    String mVerificationId;//獲得驗證碼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        drawableOK = getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);

//        mAuth = FirebaseAuth.getInstance();

//        initFireBaseCallbacks();//驗證碼

        EditText_verify();


    }

    /**
     * 判斷註冊EditText是否有輸入正確格式
     */
        private void EditText_verify(){
            //帳號欄位
            account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    getaccount = account.getText().toString();
                    if (!hasFocus) {
                        if (getaccount.length()>=6&&getaccount.length()<=8) {
                            verify_account();
                        }else if(getaccount.length()<6 || getaccount.length()>8){
                            account.setError("您輸入的帳號需要6～8之間");
                        }
                    }
                }
            });
            //密碼欄位
            passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    getpassword = passwd.getText().toString();
                    if (!hasFocus) {
                        if (getpassword.length() >= 6 && getpassword.length() <= 8) {
                            if (getpassword.equals(getpasswdVerify)) {
                                passwd.setError(null);
                                passwdVerify.setError(null);
                                passwd.setCompoundDrawables(null, null, null, null);
                                passwdVerify.setCompoundDrawables(null, null, null, null);
                                passwd.setCompoundDrawablesWithIntrinsicBounds(
                                        null, null, drawableOK, null);
                                passwdVerify.setCompoundDrawablesWithIntrinsicBounds(
                                        null, null, drawableOK, null);
                            }
                        } else if (getpassword.length() < 6 && getpassword.length() > 8){
                            passwd.setCompoundDrawablesWithIntrinsicBounds(
                                    null,null,null,null);
                            passwd.setError("您輸入的密碼需要6～8之間");
                        }
                    }
                }
            });
            //第二次輸入密碼欄位
            passwdVerify.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    getpasswdVerify = passwdVerify.getText().toString();

                    if (!hasFocus){
                        if (getpasswdVerify.equals(getpassword)){
                            passwd.setError(null);
                            passwdVerify.setError(null);
                            passwd.setCompoundDrawables(null, null, null, null);
                            passwdVerify.setCompoundDrawables(null, null, null, null);
                            Log.v("lipin",getpassword+"::"+getpasswdVerify);
                            passwd.setCompoundDrawablesWithIntrinsicBounds(
                                    null,null,drawableOK,null);
                            passwdVerify.setCompoundDrawablesWithIntrinsicBounds(
                                    null,null,drawableOK,null);
                        }else if(!getpasswdVerify.equals(getpassword)) {
                            passwd.setCompoundDrawablesWithIntrinsicBounds(
                                    null,null,null,null);
                            passwdVerify.setCompoundDrawablesWithIntrinsicBounds(
                                    null,null,null,null);
                            passwdVerify.setError("兩組輸入的密碼需一樣");
                            passwd.setError("兩組輸入的密碼需一樣");
                        }
                    }
                }
            });
            //電話號碼欄位
            phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                   getphone = phone.getText().toString();
                    if (!hasFocus){
                        if (getphone.length()==10){
                            phone.setError(null);
                            verify_phone();
                        }else {
                            phone.setError("您輸入的電話格式錯誤");
                        }
                    }
                }
            });


        }
    //進度框
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

//    /**
//     * 發送驗證碼
//     */
//    private void initFireBaseCallbacks() {
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                Toast.makeText(SignupActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Toast.makeText(SignupActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                Toast.makeText(SignupActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
//                mVerificationId = verificationId;
//            }
//        };
//    }


    /**
     * 查詢帳號是否重複
     */
    private void verify_account() {
        String url = String.format("http://%s/FoodSharing_war/Sql_Signp_Verify_Account_Servlet?account=%s",
                Utils.ip,getaccount);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        account_check(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleyApp.queue.add(request);
    }
    /**
     * 查詢電話是否重複
     */
    private void verify_phone() {
        Log.v("lipin","到此一遊");
        String url = String.format("http://%s/FoodSharing_war/Sql_Signp_Verify_phone_Servlet?phone=%s",
                Utils.ip,getphone);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        phone_check(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", getphone);

                return params;
            }
        };
        VolleyApp.queue.add(request);
    }

    /**
     * 讀出後台網頁帳號是否有值
     * @param count
     * @return
     */
    private Boolean account_check(String count) {
        String count2 = count.substring(0,1);
        int newCount = Integer.parseInt(count2);
        if (newCount < 1) {
            verify_account_OK = true;
            account.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableOK,null);
        } else {
            verify_account_OK = false;
            account.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            snackbar = Snackbar.make(allview,
                    "此帳號已註冊過",
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        Log.v("lipin",verify_account_OK+"帳號確認");
        return verify_account_OK;
    }

    /**
     * 讀出後台網頁電話是否有值
     * @param count
     * @return
     */
    private Boolean phone_check(String count) {
        String count2 = count.substring(0,1);
        int newCount = Integer.parseInt(count2);
        if (newCount < 1) {
            verify_phone_OK = true;
            phone.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableOK,null);
        } else {
            verify_phone_OK = false;
            phone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            snackbar = Snackbar.make(allview,
                    "此電話已註冊過",
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        Log.v("lipin",verify_phone_OK+"電話確認");
        return verify_phone_OK;
    }


    /**
     * 將帳密加入到sql中保存
     * 註冊按鈕
     * @param view
     */
    public void Signup_Btn(View view) {
        loadingview();
        getaccount = account.getText().toString();
        getpassword = passwd.getText().toString();
        getpasswdVerify = passwdVerify.getText().toString();
        getcaptcha = captcha.getText().toString();//拿取客戶輸入的驗證碼
        getphone = phone.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        createTime = simpleDateFormat.format(date);
        Save_Signup();


        Log.v("lipin",verify_phone_OK+":"+verify_account_OK);

        Log.v("lipin",createTime);
    }

    private void Save_Signup(){
        if (verify_phone_OK && verify_account_OK) {
            String url = "http://" + Utils.ip + "/FoodSharing_war/Sql_Signup_Servlet";
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();
                            snackbar = Snackbar.make(allview,"註冊成功",Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            intent = new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("account",getaccount);
                    params.put("password",getpassword);
                    params.put("phone",getphone);
                    params.put("createTime",createTime);

                    return params;
                }
            };
            VolleyApp.queue.add(request);
        }else {
            dialog.dismiss();
        }
    }

//    /**
//     * 手機驗證碼
//     *獲得手機驗證碼按鈕
//     * @param view
//     */
//    public void Verify_Btn(View view) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+886123456789",        // Phone number to verify
//                1,                 // Timeout duration
//                TimeUnit.MINUTES,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//
//    }
}
