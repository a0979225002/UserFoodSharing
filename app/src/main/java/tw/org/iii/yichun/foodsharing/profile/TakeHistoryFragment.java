package tw.org.iii.yichun.foodsharing.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeHistoryFragment extends Fragment {
    private GridView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_history, container, false);

        listView = view.findViewById(R.id.takeHistory_lv);
        sharefoodcard();

        return view;
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
         * List Item 集合，對應 listView_profile_history.xml
         */
        public final class ListItem{
            public ImageView image; // 食物照片
            public TextView title; // 食物名稱
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

            // 取得 listView_profile_history.xml
            ListItem ListItem = new ListItem();
            convertView = layoutInflater.inflate(R.layout.listview_profile_history, null);
            ListItem.image = (ImageView) convertView.findViewById(R.id.profile_foodList_img);
            ListItem.title = (TextView) convertView.findViewById(R.id.profile_foodList_title);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.image.setImageBitmap((Bitmap) data.get(position).get("image"));
            ListItem.title.setText((String)data.get(position).get("title"));

            return convertView;
        }
    }
    /**
     * 撈已索取完畢的食物
     */
    private void sharefoodcard(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/getRequestRecord";
        StringRequest request =  new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonFoodcard(response);

                        listView.setAdapter(new ListViewAdapter(getActivity(),list));
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
                params.put("userid", User.getId());
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
    private void JsonFoodcard(String response) {

        list = new ArrayList<HashMap<String, Object>>();

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                row = array.getJSONObject(i);

                getData(list);//將抓取的值一個一個放在listview裡面

            }

        } catch (Exception e) {
            Log.v("lipin", "JsonFoodcard:" + e.toString());
        }

    }

    /**
     * 食物 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public void getData( List<HashMap<String, Object>>list) throws ParseException {
        //因為list加入的方式的比對方式是地址,重複的地址物件會被蓋過,所以需要每次尋訪時是產生新的hashMap,故在此new出來
        hashMap = new HashMap<String, Object>();
        //拿取拆領的數字,0 or 1
        int split = Integer.valueOf(row.optString("split"));

        //將拆領的值更改為字串
        String stringSplit = null;
        if (split == 1) {
            stringSplit = "可拆領";
        } else if (split == 0) {
            stringSplit = "不可拆領";
        }
        //將sql回來的截止時間,優化顯示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(row.optString("due_date"));
        String dueDate = format.format(date);

        //取出base64的圖片
        String base64Img = row.optString("foodimg");
        //轉成bitmap
        Bitmap bitmap = MainUtils.base64Tobitmap(base64Img);

        //直接將資料顯示在首頁資訊

        hashMap.put("image", bitmap);
        hashMap.put("title", row.optString("name"));

        //下面東東目的,當客戶端點擊後進入詳細資料時可查看
        hashMap.put("city", row.optString("city"));
        hashMap.put("dist", row.optString("dist"));
        hashMap.put("deadline", dueDate);
        hashMap.put("quantity", stringSplit);
        hashMap.put("leftQuantity", row.optString("qty"));
        hashMap.put("account", row.optString("account"));
        hashMap.put("username", row.optString("username"));
        hashMap.put("address", row.optString("address"));
        hashMap.put("detail", row.optString("detail"));
        hashMap.put("category", row.optString("category"));
        hashMap.put("tag", row.optString("tag"));
        hashMap.put("token", row.optString("token"));
        hashMap.put("foodid",row.optString("id"));
        hashMap.put("status",row.optString("status"));

        list.add(hashMap);
    }
}
