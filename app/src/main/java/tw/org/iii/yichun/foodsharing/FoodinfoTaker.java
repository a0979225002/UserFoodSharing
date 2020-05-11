package tw.org.iii.yichun.foodsharing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;

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
    @BindView(R.id.queueBtnID)
    Button queueBtnID;
    @BindView(R.id.allview)
    ScrollView allview;

    private Intent intent;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_taker);
        ButterKnife.bind(this);
        setToolbar();

        intent = getIntent();

        position = intent.getIntExtra("position", -1);

        MainUtils.showloading(this);

        queuestatus();

        getfood();


    }

//    private class UIhandler extends Handler{
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//        }
//    }

    /**
     * 將foodcard的參數顯示在畫面中
     */
    private void getfood() {

        foodImage.setImageBitmap((Bitmap) MainUtils.getList().get(position).get("image"));

        username.setText(
                (String) (!MainUtils.getList().get(position).get("username").toString().trim().isEmpty()
                        ? MainUtils.getList().get(position).get("username") : MainUtils.getList().get(position).get("account")));
        queue.setText("剩餘份數:" +
                (String) MainUtils.getList().get(position).get("leftQuantity") + "份");
        foodname.setText((String) MainUtils.getList().get(position).get("title"));
        address.setText(MainUtils.getList().get(position).get("city").toString() +
                MainUtils.getList().get(position).get("dist").toString() + " " +
                MainUtils.getList().get(position).get("address"));
        category.setText((String) MainUtils.getList().get(position).get("category"));
        foodTag.setText((String) MainUtils.getList().get(position).get("tag"));
        datetime.setText((String) MainUtils.getList().get(position).get("deadline"));
        amount.setText((String) MainUtils.getList().get(position).get("leftQuantity") + "份");
        shareIt.setText((String) MainUtils.getList().get(position).get("quantity"));
        Memo.setText((String) MainUtils.getList().get(position).get("detail"));

        //設定 toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoTaker_toolbar);
        toolbar.setTitle((String)MainUtils.getList().get(position).get("title"));
    }


    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoTaker_toolbar);
        toolbar.setTitle("Food Sharing");
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
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 判斷是否已經有排隊了
     */
    private void queuestatus(){
        String url = "http://"+Utils.ip+"/FoodSharing_war/Sql_QueueStatus";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (getqueuestatus(response)){
                            queueBtnID.setText("排隊中....");
                            queueBtnID.setBackgroundColor(R.drawable.button_shape);//更改排隊按鈕顏色
                        }
                        MainUtils.dimissloading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }


        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("giverid",User.getId());
                params.put("foodcardID",(String) MainUtils.getList().get(position).get("foodid"));
                return params;
            }
        };
        MainUtils.queue.add(request);
    }
    int intqueue;//寫出去,下面單獨按鈕用得到 是0就是沒排隊, 1是有排隊
    private boolean getqueuestatus(String response){
        boolean queuebtn = false;
        String isqueue = response.trim();
        intqueue = Integer.valueOf(isqueue);

        Log.v("lipin",intqueue+":::::::::::::::::::::");
        if (intqueue == 1){
            queuebtn = true;
            return queuebtn;
        }
        return queuebtn;
    }

    /**
     * 通知對方,傳送對方token
     */
    private void ManagerGiver() {
        String url = "http://" + Utils.ip + "/FoodSharing_war/MessgingService";
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                Log.v("lipin", "123124" + MainUtils.getList().get(position).get("token").toString().trim());

                param.put("GiverToken", MainUtils.getList().get(position).get("token").toString().trim());//拿取對方token
                param.put("UserToken", User.getToken());//拿取我方token
                param.put("username", User.getAccount());
                param.put("foodname", MainUtils.getList().get(position).get("title").toString().trim());

                return param;
            }
        };
        MainUtils.queue.add(request);
    }
    /**
     * 將想要排隊的客戶加入sql中
     */
    private void queueSQl(String queue){
        String url = "http://"+Utils.ip+"/FoodSharing_war/SQl_queueTaker";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("lipin",response.trim()+":::"+321);
                        if (response.trim().equals("1")) {
                            ManagerGiver();//發布出去

                            queueBtnID.setText("排隊中....");
                            queueBtnID.setBackgroundColor(R.drawable.button_shape);//更改排隊按鈕顏色
                            MainUtils.dimissloading();
                            queuestatus();//再讓他檢查一次 排隊狀態,這樣才能更換按鈕樣式
                            onqueue = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowdate = format.format(date);

                HashMap<String,String> params = new HashMap<>();
                //索取數量
                params.put("queue",queue);
                //此食物卡片的id
                params.put("foodcardID",(String) MainUtils.getList().get(position).get("foodid"));
                //想要排隊的userid
                params.put("giverid",User.getId());

                params.put("nowdate",nowdate);

                return params;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 更新sql資料庫,讓客戶取消排隊
     */
    private void cancelQueue(){
        String url = "http://"+Utils.ip+"/FoodSharing_war/Sql_cancelQueue";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("1")){
                            queueBtnID.setText("排隊");
                            queueBtnID.setBackgroundColor(Color.BLUE);//更改排隊按鈕顏色
                            onqueue = true;//更改按鈕狀態
                        }
                        MainUtils.dimissloading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }


        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("giverid",User.getId());
                params.put("foodcardID",(String) MainUtils.getList().get(position).get("foodid"));
                return params;
            }
        };
        MainUtils.queue.add(request);
    }


    /**
     * 客戶點擊排隊按鈕
     *
     * @param view
     */
    boolean onqueue = false;
    public void queueBtn(View view) {
        //拿取自訂的dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View newview = inflater.inflate(R.layout.mydialog,null);
        //拿取自訂dialog的editext ID
        EditText editText = newview.findViewById(R.id.addFood_qty);
        Log.v("lipin",onqueue+"::");

        if (intqueue == 1 && onqueue == false){

            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle("確定不排隊了？")
                    .setMessage("系統將會取消你的排隊狀態")
                    .setNegativeButton("不排隊了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainUtils.showloading(FoodinfoTaker.this);
                            cancelQueue();//取消排隊狀態
                        }
                    })
                    .setPositiveButton("繼續排隊", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialog.show();
        }else if (intqueue == 0 || onqueue == true){
            Log.v("lipin",onqueue+"::");
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle("排隊")
                    .setMessage("點擊排隊後,需由對方通知您,您才有辦法與對方聯繫")
                    .setView(newview)
                    .setNegativeButton("不排隊", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("確定排隊", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.v("lipin",editText.getText().toString());

//                            此欄位不是空值才能進來
                            if (!editText.getText().toString().trim().isEmpty()){
                                //此欄位不是空值且值小於發布數量
                                try {
                                    if (Integer.valueOf(editText.getText().toString())<
                                            Integer.valueOf((String) MainUtils.getList().get(position).get("leftQuantity"))){
                                        MainUtils.showloading(FoodinfoTaker.this);

                                        queueSQl(editText.getText().toString());//將排隊狀態加入sql


                                    }else {
                                        Snackbar.make(allview,"您輸入的數量不能大於剩餘數量",Snackbar.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    Log.v("lipin",e.toString());
                                }

                            }else {
                                Snackbar.make(allview,"您需要數入欲索取的數量才能通知對方",Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });
            dialog.show();
        }





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
