package tw.org.iii.yichun.foodsharing;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.org.iii.yichun.foodsharing.profile.CommentFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {
    private ListView listView;
    private MaterialButton btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        listView = view.findViewById(R.id.shop_lv);
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));

        btn = view.findViewById(R.id.useDiscount);
        //btn.setOnClickListener(new );

        return view;
    }

//    private View.OnClickListener myClickListener(){
//
//    }

    public void useDiscount(){

    }

    /**
     * 商店 ListView Adapter
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
         * List Item 集合，對應 listView_store.xml
         */
        public final class ListItem{
            public ImageView storeImg; // 商店圖片
            public TextView storeName; // 商店名稱
            public TextView discountInfo; // 折扣說明
            private TextView discount; // 折扣點數
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
            ListItem ListItem = new ListViewAdapter.ListItem();
            convertView = layoutInflater.inflate(R.layout.listview_store, null);
            ListItem.storeImg = (ImageView) convertView.findViewById(R.id.storeLv_storeImg);
            ListItem.storeName = (TextView) convertView.findViewById(R.id.storeLv_storeName);
            ListItem.discountInfo = (TextView) convertView.findViewById(R.id.storeLv_discountInfo);
            ListItem.discount = (TextView) convertView.findViewById(R.id.storeLv_discount);
            convertView.setTag(ListItem);

            // 綁定資料
            ListItem.storeImg.setImageResource((Integer) data.get(position).get("storeImg"));
            ListItem.storeName.setText((String)data.get(position).get("storeName"));
            ListItem.discountInfo.setText((String)data.get(position).get("discountInfo"));
            ListItem.discount.setText((String)data.get(position).get("discount"));

            return convertView;
        }
    }


    /**
     * 商店 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public List<HashMap<String, Object>> getData(){
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 10; i++){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("storeImg", R.drawable.ic_shop_24dp);
            hashMap.put("storeName", "特力屋購物網" + i);
            hashMap.put("discountInfo", "可於線上消費折抵");
            hashMap.put("discount", "10-100點");
            list.add(hashMap);
        }
        return list;
    }
}
