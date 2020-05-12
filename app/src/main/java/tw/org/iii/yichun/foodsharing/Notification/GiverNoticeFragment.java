package tw.org.iii.yichun.foodsharing.Notification;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.R;

public class GiverNoticeFragment extends Fragment {
    private ListView listView;
    private String [] from = {"notification","time"};
    private int [] to = {R.id.notification_msg, R.id.notification_time};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_giver_notice, container, false);


        listView = view.findViewById(R.id.notification_comment);

        listView.setAdapter(new ListViewAdapter(getActivity(), MainUtils.getList()));

        return view;
    }




    private class ListViewAdapter extends BaseAdapter {
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

            return convertView;
        }
    }
    /**
     * 拿取
     */

    /**導入listview原本資料**/
    LinkedList<HashMap<String,String>> list;
    public LinkedList<HashMap<String,String>> getData(){
        list = new LinkedList<>();
        for (int i = 0; i < 10; i++){
            HashMap<String, String> row =new HashMap<>();
            row.put(from[0], "王小明已於愛心便當排隊");
            row.put(from[1], "2020.04.30 08:0"+i);
            list.add(0,row); //新通知放在最上面

        }
        return list;
    }
    /**
     * 拿取能讓你來排隊的通知
     */
    private void getGiverqueue(){
        String url = "";
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
                return super.getParams();
            }
        };
        MainUtils.queue.add(request);
    }
}
