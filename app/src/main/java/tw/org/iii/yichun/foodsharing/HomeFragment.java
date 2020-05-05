package tw.org.iii.yichun.foodsharing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.iii.yichun.foodsharing.Global.MainUtils;
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

        listView = view.findViewById(R.id.home_lv);

        if (User.getLongitude() != null) {
            getposition();//取得現在所在位置轉成文字地址
        }else {
            getfoodcard();//資料庫獲得食物資訊,並將食物資訊放在list裡面,把參數給予適配器adpader
        }


        listViewClickListener();


        gotoFilter();//去進階搜尋頁面

        return view;
    }
    /**
     * 點擊跳轉頁面
     */
    private void listViewClickListener(){
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
    }

    /**
     *  去地圖頁面
     */
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

            ListItem.image.setImageBitmap((Bitmap) data.get(position).get("image"));
            ListItem.title.setText((String)data.get(position).get("title"));
            ListItem.location.setText((String)data.get(position).get("location"));
            ListItem.deadline.setText((String)data.get(position).get("deadline"));
            ListItem.quantity.setText((String)data.get(position).get("quantity"));
            ListItem.leftQuantity.setText((String)data.get(position).get("leftQuantity"));

            return convertView;
        }
    }
    /**
     * 抓取現在客戶經緯度轉換為,拿取區域,與縣市
     */
    private void getposition(){

        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&language=zh-TW&key=AIzaSyDn1AwRWz2oQX_oOr5CobxB1PRe-Vg2eyA",
                User.getLatitude(),User.getLongitude());

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonposition(response);

                        getfoodcard();//資料庫獲得食物資訊,並將食物資訊放在list裡面,把參數給予適配器adpader

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        );
        MainUtils.queue.add(request);
    }

    /**
     * 將傳回來了json地址拉出來存取地區,縣市
     */
    private void jsonposition(String response) {
        try {
            JSONObject  root = new JSONObject(response);
            String statis = root.getString("status");
            //檢查是否為ok,google如果有拿取到正確地址會在status欄位顯示ok

            Log.v("lipin","status"+statis);

            if (statis.equals("OK")){
                JSONArray results = root.getJSONArray("results");
                JSONObject result = results.getJSONObject(0);
                JSONArray address_components = result.getJSONArray("address_components");
                JSONObject city = address_components.getJSONObject(3);
                JSONObject dist = address_components.getJSONObject(2);

                //將轉出來的縣市地址傳入權域靜態類中
                User.setCity(city.getString("short_name"));
                User.setDist(dist.getString("short_name"));

            }

        }catch (Exception e){
            Log.v("lipin",e.toString());
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
                        JsonFoodcard(response);
                        //將抓下來的參數給予建構式中的ListViewAdapter class
                        listView.setAdapter(new ListViewAdapter(getActivity(), list));
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

                //檢查有無取得地址,如果有的話,將地址傳給後端,讓顯示在首頁的不再是最新的20個資訊,而是你附近地址的資訊
                if (User.getDist()!=null&&User.getCity()!=null){
                    params.put("city",User.getCity());
                    params.put("dist",User.getDist());
                    Log.v("lipin",User.getCity()+"22222");
                    return params;
                }else {
                    return null;
                }
            }
        };
        MainUtils.queue.add(request);
    }


    /**
     * 抓取json的食物資訊
     * @param response
     */
    JSONObject row;
    HashMap<String, Object> hashMap;
    List<HashMap<String, Object>> list;
    private void JsonFoodcard(String response) {
        list = new ArrayList<HashMap<String, Object>>();

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length();i++){
                row = array.getJSONObject(i);

                getData();//將抓取的值一個一個放在listview裡面

            }

        }catch (Exception e){
            Log.v("lipin","JsonFoodcard:"+e.toString());
        }

    }

    /**
     * 食物 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public void getData(){
            //因為list加入的方式的比對方式是地址,重複的地址物件會被蓋過,所以需要每次尋訪時是產生新的hashMap,故在此new出來
            hashMap = new HashMap<String, Object>();

            //拿取拆領的數字,0 or 1
            int split = Integer.valueOf(row.optString("split"));

            //將拆領的值更改為字串
            String stringSplit = null;
            if (split == 1){
                stringSplit = "可拆領";
            }else if (split ==0){
                stringSplit = "不可拆領";
            }

            //取出base64的圖片
            String base64Img = row.optString("foodimg");
            //轉成bitmap
            Bitmap bitmap = MainUtils.base64Tobitmap(base64Img);

            hashMap.put("image",bitmap);
            hashMap.put("title", row.optString("name") );
            hashMap.put("location", row.optString("city")+row.optString("dist"));
            hashMap.put("deadline", "期限：" + row.optString("due_date"));
            hashMap.put("quantity", "可否拆領："+stringSplit);
            hashMap.put("leftQuantity", "預估剩餘："+ row.optString("qty")+ " 份");

            Log.v("lipin",hashMap.toString());
            list.add(hashMap);
         }
}
