package tw.org.iii.yichun.foodsharing.Loading;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.R;

/**
 * 註冊頁面
 */
public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }


}
