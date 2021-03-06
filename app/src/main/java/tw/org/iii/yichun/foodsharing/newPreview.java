package tw.org.iii.yichun.foodsharing;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
import tw.org.iii.yichun.foodsharing.Global.Utils;
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
    private String editFoodcard;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_preview);
        ButterKnife.bind(this);

        queueBtn.setEnabled(false);//將排隊按鈕關閉,不給點擊
        queueBtn.setBackgroundColor(Color.GRAY);//更改排隊按鈕顏色

        intent = getIntent();
        addFood = (AddFood) getIntent().getSerializableExtra("savefood");
        editFoodcard = intent.getStringExtra("FoodinfoGiver");
        position = intent.getIntExtra("position",-1);

        Log.v("lipin",editFoodcard+123);

        dismissSnckbar();//關掉snackbar

        FoodAmount();//取出數量的值,用正則把數字與字串分開
        getFoodCard();//拿取addfoodactivity寫入的值
        getuser();//取得客戶的客戶名稱,與照片
        setToolbar();//設定 Toolbar



        Log.v("lipin",editFoodcard+"");
        Log.v("lipin","拿取帳號"+User.getAddress());
        Log.v("lipin","拿取電話"+User.getPhone());
    }

    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.addfoodcard_toolbar);
        String title = null;

        title = "預覽";

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //回復前頁的設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //toolbar切換設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(newPreview.this, AddFoodActivity.class);
                intent.putExtra("savefood", addFood);

                if (editFoodcard !=null) {
                    intent.putExtra("FoodinfoGiver", "editFoodcard");//通知intent的b介面能更改
                    intent.putExtra("FoodinfoGiver_preview", "editFoodcard2");//通知intent的b介面能更改
                    intent.putExtra("position", position);
                }
                startActivityForResult(intent, 321);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
                return true; //回復前頁

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * 關掉snackbar
     */
    private void dismissSnckbar(){
        allview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (snackbar != null) snackbar.dismiss();
                    Log.v("lipin","點擊事件");
                }
                return false;
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
            username.setText(User.getName());

        if (User.getName() == null) {
            username.setText(User.getAccount());
        }
    }

    /**
     * 拿取addfoodactivity寫入的值
     */
    private void getFoodCard() {
        String Imgname = addFood.getAddFoodImg();

        if (Imgname ==null){
            Log.v("lipin",position+Imgname+"asdadas");
            bitmap = (Bitmap) MainUtils.getGiverlist().get(position).get("image");
        }

        queue.setText(foodAmount_Number + "人排隊");
        address.setText(addFood.getMerge_arrdress());
        category.setText(addFood.getAddFoodCategory());
        foodTag.setText(addFood.getAddFoodTag());
        datetime.setText(addFood.getAddFoodDatetime());
        amount.setText(addFood.getAddFoodAmount()+"份");

        if (addFood.isShareIt()) {
            shareIt.setText("可以");
        } else {
            shareIt.setText("不可以");
        }

        Memo.setText(addFood.getAddFoodMemo());
        foodname.setText(addFood.getAddFoodName());

        //拿取addfoodactivity儲存的照片名,搜尋該照片後放在此頁面
        if (Imgname !=null) {
            sdroot = Environment.getExternalStorageDirectory();
            bitmap = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/" + Imgname);//拿出sd卡位置的圖片

        }
        foodImage.setImageBitmap(bitmap);


    }

    /**
     * 分享按鈕
     * @param view
     */
    public void shareIt(View view) {
        MainUtils.showloading(this);//跳出讀取條

        //加入建立時間
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        createTime = simpleDateFormat.format(date);

        if (editFoodcard!= null){
            SaveEditFoodCard();
        }else {
            SaveFoodcard();
        }
    }
    /**
     * 將要編修的食物發布出去
     */
    private void SaveEditFoodCard(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/SaveEditFoodCard";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("1")) {
                            Log.v("lipin","廣播發送");

                            intent = new Intent(newPreview.this, MainActivity.class);
                            Intent intent2 = new Intent("EditFoodCard");//加入廣播中
                            intent2.putExtra("lipin","有來媽");
                            sendBroadcast(intent2);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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

                parmas.put("foodimg",base64img);
                parmas.put("foodid", (String) MainUtils.getGiverlist().get(position).get("foodid"));
                parmas.put("userID",User.getId());
                parmas.put("foodname", foodname.getText().toString());
                parmas.put("category", category.getText().toString().trim());
                parmas.put("city", addFood.getAddFoodCity());
                parmas.put("dist", addFood.getAddFoodDist());
                parmas.put("address", addFood.getAddress());
                parmas.put("dueDate", datetime.getText().toString());
                parmas.put("tag", foodTag.getText().toString().trim());
                parmas.put("qty", Integer.toString(foodAmount_Number));
                parmas.put("split", addFood.isShareIt() == true ? "1" : "0");
                parmas.put("createtime", createTime);
                parmas.put("detail",Memo.getText().toString());

                Log.v("lipin", "測試::"+datetime.getText().toString());
                return parmas;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 將要發布的食物資訊加入sql中
     */
    private void SaveFoodcard() {

        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_AddFoodCard";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.trim().equals("1")) {
                            intent = new Intent(newPreview.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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

                parmas.put("foodimg",base64img);

                parmas.put("userID",User.getId());
                parmas.put("foodname", foodname.getText().toString());
                parmas.put("category", category.getText().toString().trim());
                parmas.put("city", addFood.getAddFoodCity());
                parmas.put("dist", addFood.getAddFoodDist());
                parmas.put("address", addFood.getAddress());
                parmas.put("dueDate", datetime.getText().toString());
                parmas.put("tag", foodTag.getText().toString().trim());
                parmas.put("qty", Integer.toString(foodAmount_Number));
                parmas.put("split", addFood.isShareIt() == true ? "1" : "0");
                parmas.put("createtime", createTime);
                parmas.put("detail",Memo.getText().toString());

                Log.v("lipin", "測試::"+datetime.getText().toString());
                return parmas;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 傳回來驗證是否加入sql,如果有就引導到首頁
     */
    private void CheckAddfood(String response) {
        Log.v("lipin","測試拿取"+response);

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
        if (editFoodcard !=null) {
            intent.putExtra("FoodinfoGiver", "editFoodcard");//通知intent的b介面能更改
            intent.putExtra("FoodinfoGiver_preview", "editFoodcard2");//通知intent的b介面能更改
            intent.putExtra("position", position);
        }
        startActivityForResult(intent, 321);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("lipin", "預覽onStop");
    }
}
