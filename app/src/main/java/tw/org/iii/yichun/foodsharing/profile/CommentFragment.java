package tw.org.iii.yichun.foodsharing.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        listView = view.findViewById(R.id.comment_lv);

        getUserComment();

        return view;
    }

    /**
     * 評論 ListView Adapter
     */
    private class ListViewAdapter extends BaseAdapter {

        private List<HashMap<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public ListViewAdapter(Context context, List<HashMap<String, Object>> data){
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        public void removeItem(int index){
            data.remove(index);
        }

        /**
         * List Item 集合，對應 listView_comment.xml
         */
        public final class ListItem{
            public CircleImageView userPhoto;
            public TextView userName, comment; // 使用者名稱
            public RatingBar ratingBar; // 評分
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

            // 取得 listView_comment.xml
            ListItem ListItem = new ListItem();
            convertView = layoutInflater.inflate(R.layout.listview_comment, null);
            ListItem.userPhoto = (CircleImageView) convertView.findViewById(R.id.comment_userPhoto);
            ListItem.userName = (TextView) convertView.findViewById(R.id.comment_userName);
            ListItem.ratingBar = (RatingBar) convertView.findViewById(R.id.comment_ratingBar);
            ListItem.ratingBar.setStepSize(0.5f);
            ListItem.comment = (TextView) convertView.findViewById(R.id.comment_comment);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.userPhoto.setImageResource((Integer) data.get(position).get("userPhoto"));
            ListItem.userName.setText((String)data.get(position).get("account"));
            ListItem.ratingBar.setRating(Float.valueOf(data.get(position).get("tokerfraction").toString()));
            ListItem.comment.setText((String)data.get(position).get("comment"));

            return convertView;
        }
    }
    /**
     *
     */
    private void ListviewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //拿取自訂的dialog
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View newview = inflater.inflate(R.layout.mydialog5, null);
                //拿取自訂dialog的ID
                ImageView imag = newview.findViewById(R.id.foodList_img);
                TextView foodname = newview.findViewById(R.id.foodList_title);
                TextView dueDate = newview.findViewById(R.id.foodList_deadline);
                TextView split = newview.findViewById(R.id.foodList_quantity);
                EditText addfoodcomment = newview.findViewById(R.id.addfoodcomment);
                RatingBar ratingBar = newview.findViewById(R.id.comment_ratingBar);

                imag.setImageBitmap((Bitmap) list.get(position).get("image"));
                foodname.setText((String) list.get(position).get("title"));
                dueDate.setText("索取人:" + (String)list.get(position).get("account"));
                split.setText("已索取數量:" + (String) list.get(position).get("qty"));
                addfoodcomment.setText((String)list.get(position).get("comment"));
                ratingBar.setStepSize(0.5f);
                ratingBar.setRating(Float.valueOf(list.get(position).get("tokerfraction").toString()));

                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("查看評論")
                        .setView(newview)
                        .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dialog.show();
            }
        });
    }
    /**
     * 拿取自己的食物已經拿到的評論
     */
    ListViewAdapter listViewAdapter;
    private void getUserComment(){
        String url = "http://"+ Utils.ip+"/FoodSharing_war/getUserComment";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("lipin",response);
                        JsonFoodcard(response);
                        listViewAdapter = new ListViewAdapter(getActivity(),list);
                        listView.setAdapter(listViewAdapter);
                        ListviewListener();
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
        hashMap.put("createtime",row.optString("createtime"));
        hashMap.put("userid",row.optString("user_id"));
        hashMap.put("qty",row.optString("takerqty"));
        hashMap.put("modified",row.optString("modified"));
        hashMap.put("comment",row.optString("comment"));
        hashMap.put("userPhoto", R.drawable.profile_img);
        hashMap.put("tokerfraction",row.optString("tokerfraction"));
        list.add(hashMap);
    }




//    /**
//     * 評論 ListView getData
//     */
//    // TODO: 2020/4/27 撈資料庫資料
//    public List<HashMap<String, Object>> getData(){
//        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < 10; i++){
//            HashMap<String, Object> hashMap = new HashMap<String, Object>();
//            hashMap.put("userPhoto", R.drawable.profile_img);
//            hashMap.put("userName", "User Name" + i);
//            hashMap.put("ratingBar", i/2);
//            hashMap.put("comment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean euismod bibendum laoreet. Proin gravida dolor sit amet lacus accumsan et viverra justo commodo.");
//            list.add(hashMap);
//        }
//        return list;
//    }
}
