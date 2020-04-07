package tw.org.iii.yichun.foodsharing.Loading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 登入頁面
 */
public class LoginActivty extends AppCompatActivity {

    @BindView(R.id.account) EditText account;//帳號
    @BindView(R.id.password) EditText password;//密碼
    @BindView(R.id.signup_link) TextView signup_link;//註冊
    @BindView(R.id.forget_password_link)TextView forget_password_link;//忘記密碼
    @BindView(R.id.loginbtn) Button loginbtn;
    private boolean validate;//判斷帳密是否輸入正確
    private int Enabled = 0;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
    /**
     * 登入時判斷
     */
    public void Login(View view) {
        if (!isValidate()){
            onLogin_Failed();
            return;
        }
    }

    /**
     * 註冊
     * @param view
     */
    public void Signuplink(View view) {
        intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent,0);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    /**
     * 忘記密碼
     * @param view
     */
    public void Forgetpasswordlink(View view) {


    }
 //   跳到其他畫面取值判斷
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                this.finish();
            }
        }

    }

    //輸入退回鍵,會有home鍵效果,回到後台
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    /**
     * 登入成功
     */
    public void onlogin_successfully(){
        Enabled = 0;//將登入失敗次數歸零
        loginbtn.setEnabled(true);
        finish();
    }

    /**
     * 登入失敗
     */
    public void onLogin_Failed(){
        Enabled++;
        if (Enabled<5){
            Toast.makeText(this,"登入失敗:剩餘"+(5-Enabled>=0?5-Enabled:0)
                    ,Toast.LENGTH_SHORT).show();
        }

        if (Enabled>=5){
            Toast.makeText(this,"失敗次數過多,請稍後在試",Toast.LENGTH_LONG).show();
            loginbtn.setEnabled(false);
        }else {
            loginbtn.setEnabled(true);
        }

    }

    /**
     * 登入帳密判斷是否輸入正確
     * @return
     */
    private boolean isValidate(){
        validate = true;
        String isaccount = account.getText().toString();
        String ispassword = password.getText().toString();

        if (isaccount.isEmpty() || ispassword.length()<6 || ispassword.length() > 10){
            account.setError("帳號格式輸入錯誤,請輸入6~10之間");
            validate =false;
        }
        if (ispassword.isEmpty() || ispassword.length()<6 || ispassword.length() > 10){
            password.setError("密碼格式輸入錯誤,請輸入6~10之間");
            validate =false;
        }
        return validate;
    }


}

