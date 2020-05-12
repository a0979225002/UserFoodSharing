package tw.org.iii.yichun.foodsharing.profile;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.org.iii.yichun.foodsharing.FragmentAdapter;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private CircleImageView profileImg;
    private TextView username;
    private ViewPager viewPager;
    private Fragment fragment1, fragment2, fragment3;
    private TabLayout tabLayout;
    private View view;
    private ArrayList views;
    private ArrayList<String> title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_img);
        tabLayout = view.findViewById(R.id.profile_tabLayout);
        viewPager = view.findViewById(R.id.profile_viewPager);
        username = view.findViewById(R.id.profile_userName);
        username.setText(User.getAccount());

        addlist();//將標題與fragment加入陣列

        setFragment();//adapter三個fragment,實施監聽

        return view;
    }

    /**
     * 將fragment與標題加入陣列中
     */
    private void addlist(){

        //新增頁面
        views = new ArrayList();

        fragment1 = new ShareHistoryFragment();
        fragment2 = new TakeHistoryFragment();
        fragment3 = new CommentFragment();

        views.add(0,fragment1);
        views.add(1,fragment2);
        views.add(2,fragment3);

        //新增標題
        title = new ArrayList();
        title.add("分享紀錄");
        title.add("索取紀錄");
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
}