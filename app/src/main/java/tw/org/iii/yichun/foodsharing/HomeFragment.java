package tw.org.iii.yichun.foodsharing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.profile.ShareHistoryFragment;

public class HomeFragment extends Fragment {
    private ListView listView;
    private ImageView selectmap,filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        selectmap = view.findViewById(R.id.selectmap);
        gotomap();
        filter = view.findViewById(R.id.filter);
        gotoFilter();

        listView = view.findViewById(R.id.home_lv);
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //做出畫面-點擊第0個，跳到該卡片詳細資料頁面 (Giver視角)
                if (position == 0){
                    Intent intent = new Intent(view.getContext(), FoodinfoGiver.class);
                    startActivityForResult(intent, 0);
                }//做出畫面-點擊第1個，跳到該卡片詳細資料頁面 (Taker視角)
                if (position == 1){
                    Intent intent = new Intent(view.getContext(), FoodinfoTaker.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        return view;
    }

    //去地圖頁面  todo: 有錯誤，要判斷homemapfragment活在哪裡
    private void gotomap(){
        selectmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), tw.org.iii.yichun.foodsharing.GoogleMap.Map.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    //去進階搜尋
    private void gotoFilter(){
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchFilterActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }



    /**
     * 食物 ListView Adapter
     */
    public class ListViewAdapter extends BaseAdapter {

        private List<HashMap<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public ListViewAdapter(Context context, List<HashMap<String, Object>> data){
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }


        /**
         * HomeList Item 集合，對應 listView_home.xml
         */
        public final class ListItem{
            public ImageView image; // 食物照片
            public TextView title, location, deadline, quantity, leftQuantity;
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
            convertView = layoutInflater.inflate(R.layout.listview_home, null);
            ListItem.image = (ImageView) convertView.findViewById(R.id.foodList_img);
            ListItem.title = (TextView) convertView.findViewById(R.id.foodList_title);
            ListItem.location = (TextView) convertView.findViewById(R.id.foodList_location);
            ListItem.deadline = (TextView) convertView.findViewById(R.id.foodList_deadline);
            ListItem.quantity = (TextView) convertView.findViewById(R.id.foodList_quantity);
            ListItem.leftQuantity = (TextView) convertView.findViewById(R.id.foodList_leftQuantity);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.image.setImageResource((Integer)data.get(position).get("image"));
            ListItem.title.setText((String)data.get(position).get("title"));
            ListItem.location.setText((String)data.get(position).get("location"));
            ListItem.deadline.setText((String)data.get(position).get("deadline"));
            ListItem.quantity.setText((String)data.get(position).get("quantity"));
            ListItem.leftQuantity.setText((String)data.get(position).get("leftQuantity"));

            return convertView;
        }
    }
    /**
     * 抓取sql內的食物資訊
     */
    private void getfoodcard(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/Sql_getAddFoodCard";

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
                        Log.v("lipin",error.toString());
                    }
                }
        ){
            //拿取user當前地址,如果抓不到user當前位置就抓取整個foodcard表單
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                if (User.getDist()!=null){
                    //傳回地址,
                }
                return null;
            }
        };

    }

    /**
     * 食物 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public List<HashMap<String, Object>> getData(){
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("image", R.drawable.foodimg);
            hashMap.put("title", "食物名稱" + i);
            hashMap.put("location", "台中市南屯區" + i);
            hashMap.put("deadline", "期限：" + "2020/05/27 20:00");
            hashMap.put("quantity", "份數：" + i + " 份，可拆領");
            hashMap.put("leftQuantity", "預估剩餘：" + i + " 份");
            list.add(hashMap);
        }
        return list;
    }
}
