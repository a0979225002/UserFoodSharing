package tw.org.iii.yichun.foodsharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Item.AddFood;
import tw.org.iii.yichun.foodsharing.Item.User;


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
    @BindView(R.id.allview)
    ScrollView allview;        //整個layout

    private Intent intent;
    private AddFood addFood;
    private File sdroot;
    private Bitmap bitmap;
    private int foodAmount_Number;//食物數量,將字串與數字分開,這裡只有數字
    private String createTime;//建立時間
    private int intcount;//確認是否有加入sql,有:1 沒有:0
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_preview);
        ButterKnife.bind(this);

        queueBtn.setEnabled(false);//將排隊按鈕關閉,不給點擊
        queueBtn.setBackgroundColor(Color.GRAY);//更改排隊按鈕顏色

        intent = getIntent();
        addFood = (AddFood) getIntent().getSerializableExtra("savefood");



        FoodAmount();//取出數量的值,用正則把數字與字串分開
        getFoodCard();//拿取addfoodactivity寫入的值
        getuser();//取得客戶的客戶名稱,與照片
        dismissSnckbar();//關掉snackbar


        Log.v("lipin","拿取id:"+ User.getId());
        Log.v("lipin","拿取帳號"+User.getAddress());
        Log.v("lipin","拿取電話"+User.getPhone());
    }


    /**
     * 關掉snackbar
     */
    private void dismissSnckbar(){
        allview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null) snackbar.dismiss();
            }
        });
    }

    /**
     * 取出數量的值,用正則把數字與字串分開
     */
    private void FoodAmount() {
        String foodAmount = addFood.getAddFoodAmount();
        String foodAmount2 = "[^0-9]";
        Pattern pattern = Pattern.compile(foodAmount2);
        Matcher matcher = pattern.matcher(foodAmount);
        foodAmount_Number = Integer.valueOf(matcher.replaceAll("").trim());

        Log.v("lipin", foodAmount_Number + "食物數量");
    }

    /**
     * 取得客戶的客戶名稱,與照片
     */
    private void getuser() {

        //如果客戶沒有輸入詳細資料,取得不到客戶名稱,將給予帳號名稱來代替
        username.setText(getSharedPreferences("FoodSharing_User", MODE_PRIVATE)
                .getString("name", null));


        if (username.getText().toString().trim().isEmpty()) {
            username.setText(getSharedPreferences("FoodSharing_User", MODE_PRIVATE)
                    .getString("account", null));

        }
    }

    /**
     * 拿取addfoodactivity寫入的值
     */
    private void getFoodCard() {

        String Imgname = addFood.getAddFoodImg();
        queue.setText(foodAmount_Number + "人排隊");
        address.setText(addFood.getMerge_arrdress());
        category.setText(addFood.getAddFoodCategory());
        foodTag.setText(addFood.getAddFoodTag());
        datetime.setText(addFood.getAddFoodDatetime());
        amount.setText(addFood.getAddFoodAmount());

        if (addFood.isShareIt()) {
            shareIt.setText("可以");
        } else {
            shareIt.setText("不可以");
        }

        Memo.setText(addFood.getAddFoodMemo());
        foodname.setText(addFood.getAddFoodName());

        //拿取addfoodactivity儲存的照片名,搜尋該照片後放在此頁面
        sdroot = Environment.getExternalStorageDirectory();
        bitmap = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/" + Imgname);//拿出sd卡位置的圖片
        foodImage.setImageBitmap(bitmap);
    }

    /**
     * 分享按鈕
     *
     * @param view
     */
    public void shareIt(View view) {
        MainUtils.showloading(this);//跳出讀取條

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        createTime = simpleDateFormat.format(date);

        SaveFoodcard();
    }

    /**
     * 將要發布的食物資訊加入sql中
     */
    private void SaveFoodcard() {
        String url = "";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CheckAddfood(response);

                        if (intcount == 1) {
                            intent = new Intent(newPreview.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                            MainUtils.dimissloading();//關掉讀取框

                        } else {
                            MainUtils.dimissloading();
                            snackbar = Snackbar.make(allview,"伺服器過載中,請稍後發布",Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String base64img = MainUtils.bitmaptoBase64(bitmap);//拿取已經將圖片轉成base64的字串

                HashMap<String, String> parmas = new HashMap<>();

                parmas.put("base64img",base64img);

                parmas.put("username", getSharedPreferences("FoodSharing_User", MODE_PRIVATE)
                        .getString("name", null));
                parmas.put("account", getSharedPreferences("FoodSharing_User", MODE_PRIVATE)
                        .getString("account", null));
                parmas.put("foodname", foodname.getText().toString());
                parmas.put("category", category.toString());
                parmas.put("city", addFood.getAddFoodCity());
                parmas.put("dist", addFood.getAddFoodDist());
                parmas.put("address", addFood.getAddress());
                parmas.put("dueDate", datetime.toString());
                parmas.put("tag", foodTag.toString());
                parmas.put("qty", Integer.toString(foodAmount_Number));
                parmas.put("split", addFood.isShareIt() == true ? "1" : "0");
                parmas.put("createtime", createTime);

                return parmas;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 傳回來驗證是否加入sql,如果有就引導到首頁
     */
    private void CheckAddfood(String response) {

        //在這會取得0或1,1是有加入sql,0是沒有加入sql

        String count = response.substring(0, 1);
        intcount = Integer.valueOf(count);
    }


    /**
     * 改寫返回鍵,點擊返回鍵可以返回AddFoodActivity,且拿取當初給予的值
     */
    @Override
    public void onBackPressed() {

        intent = new Intent(newPreview.this, AddFoodActivity.class);
        intent.putExtra("savefood", addFood);
        startActivityForResult(intent, 321);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("lipin", "預覽onStop");
    }
}
