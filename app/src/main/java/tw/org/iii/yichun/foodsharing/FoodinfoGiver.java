package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

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
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(this, list));

        setToolbar();//設定tool bar

    }

    public class ListViewAdapter extends BaseAdapter{
        private List<HashMap<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public ListViewAdapter(Context context, List<HashMap<String, Object>> data){
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        /**
         * HomeList Item 集合，對應 listView_foodinfogiver_takerlist.xml
         */
        public final class ListItem{
            public ImageView userImage, send; // 食物照片
            public TextView orderNo, username, takerwant;
            public Button getornotbtn;
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
            ListItem.username = (TextView) convertView.findViewById(R.id.takerlist_username);
            ListItem.takerwant = (TextView) convertView.findViewById(R.id.takerlist_takerwant);
            ListItem.getornotbtn = (Button) convertView.findViewById(R.id.takerlist_getornotbtn);
            ListItem.send = (ImageView) convertView.findViewById(R.id.takerlist_send);
            convertView.setTag(ListItem);

            // 綁定資料 (todo: 資料data.get(position).get("key")無法成功，先寫死，刻出版面)
            ListItem.orderNo.setText("1");
            ListItem.userImage.setImageResource(R.drawable.ic_person_24dp);
            ListItem.username.setText("王小明");
            ListItem.takerwant.setText("想要10份");
            ListItem.getornotbtn.setText("已領取");
            //todo:按鈕點選後要改變樣式 未領取/已領取
            ListItem.getornotbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FoodinfoGiver.this,
                            ListItem.getornotbtn.getText().toString(),
                            Toast.LENGTH_SHORT).show();
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


    private List<HashMap<String, Object>> getData() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderNo", i);
            hashMap.put("userImage", R.drawable.ic_person_24dp);
            hashMap.put("username", "王小明");
            hashMap.put("takerwant", "想要"+i+"份");
            list.add(hashMap);
        }
        return list;
    }

    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoGiver_toolbar);

        String title = "愛心便當"; // 食物名稱

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //回復前頁的設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu,menu); //設定toolbar編輯按鈕

        return true;
    }

    //toolbar切換設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish(); return true; //回復前頁
            case R.id.menu_edit: //去編輯頁面
                Intent intent = new Intent(this, AddFoodActivity.class);
                startActivityForResult(intent,0); return true;
            default:return super.onOptionsItemSelected(item);
        }

    }
}
