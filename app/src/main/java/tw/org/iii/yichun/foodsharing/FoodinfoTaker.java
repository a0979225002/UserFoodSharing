package tw.org.iii.yichun.foodsharing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.Loading.LoadingActivity;

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
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 通知對方,傳送對方token
     */
    private void ManagerGiver(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/MessgingService";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("GiverToken",hashmap.get("token").toString().trim());//拿取對方token
                param.put("UserToken",User.getToken());//拿取我方token
                param.put("username", User.getAccount());
                param.put("foodname",hashmap.get("title").toString().trim());

                return param;
            }
        };
        MainUtils.queue.add(request);
    }


    /**
     * 客戶點擊排隊按鈕
     * @param view
     */
    public void queueBtn(View view) {

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("排隊")
                .setMessage("點擊排隊後,需由對方通知您,您才有辦法與對方聯繫")
                .setNegativeButton("不排隊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("確定排隊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManagerGiver();
                    }
                });
        dialog.show();

//        //點擊推播會進入的頁面
//        Intent intent = new Intent(this, LoadingActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        final String CHANNEL_ID = "lipin";

        //android 7以前的不用做,此串為驗證
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

//        //由Builder生成需發送的物件
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_round)
//                .setContentTitle("剩食領取者通知")
//                .setContentText("很重要,一定要看")
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        //由Manager發送物件
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(123, builder.build());
    }
}
