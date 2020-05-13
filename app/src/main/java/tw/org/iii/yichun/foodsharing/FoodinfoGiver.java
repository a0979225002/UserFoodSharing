package tw.org.iii.yichun.foodsharing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;

public class FoodinfoGiver extends AppCompatActivity {

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
    @BindView(R.id.takerlist)
    ListView takerlist;
    @BindView(R.id.itemview)
    LinearLayout itemview;
    @BindView(R.id.displayBtn)
    TextView displayBtn;
    @BindView(R.id.shareBtnID)
    Button shareBtnID;
    private ListView listView;
    private Intent intent;
    private int position;
    private ListViewAdapter ListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_giver);
        ButterKnife.bind(this);


        listView = findViewById(R.id.takerlist);

        intent = getIntent();
        position = intent.getIntExtra("position", -1);

        setToolbar();//設定tool bar
        getfood();//將食物資料顯示在螢幕中
        getTaker();//拿取有多少人來此排隊


    }

    //listview點擊監聽
    private void takerlistTouch() {
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("lipin", ListViewAdapter.getCount() + "");
                if (ListViewAdapter.getCount() >= 2) {
                    ListViewAdapter.additemNum(1);
                    ListViewAdapter.notifyDataSetChanged();
                    itemview.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.updown_show));

                } else {
                    ListViewAdapter.additemNum(list.size());
                    ListViewAdapter.notifyDataSetChanged();
                    itemview.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.updown_show));
                }
            }

        });
    }

    /**
     * 結束分享按鈕
     *
     * @param view
     */
    public void shareBtn(View view) throws ParseException {
//        如果是結束狀態
        if (MainUtils.getGiverlist().get(position).get("status").toString().equals("0")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date duedate = new Date();
            Date nowdate = new Date();
            duedate = format.parse((String) MainUtils.getGiverlist().get(position).get("deadline"));
            Log.v("lipin",duedate+"");

            //現在時間在截止日期之後
            if (nowdate.before(duedate)){

                userFoodEnd();

                //現在時間在截止日期之前
            }else if (nowdate.after(duedate)){
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                        .setTitle("截止日期已過期")
                        .setMessage("您需要先更改此分布的截止時間")
                        .setNegativeButton("前往更改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FoodinfoGiver.this, AddFoodActivity.class);
                                intent.putExtra("FoodinfoGiver","editFoodcard");//通知intent的b介面能更改
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dialog.show();
                Log.v("lipin","時間已加入");
            }
//            如果是分享中狀態
        }else if (MainUtils.getGiverlist().get(position).get("status").toString().equals("1")){
            userFoodEnd();
        }

        }


    /**
     * 結束分享
     */
    private void userFoodEnd() {
        String url = "http://" + Utils.ip + "/FoodSharing_war/Sql_foodEnd";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("結束分享")){
                            shareBtnID.setText("結束分享");
                            MainUtils.getGiverlist().get(position).put("status",0);
                        }else if (response.trim().equals("分享中")){
                            shareBtnID.setText("分享中...");
                            MainUtils.getGiverlist().get(position).put("status",1);
                        }
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
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", User.getId());
                params.put("foodid", (String) MainUtils.getGiverlist().get(position).get("foodid"));


                return params;
            }
        };
        MainUtils.queue.add(request);
    }


    //拿取剩餘份數
    private void Remainingqueue() {
        int restqueue = Integer.valueOf(MainUtils.getGiverlist().get(position).get("leftQuantity").toString());
        for (int i = 0; i < list.size(); i++) {

            int userqty = Integer.valueOf(list.get(i).get("qty").toString());

            restqueue = restqueue - userqty;
        }
        queue.setText("剩餘份數:" + restqueue + "份");
    }


    /**
     * 將食物資料顯示在螢幕中
     */
    private void getfood() {

        foodImage.setImageBitmap((Bitmap) MainUtils.getGiverlist().get(position).get("image"));

        username.setText(User.getName() != null ? User.getName() : User.getAccount());
        foodname.setText((String) MainUtils.getGiverlist().get(position).get("title"));
        address.setText(MainUtils.getGiverlist().get(position).get("city").toString() +
                MainUtils.getGiverlist().get(position).get("dist").toString() + " " +
                MainUtils.getGiverlist().get(position).get("address"));
        category.setText((String) MainUtils.getGiverlist().get(position).get("category"));
        foodTag.setText((String) MainUtils.getGiverlist().get(position).get("tag"));
        datetime.setText((String) MainUtils.getGiverlist().get(position).get("deadline"));
        amount.setText((String) MainUtils.getGiverlist().get(position).get("leftQuantity") + "份");
        shareIt.setText((String) MainUtils.getGiverlist().get(position).get("quantity"));
        Memo.setText((String) MainUtils.getGiverlist().get(position).get("detail"));

        if (MainUtils.getGiverlist().get(position).get("status").toString().equals("0")){
            shareBtnID.setText("已結束分享");
        }else if (MainUtils.getGiverlist().get(position).get("status").toString().equals("1")){
            shareBtnID.setText("分享中...");
        }

    }

    public class ListViewAdapter extends BaseAdapter {
        private List<HashMap<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        private int itemCount = 1;

        public ListViewAdapter(Context context, List<HashMap<String, Object>> data) {
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        private void additemNum(int number) {
            itemCount = number;
        }

        /**
         * HomeList Item 集合，對應 listView_foodinfogiver_takerlist.xml
         */
        private final class ListItem {
            private ImageView userImage, send; // 食物照片
            private TextView orderNo, takername, takerwant;
            private Button getornotbtn;
        }

        @Override
        public int getCount() {
            if (data.size() >= 2) {
                Log.v("lipin", "有來媽");
                return itemCount;
            } else {
                return data.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 取得 listView_home.xml
            ListItem ListItem = new ListItem();
            convertView = layoutInflater.inflate(R.layout.listview_foodinfogiver_takerlist, null);
            ListItem.orderNo = (TextView) convertView.findViewById(R.id.takerlist_orderNo);
            ListItem.userImage = (ImageView) convertView.findViewById(R.id.takerlist_userImage);
            ListItem.takername = (TextView) convertView.findViewById(R.id.takerlist_takername);
            ListItem.takerwant = (TextView) convertView.findViewById(R.id.takerlist_takerwant);
            ListItem.getornotbtn = (Button) convertView.findViewById(R.id.takerlist_getornotbtn);
            ListItem.send = (ImageView) convertView.findViewById(R.id.takerlist_send);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.orderNo.setText((int) data.get(position).get("orderNo") + "");
            ListItem.userImage.setImageResource((Integer) data.get(position).get("userImage"));
            ListItem.takername.setText((String) data.get(position).get("username"));
            ListItem.takerwant.setText("想要" + data.get(position).get("qty") + "份");

            if (data.get(position).get("takeornot").equals("0")) {
                ListItem.getornotbtn.setText("未領取");
            } else if (data.get(position).get("takeornot").equals("1")) {
                ListItem.getornotbtn.setText("已領取");
            }
            //todo:按鈕點選後要改變樣式 未領取/已領取
            ListItem.getornotbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "http://" + Utils.ip + "/FoodSharing_war/updateTakeornot";
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("lipin", response);
                                    if (response.trim().equals("0")) {
                                        ListItem.getornotbtn.setText("未領取");
                                        data.get(position).put("takeornot", 0);
                                    } else if (response.trim().equals("1")) {
                                        ListItem.getornotbtn.setText("已領取");
                                        data.get(position).put("takeornot", 1);
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
                            HashMap<String, String> params = new HashMap<>();
                            params.put("userid", list.get(position).get("userid").toString());
                            params.put("takeornot", list.get(position).get("takeornot").toString());
                            params.put("foodid", (String) MainUtils.getGiverlist().get(FoodinfoGiver.this.position).get("foodid"));
                            Log.v("lipin", MainUtils.getGiverlist().get(FoodinfoGiver.this.position).get("foodid").toString());

                            return params;
                        }
                    };
                    MainUtils.queue.add(request);
                }
            });
            ListItem.send.setImageResource(R.drawable.ic_send_black_24dp);

            //聊天室按鈕
            ListItem.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(FoodinfoGiver.this)
                            .setTitle("前往聊天室")
                            .setMessage("前往聊天室前，您是否需要用推播通知對方")
                            .setNeutralButton("離開", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("通知", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /**
                                     * 通知對方,傳送對方token
                                     */
                                    String url = "http://" + Utils.ip + "/FoodSharing_war/GiverMessgingService";
                                    StringRequest request = new StringRequest(
                                            Request.Method.POST,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Intent intent = new Intent(context, ChatRoomActivity.class);
                                                    startActivityForResult(intent, 0);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                                            HashMap<String, String> param = new HashMap<>();

//                                            Log.v("lipin", "123124" + data.get(position).get("token").toString().trim());

                                            param.put("TakerToken", data.get(position).get("token").toString().trim());//拿取對方token
                                            param.put("UserToken", User.getToken());//拿取我方token
                                            param.put("username", User.getAccount());
                                            param.put("foodname", (String) MainUtils.getGiverlist().get(FoodinfoGiver.this.position).get("title"));

                                            return param;
                                        }
                                    };
                                    MainUtils.queue.add(request);
                                }
                            })
                            .setPositiveButton("不通知", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, ChatRoomActivity.class);
                                    startActivityForResult(intent, 0);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            });
                    dialog.show();
                }
            });

            return convertView;

        }
    }


    /**
     * 拿取user自己的卡片資訊想排隊之人的資訊
     *
     * @return
     */
    private void getTaker() {
        String url = "http://" + Utils.ip + "/FoodSharing_war/Sql_getTaker";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonTakers(response);
                        Log.v("lipin", response);

                        ListViewAdapter = new ListViewAdapter(FoodinfoGiver.this, list);
                        listView.setAdapter(ListViewAdapter);
                        takerlistTouch();//listview點擊監聽
                        Remainingqueue();
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
                HashMap<String, String> params = new HashMap<>();

                params.put("foodid", (String) MainUtils.getGiverlist().get(position).get("foodid"));
                Log.v("lipin", (String) MainUtils.getGiverlist().get(position).get("foodid"));

                return params;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 抓取json的食物資訊
     *
     * @param response
     */
    JSONObject row;
    HashMap<String, Object> hashMap;
    List<HashMap<String, Object>> list;

    private void JsonTakers(String response) {

        list = new ArrayList<HashMap<String, Object>>();

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                row = array.getJSONObject(i);

                getData(i);//將抓取的值一個一個放在listview裡面

            }

        } catch (Exception e) {
            Log.v("lipin", "JsonFoodcard:" + e.toString());
        }

    }

    /**
     * 食物 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public void getData(int i) throws ParseException {
        //因為list加入的方式的比對方式是地址,重複的地址物件會被蓋過,所以需要每次尋訪時是產生新的hashMap,故在此new出來
        hashMap = new HashMap<String, Object>();

        //如果user有自己的大頭照,就使用user自己的大頭照
        if (row.optString("img", null) != null) {
            //取出base64的圖片
            String base64Img = row.optString("img");
            //轉成bitmap
            Bitmap bitmap = MainUtils.base64Tobitmap(base64Img);

            hashMap.put("userImage", bitmap);
        } else {
            hashMap.put("userImage", R.drawable.ic_person_24dp);
        }

        hashMap.put("orderNo", i + 1);
        hashMap.put("username", row.optString("name", null) != null ?
                row.optString("name", null) : row.optString("account"));

        hashMap.put("qty", row.optString("qty"));
        hashMap.put("takeornot", row.optString("takeornot"));
        hashMap.put("userid", row.optString("user_id"));
        hashMap.put("token", row.optString("token"));

        Log.v("lipin", row.optString("token"));


        list.add(hashMap);
    }

    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoGiver_toolbar);

        String title = (String) MainUtils.getGiverlist().get(position).get("title"); // 食物名稱

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //回復前頁的設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu); //設定toolbar編輯按鈕

        return true;
    }

    //toolbar切換設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true; //回復前頁
            case R.id.menu_edit: //去編輯頁面

                Intent intent = new Intent(FoodinfoGiver.this, AddFoodActivity.class);
                intent.putExtra("FoodinfoGiver","editFoodcard");//通知intent的b介面能更改
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
