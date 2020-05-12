package tw.org.iii.yichun.foodsharing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import tw.org.iii.yichun.foodsharing.Notification.CommentNoticeFragment;
import tw.org.iii.yichun.foodsharing.Notification.GiverNoticeFragment;
import tw.org.iii.yichun.foodsharing.Notification.TakerNoticeFragment;
import tw.org.iii.yichun.foodsharing.profile.CommentFragment;
import tw.org.iii.yichun.foodsharing.profile.ShareHistoryFragment;
import tw.org.iii.yichun.foodsharing.profile.TakeHistoryFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private View view;
    private ArrayList views;
    private ArrayList<String>title;
    private Fragment fragment1, fragment2, fragment3;
    private ViewPager viewPager;
    private TabLayout tabLayout;



    //private Button btn;
    private ListView lv;

    private SimpleAdapter adapter;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private String [] from = {"notification","time"};
    private int [] to = {R.id.notification_msg, R.id.notification_time};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        viewPager = view.findViewById(R.id.notification_viewPager);
        tabLayout = view.findViewById(R.id.notification_tabLayout);


        addlist();

        return view;

    }

    private void addlist() {

        //新增頁面
        views = new ArrayList();


        fragment1 = new TakerNoticeFragment();
        fragment2 = new GiverNoticeFragment();
        fragment3 = new CommentNoticeFragment();

        views.add(0, fragment1);
        views.add(1, fragment2);
        views.add(2, fragment3);

        //新增標題
        title = new ArrayList();
        title.add("分享者通知");
        title.add("索取者通知");
        title.add("評論");

    }
    /**
     * 將fragment加入適配器,進行監聽
     */
    private void setFragment() {

        tabLayout.setupWithViewPager(viewPager);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                Log.v("lipin", position + "");
                return (Fragment) views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return title.get(position);
            }
        };

        viewPager.setAdapter(pagerAdapter);

    }



//    private void findID(){
//        //btn = (Button) view.findViewById(R.id.notify); //模擬通知進來
//        lv = (ListView) view.findViewById(R.id.notification_lv);//fragment_notification中的listivew 訊息進來呈現的地方
//
//    }
//
//    /**初始化listview套用layout--listview_notification**/
//    private void initlv() {
//        adapter = new SimpleAdapter(this.getActivity(), data, R.layout.listview_notification,from, to);
//        lv.setAdapter(adapter);
//
//        /**可點擊的listview**/
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //todo: how?
//                if (position == 0){
//                    Log.v("flora","onclick");
//                }
//
//
//
//            }
//        });
//    }
//
//    /**導入listview原本資料**/
//    public LinkedList<HashMap<String,String>> getData(){
//        LinkedList<HashMap<String,String>> list = new LinkedList<>();
//        for (int i = 0; i < 10; i++){
//            HashMap<String, String> row =new HashMap<>();
//            row.put(from[0], "王小明已於愛心便當排隊");
//            row.put(from[1], "2020.04.30 08:0"+i);
//            data.add(0,row); //新通知放在最上面
//            adapter.notifyDataSetChanged();
//
//        }
//        return list;
//    }


    /**模擬通知資料進來呈現在listview **/
//    private void addNotification(){
//        btn.setOnClickListener(new View.OnClickListener() {
//            int count = 0;
//            @Override
//            public void onClick(View v) {
//                Log.v("flora","onclick");
//                HashMap<String, String> row =new HashMap<>();
//                row.put(from[0], "王小明已於愛心便當排隊");
//                row.put(from[1], "2020.04.30 08:0"+count++);
//                data.add(0,row); //新通知放在最上面
//                adapter.notifyDataSetChanged();
//
//            }
//        });
//    }








    //todo: 別人做了什麼事 就丟到這來? 情境: 1. 索取者按下排隊->通知分享者 ;
    // 2. 分享者卡片到期->通知分享者 3. 索取者卡片排隊結束-> 通知索取者
    // 4. 評分...
    // 待辦功能: 對應的通知點選要能夠到對應的頁面  推論: 所以通知送進來時，要帶page資訊以供intent過去
    // notification資料庫? 因為每次進來都要能看到歷史通知



}