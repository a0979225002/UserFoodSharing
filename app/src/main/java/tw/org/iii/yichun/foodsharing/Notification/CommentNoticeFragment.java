package tw.org.iii.yichun.foodsharing.Notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.R;
import tw.org.iii.yichun.foodsharing.profile.CommentFragment;
import tw.org.iii.yichun.foodsharing.profile.ShareHistoryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentNoticeFragment extends Fragment {
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_notice,container,false);

        listView = view.findViewById(R.id.notification_comment);

        listView.setAdapter(new ListViewAdapter(getActivity(),MainUtils.getList()));

        // Inflate the layout for this fragment
        return view;
    }

    private class ListViewAdapter extends BaseAdapter{
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
     * 拿取以拿取完畢狀態,由發布者給予
     */
    private void getfoodEnd(){
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
