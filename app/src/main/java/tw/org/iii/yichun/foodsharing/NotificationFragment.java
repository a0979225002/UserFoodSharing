package tw.org.iii.yichun.foodsharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.LinkedList;

import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private Button btn;
    private ListView lv;

    private SimpleAdapter adapter;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private String [] from = {"iv","tv"};
    private int [] to = {R.id.notification_msg, R.id.notification_time};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        btn = (Button) view.findViewById(R.id.notify);
        btn.setOnClickListener(new View.OnClickListener() {
            int count = 0;
            @Override
            public void onClick(View v) {
                Log.v("flora","onclick");
                HashMap<String, String> row =new HashMap<>();
                row.put(from[0], "王小明已於愛心便當排隊");
                row.put(from[1], "2020.04.30 08:0"+count++);
                data.add(0,row); //新通知放在最上面
                adapter.notifyDataSetChanged();

            }
        });

        lv = (ListView) view.findViewById(R.id.lv);
        initlv();
        return view;

    }

    private void initlv() {
        adapter = new SimpleAdapter(this.getActivity(), data, R.layout.listview_notification,from, to);
        lv.setAdapter(adapter);
    }

    //todo: 別人做了什麼事 就丟到這來? 情境: 1. 索取者按下排隊->通知分享者 ;
    // 2. 分享者卡片到期->通知分享者 3. 索取者卡片排隊結束-> 通知索取者
    // 4. 評分...
    // 待辦功能: 對應的通知點選要能夠到對應的頁面  推論: 所以通知送進來時，還要帶page資訊以供intent過去?


}
