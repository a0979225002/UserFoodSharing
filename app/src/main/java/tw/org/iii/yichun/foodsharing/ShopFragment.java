package tw.org.iii.yichun.foodsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        listView = view.findViewById(R.id.shop_lv);
        List<HashMap<String,Object>> list = getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                useDiscount();
            }
        });

        return view;
    }


    public void useDiscount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.dialog_qrcode, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        btn = view.findViewById(R.id.qrcode_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

            // 取得 listView_store.xml
            ListItem ListItem = new ListViewAdapter.ListItem();
            convertView = layoutInflater.inflate(R.layout.listview_store, null);
            ListItem.storeImg = (ImageView) convertView.findViewById(R.id.storeLv_storeImg);
            ListItem.storeName = (TextView) convertView.findViewById(R.id.storeLv_storeName);
            ListItem.discountInfo = (TextView) convertView.findViewById(R.id.storeLv_discountInfo);
            ListItem.discount = (TextView) convertView.findViewById(R.id.storeLv_discount);
            convertView.setTag(ListItem);

//             綁定資料
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

        Object[] storeImg = new Object[10];
        storeImg[0] = R.drawable.store_logo_1;
        storeImg[1] = R.drawable.store_logo_2;
        storeImg[2] = R.drawable.store_logo_3;
        storeImg[3] = R.drawable.store_logo_4;
        storeImg[4] = R.drawable.store_logo_5;
        storeImg[5] = R.drawable.store_logo_6;
        storeImg[6] = R.drawable.store_logo_7;
        storeImg[7] = R.drawable.store_logo_8;
        storeImg[8] = R.drawable.store_logo_9;
        storeImg[9] = R.drawable.store_logo_10;

        String[] storeName = new String[10];
        storeName[0] = "統一超商";
        storeName[1] = "全家便利商店";
        storeName[2] = "麥當勞";
        storeName[3] = "萊爾富超商";
        storeName[4] = "屈臣氏";
        storeName[5] = "Cama";
        storeName[6] = "路易莎咖啡";
        storeName[7] = "家樂福";
        storeName[8] = "鬍鬚張";
        storeName[9] = "輕井澤鍋物";

        for (int i = 0; i < 10; i++){
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("storeImg", storeImg[i]);
            hashMap.put("storeName", storeName[i]);
            hashMap.put("discountInfo", "可於實體店面消費折抵");
            hashMap.put("discount", (int)(Math.random()*10)*10 + " - " + (int)(Math.random()*100)*10 + " 點");
            list.add(hashMap);
        }
        return list;
    }
}
