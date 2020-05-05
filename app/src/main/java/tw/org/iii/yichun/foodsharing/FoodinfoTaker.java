package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;

public class FoodinfoTaker extends AppCompatActivity {
    private Intent intent;
    private HashMap<String, Object> hashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_taker);
        setToolbar();

        hashmap = (HashMap<String, Object>) getIntent().getSerializableExtra("foodcard");

        Log.v("lipin",hashmap.toString());



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
        if (item.getItemId() == android.R.id.home){
            finish();//回復前頁
        }
        return super.onOptionsItemSelected(item);
    }
}
