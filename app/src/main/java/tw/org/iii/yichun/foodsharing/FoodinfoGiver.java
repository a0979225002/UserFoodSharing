package tw.org.iii.yichun.foodsharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodinfoGiver extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_giver);
        listView = findViewById(R.id.takerlist);

        setToolbar();
    }



    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoGiver_toolbar);

        String title = "愛心便當"; // 食物名稱
        //todo: 回復 icon
        //todo: 進入卡片編輯頁面 使用edit icon

        //toolbar.setCollapseIcon();
        toolbar.setTitle(title);
    }



}
