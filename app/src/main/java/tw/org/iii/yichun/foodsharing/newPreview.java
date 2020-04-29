package tw.org.iii.yichun.foodsharing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Item.AddFood;


/**
 * 預覽發布的食物卡片畫面
 */
public class newPreview extends AppCompatActivity {

    @BindView(R.id.foodImage)
    ImageView foodImage;        //發布照片
    @BindView(R.id.userImage)
    ImageView userImage;        //客戶大頭照
    @BindView(R.id.username)
    TextView username;          //客戶名稱
    @BindView(R.id.queueBtn)
    Button queueBtn;            //排隊按鈕
    @BindView(R.id.queue)
    TextView queue;             //排隊人數
    @BindView(R.id.address)
    TextView address;           //地址
    @BindView(R.id.category)
    TextView category;          //食物類型
    @BindView(R.id.foodTag)
    TextView foodTag;           //食物標籤
    @BindView(R.id.datetime)
    TextView datetime;          //截止日期
    @BindView(R.id.amount)
    TextView amount;            //食物數量
    @BindView(R.id.shareIt)
    TextView shareIt;           //可否拆領
    @BindView(R.id.Memo)
    TextView Memo;              //備註,說明
    @BindView(R.id.foodname)
    TextView foodname;

    private Intent intent;
    private AddFood addFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_preview);
        ButterKnife.bind(this);

        queueBtn.setEnabled(false);//將排隊按鈕關閉
        queueBtn.setBackgroundColor(Color.GRAY);//更改排隊按鈕顏色

        intent = getIntent();
        addFood = (AddFood) getIntent().getSerializableExtra("savefood");

    }


    /**
     * 分享按鈕
     *
     * @param view
     */
    public void shareIt(View view) {


    }

    /**
     * 改寫返回鍵,點擊返回鍵可以返回AddFoodActivity,且拿取當初給予的值
     */
    @Override
    public void onBackPressed() {
       intent = new Intent(newPreview.this, AddFoodActivity.class);
       intent.putExtra("savefood",addFood);
       startActivityForResult(intent,321);
       finish();
    }
}
