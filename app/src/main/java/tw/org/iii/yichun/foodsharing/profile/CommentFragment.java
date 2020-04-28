package tw.org.iii.yichun.foodsharing.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));

        return view;
    }

    /**
     * 評論 ListView Adapter
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
            ListItem.comment = (TextView) convertView.findViewById(R.id.comment_comment);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.userPhoto.setImageResource((Integer) data.get(position).get("userPhoto"));
            ListItem.userName.setText((String)data.get(position).get("userName"));
            ListItem.ratingBar.setRating((Integer)data.get(position).get("ratingBar"));
            ListItem.comment.setText((String)data.get(position).get("comment"));

            return convertView;
        }
    }


    /**
     * 評論 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public List<HashMap<String, Object>> getData(){
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("userPhoto", R.drawable.profile_img);
            hashMap.put("userName", "User Name" + i);
            hashMap.put("ratingBar", i/2);
            hashMap.put("comment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean euismod bibendum laoreet. Proin gravida dolor sit amet lacus accumsan et viverra justo commodo.");
            list.add(hashMap);
        }
        return list;
    }
}
