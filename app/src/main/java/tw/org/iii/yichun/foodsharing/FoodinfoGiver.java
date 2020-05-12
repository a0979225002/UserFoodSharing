package tw.org.iii.yichun.foodsharing;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    @BindView(R.id.remaining)
    TextView remaining;
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
    @BindView(R.id.allview)
    ScrollView allview;
    private ListView listView;
    private Intent intent;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo_giver);
        ButterKnife.bind(this);
        listView = findViewById(R.id.takerlist);

        intent = getIntent();
        position =  intent.getIntExtra("position", -1);

        setToolbar();//設定tool bar
        getfood();//將食物資料顯示在螢幕中
        getTaker();//拿取有多少人來此排隊




    }
    private void getfood() {

        foodImage.setImageBitmap((Bitmap) MainUtils.getGiverlist().get(position).get("image"));

        username.setText(User.getName()!= null?User.getName():User.getAccount());
        queue.setText("剩餘份數:" +
                (String) MainUtils.getGiverlist().get(position).get("leftQuantity") + "份");
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


    }


    private boolean isStatus;

    public class ListViewAdapter extends BaseAdapter {
        private List<HashMap<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;

        public ListViewAdapter(Context context, List<HashMap<String, Object>> data) {
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
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
            return data.size();
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

            // 綁定資料 (todo: 資料data.get(position).get("key")無法成功，先寫死，刻出版面)
            ListItem.orderNo.setText((int)data.get(position).get("orderNo")+"");
            ListItem.userImage.setImageResource((Integer) data.get(position).get("userImage"));
            ListItem.takername.setText((String)data.get(position).get("username"));
            ListItem.takerwant.setText("想要"+data.get(position).get("qty")+"份");

            if (data.get(position).get("takeornot").equals("0")){
                ListItem.getornotbtn.setText("未領取");
            }else if (data.get(position).get("takeornot").equals("1")){
                ListItem.getornotbtn.setText("已領取");
            }
            String ornot = (String) data.get(position).get("takeornot");
            //todo:按鈕點選後要改變樣式 未領取/已領取
            ListItem.getornotbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "http://"+Utils.ip+"/FoodSharing_war/updateTakeornot";
                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("lipin",response);
                                        if (response.trim().equals("0")){
                                            ListItem.getornotbtn.setText("未領取");

                                        }else if (response.trim().equals("1")){
                                            ListItem.getornotbtn.setText("已領取");

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
                            HashMap<String,String> params = new HashMap<>();
                            params.put("userid",list.get(position).get("userid").toString());
                            params.put("takeornot",list.get(position).get("takeornot").toString());
                            params.put("foodid",(String) MainUtils.getGiverlist().get(FoodinfoGiver.this.position).get("foodid"));
                            Log.v("lipin",MainUtils.getGiverlist().get(FoodinfoGiver.this.position).get("foodid").toString());

                            return params;
                        }
                    };
                    MainUtils.queue.add(request);
                }
            });
            ListItem.send.setImageResource(R.drawable.ic_send_black_24dp);
            ListItem.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatRoomActivity.class);
                    startActivityForResult(intent, 0);
                }
            });

            return convertView;

        }
    }

    /**
     * 拿取user自己的卡片資訊想排隊之人的資訊
     * @return
     */
    private void getTaker() {
        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_getTaker";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonTakers(response);
                        Log.v("lipin",response);
                        listView.setAdapter(new ListViewAdapter(FoodinfoGiver.this,list));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("foodid", (String) MainUtils.getGiverlist().get(position).get("foodid"));
                Log.v("lipin",(String) MainUtils.getGiverlist().get(position).get("foodid"));

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
    List<HashMap<String, Object>>list;
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
    public void getData( int i) throws ParseException {
        //因為list加入的方式的比對方式是地址,重複的地址物件會被蓋過,所以需要每次尋訪時是產生新的hashMap,故在此new出來
        hashMap = new HashMap<String, Object>();

        //如果user有自己的大頭照,就使用user自己的大頭照
        if (row.optString("img",null)!=null){
            //取出base64的圖片
            String base64Img = row.optString("img");
            //轉成bitmap
            Bitmap bitmap = MainUtils.base64Tobitmap(base64Img);

            hashMap.put("userImage",bitmap);
        }else {
            hashMap.put("userImage", R.drawable.ic_person_24dp);
        }

        hashMap.put("orderNo",i+1);
        hashMap.put("username",row.optString("name",null)!=null?
                row.optString("name",null):row.optString("account"));

        hashMap.put("qty",row.optString("qty"));
        hashMap.put("takeornot",row.optString("takeornot"));
        hashMap.put("userid",row.optString("user_id"));


        list.add(hashMap);
    }

//    private List<HashMap<String, Object>> getData() {
//        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < 10; i++) {
//            HashMap<String, Object> hashMap = new HashMap<String, Object>();
//            hashMap.put("orderNo", i);
//            hashMap.put("userImage", R.drawable.ic_person_24dp);
//            hashMap.put("username", "王小明");
//            hashMap.put("takerwant", "想要" + i + "份");
//            list.add(hashMap);
//        }
//        return list;
//    }

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
                Intent intent = new Intent(this, AddFoodActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
