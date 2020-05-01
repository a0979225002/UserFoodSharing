package tw.org.iii.yichun.foodsharing.Loading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.MainActivity;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 登入頁面
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.account)
    EditText account;//帳號
    @BindView(R.id.password)
    EditText password;//密碼
    @BindView(R.id.signup_link)
    TextView signup_link;//註冊
    @BindView(R.id.forget_password_link)
    TextView forget_password_link;//忘記密碼
    @BindView(R.id.loginbtn)
    Button loginbtn;
    @BindView(R.id.allview)
    LinearLayout allview;

    Snackbar snackbar;
    private boolean validate;//判斷帳密是否輸入正確
    private int Enabled = 0;
    private Intent intent;

    private String getaccount;
    private String getpasswd;
    private int intcount;//1：是後端判斷帳密正確,0：是後端帳密判斷錯誤
    private SharedPreferences addUser;//保存帳密
    private ACProgressFlower dialog;//進度框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getSignup();//拿取註冊時的帳號密碼
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
     * 如果是從註冊頁面回來的客戶,擁有直接拿取當初註冊時填寫的帳號密碼
     */
    private void getSignup() {
        intent = this.getIntent();
        getaccount = intent.getStringExtra("account");
        getpasswd = intent.getStringExtra("passwd");
        account.setText(getaccount);
        password.setText(getpasswd);
    }

    /**
     * 登入按鈕
     */
    public void Login(View view) {
        loadingview();
        if (isValidate()) {

            Verify_account_passwd();


        } else if (!isValidate()) {//只要輸入格式錯誤就不給傳入後端判斷
            dialog.dismiss();
            onLogin_Failed();
        }
    }

    /**
     * 註冊按鈕
     * @param view
     */
    public void Signuplink(View view) {
        intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 忘記密碼按鈕
     * @param view
     */
    public void Forgetpasswordlink(View view) {


    }

    //   跳到其他畫面取值判斷
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }

    }

    // 輸入退回鍵,會有home鍵效果,回到後台
    @Override
    public void onBackPressed() {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 後台驗證帳密
     */
    private void Verify_account_passwd(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_Login_Verify";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Verify(response);
                        onlogin_successfully();
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
                params.put("account",getaccount);
                params.put("passwd",getpasswd);

                return params;
            }
        };

        MainUtils.queue.add(request);
    }
    /**
     * 帳密驗證是否正確
     * 1是正確 0是錯誤
     */
    private void Verify(String respons){
        String count = respons.substring(0,1);
        intcount = Integer.valueOf(count);

    }

    /**
     * 登入成功
     */
    private void onlogin_successfully() {
        if (intcount == 1){
            //將帳密存到手機內記錄
            addUser = getSharedPreferences("FoodSharing_User",MODE_PRIVATE);
            addUser.edit()
                    .putString("account",getaccount)
                    .putString("password",getpasswd)
                    .commit();
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            dialog.dismiss();
        }else {
            onLogin_Failed();//增加次數限制 超過五次以上將會無法登入
        }
    }
    /**
     * 登入失敗
     */
    private void onLogin_Failed() {
        Enabled++;
        if (validate) {
            if (Enabled < 5) {
                snackbar = Snackbar.make(allview,
                        "登入失敗:剩餘" + (5 - Enabled >= 0 ? 5 - Enabled : 0), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                return;
            }

            if (Enabled >= 5) {
                snackbar = Snackbar.make(allview,
                        "失敗次數過多,請稍後在試", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                loginbtn.setEnabled(false);
                loginbtn.setBackgroundColor(Color.GRAY);
            } else {
                loginbtn.setEnabled(true);
            }
        }

    }

    /**
     * 登入帳密判斷格式是否輸入正確
     *
     * @return
     */
    private boolean isValidate() {

        validate = true;
        getaccount = account.getText().toString();
        getpasswd = password.getText().toString();

        if (!getaccount.isEmpty()) {
            if (getaccount.length() < 6 || getaccount.length() > 10) {
                account.setError("帳號格式輸入錯誤,請輸入6~10之間");
                validate = false;
                return validate;
            }
        } else {
            snackbar = Snackbar.make(allview,"帳號不能留空",Snackbar.LENGTH_LONG);
            snackbar.show();
            validate = false;
            return validate;
        }
        if (!getpasswd.isEmpty()) {
            if (getpasswd.length() < 6 || getpasswd.length() > 10) {
                password.setError("密碼格式輸入錯誤,請輸入6~10之間");
                validate = false;
                return validate;
            }
        }else {
            snackbar = Snackbar.make(allview,"密碼不能留空",Snackbar.LENGTH_LONG);
            snackbar.show();
            validate = false;
            return validate;
        }
        return validate;
    }
}

