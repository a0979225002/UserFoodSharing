package tw.org.iii.yichun.foodsharing.Loading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cc.cloudist.acplibrary.ACProgressPie;
import tw.org.iii.yichun.foodsharing.R;

public class LoadingActivity extends AppCompatActivity {
    SharedPreferences setting;
    ACProgressFlower dialog;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingview();
        Auto_login();
    }

    /**
     * 讀取匡執行中
     */
    private void loadingview(){
        //讀取匡
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

        if ((setting.getString("Auto_USERID",null))!=null){



        }else {
            dialog.dismiss();
            intent = new Intent(this,LoginActivty.class);
            startActivityForResult(intent,0);
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        }


}
