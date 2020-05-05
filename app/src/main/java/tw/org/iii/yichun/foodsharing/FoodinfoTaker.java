package tw.org.iii.yichun.foodsharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodinfoTaker extends AppCompatActivity {

    @BindView(R.id.foodImage)
    ImageView foodImage;
    @BindView(R.id.userImage)
    ImageView userImage;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.queue)
    TextView queue;
    @BindView(R.id.foodname)
    TextView foodname;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.foodTag)
    TextView foodTag;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.shareIt)
    TextView shareIt;
    @BindView(R.id.Memo)
    TextView Memo;

    private Intent intent;
    private HashMap<String, Object> hashmap;
    private int intqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_taker);
        ButterKnife.bind(this);
        setToolbar();

        hashmap = (HashMap<String, Object>) getIntent().getSerializableExtra("foodcard");

        getfood();
        intqueue = Integer.valueOf((String)hashmap.get("leftQuantity"));

    }

    /**
     * 將foodcard的參數顯示在畫面中
     */
    private void getfood(){
        foodImage.setImageBitmap((Bitmap) hashmap.get("image"));
        username.setText(
                (String)(!hashmap.get("username").toString().trim().isEmpty()?hashmap.get("username"):hashmap.get("account")));
        queue.setText("剩餘份數:"+
                (String)hashmap.get("leftQuantity")+"份");
        foodname.setText((String)hashmap.get("title"));
        address.setText(hashmap.get("city").toString()+
                hashmap.get("dist").toString()+" "+
                hashmap.get("address"));
        category.setText((String)hashmap.get("category"));
        foodTag.setText((String)hashmap.get("tag"));
        datetime.setText((String)hashmap.get("deadline"));
        amount.setText((String)hashmap.get("leftQuantity")+"份");
        shareIt.setText((String)hashmap.get("quantity"));
        Memo.setText((String)hashmap.get("detail"));

        Log.v("lipin",hashmap.get("address")+"123");
    }


    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoTaker_toolbar);
        String title = "愛心便當"; // 食物名稱
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //回復前頁的設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //toolbar切換設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();//回復前頁
        }
        return super.onOptionsItemSelected(item);
    }

    public void queueBtn(View view) {

        intqueue --;
        queue.setText(intqueue+"");
    }
}
