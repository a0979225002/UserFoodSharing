package tw.org.iii.yichun.foodsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private Patterns patterns;//判斷
    private int Enabled = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
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
    //跳到其他畫面取值判斷
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        Enabled = 0;
        loginbtn.setEnabled(true);
        finish();
    }

    /**
     * 登入失敗
     */
    public void onLogin_Failed(){
        Enabled++;
        Toast.makeText(getBaseContext(),"登入失敗:剩餘"+(5-Enabled>=0?5-Enabled:0)
                ,Toast.LENGTH_LONG).show();

        if (Enabled>=5){
            loginbtn.setEnabled(false);
        }
            loginbtn.setEnabled(true);
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

