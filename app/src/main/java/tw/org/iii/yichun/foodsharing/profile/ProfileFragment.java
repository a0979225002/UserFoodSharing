package tw.org.iii.yichun.foodsharing.profile;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private CircleImageView profileImg;
    private ViewPager viewPager;
    private Fragment fragment1, fragment2, fragment3;
    private TabLayout tabLayout;
    private View view;
    private ArrayList views;


    @Override
    public void onStart() {
        super.onStart();
        Log.v("yichun", "start");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_img);
        tabLayout = view.findViewById(R.id.profile_tabLayout);
        viewPager = view.findViewById(R.id.profile_viewPager);

        views = new ArrayList();

        fragment1 = new ShareHistoryFragment();
        fragment2 = new TakeHistoryFragment();
        fragment3 = new CommentFragment();

        views.add(0,fragment1);
        views.add(1,fragment2);
        views.add(2,fragment3);

        setFragment();

        return view;
    }


    private void setFragment() {

//        tabLayout.setupWithViewPager(viewPager);


        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager(),tabLayout.getTabCount()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Log.v("lipin", 123456+"::::"+position+"");

                return (Fragment) views.get(position);
            }


            @Override
            public int getCount() {
                return views.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                tabLayout.getTabAt(0).setText("分享紀錄");
                tabLayout.getTabAt(1).setText("索取紀錄");
                tabLayout.getTabAt(2).setText("評論");

                Log.v("lipin",position+"::::0000");
                return super.instantiateItem(container, position);
            }
        });

        // 禁止 viewPager 左右滑動
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        viewPager.setCurrentItem(0);

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.v("lipin", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("lipin", "onDestroy");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}