package tw.org.iii.yichun.foodsharing.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tw.org.iii.yichun.foodsharing.R;

public class ShareHistoryFragment extends Fragment {
    private GridView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_share_history, container, false);
        listView = view.findViewById(R.id.shareHistory_lv);
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));

        return view;
    }


    /**
     * 食物 ListView Adapter
     */
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
         * HomeList Item 集合，對應 listView_home.xml
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
            ListItem.image.setImageResource((Integer)data.get(position).get("image"));
            ListItem.title.setText((String)data.get(position).get("title"));

            return convertView;
        }
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
            list.add(hashMap);
        }
        return list;
    }
}

