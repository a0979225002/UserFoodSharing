package tw.org.iii.yichun.foodsharing.Loading;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 註冊頁面
 */
public class SignupActivity extends AppCompatActivity {

    TextInputEditText editText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


    }


    public void Signup_Btn(View view) {
    }
}
