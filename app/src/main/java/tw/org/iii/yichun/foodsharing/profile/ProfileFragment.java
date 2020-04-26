package tw.org.iii.yichun.foodsharing.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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

    @Override
    public void onStart() {
        super.onStart();
        Log.v("yichun","start");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_img);

        tabLayout = view.findViewById(R.id.profile_tabLayout);
        viewPager = view.findViewById(R.id.profile_viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fragment1 = new ShareHistoryFragment();
        fragment2 = new TakeHistoryFragment();
        fragment3 = new CommentFragment();

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager(), tabLayout.getTabCount()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                tabLayout.getTabAt(0).setText("分享紀錄");
                tabLayout.getTabAt(1).setText("索取紀錄");
                tabLayout.getTabAt(2).setText("評論");
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                Log.v("yichun","" + position);
                return super.getPageTitle(position);
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

