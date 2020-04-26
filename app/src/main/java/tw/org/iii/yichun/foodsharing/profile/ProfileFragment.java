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

//        removefragment();
        setFragment();

       String ss=   viewPager.getAdapter().toString();
        Log.v("lipin",ss);
        return view;
    }

    private void removefragment() {
        //先保证ViewPager之前已设置过Adapter,这样才有可能存在缓存
        if (viewPager.getAdapter() != null) {
            //获取FragmentManager实现类的class对象,这里指的就是FragmentManagerImpl
            Class<? extends FragmentManager> aClass = getChildFragmentManager().getClass();
            try {
                //1.获取其mAdded字段
                Field f = aClass.getDeclaredField("mAdded");
                f.setAccessible(true);
                //强转成ArrayList
                ArrayList<Fragment> list = (ArrayList) f.get(getChildFragmentManager());
                //清空缓存
                list.clear();

                //2.获取mActive字段
                f = aClass.getDeclaredField("mActive");
                f.setAccessible(true);
                //强转成SparseArray
                SparseArray<Fragment> array = (SparseArray) f.get(getChildFragmentManager());
                //清空缓存
                array.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//再次设置ViewPager的Adapter





    }
    private void setFragment() {
        profileImg = view.findViewById(R.id.profile_img);

        tabLayout = view.findViewById(R.id.profile_tabLayout);
        viewPager = view.findViewById(R.id.profile_viewPager);
        tabLayout.setupWithViewPager(viewPager);

        fragment1 = new ShareHistoryFragment();
        fragment2 = new TakeHistoryFragment();
        fragment3 = new CommentFragment();

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager(),tabLayout.getTabCount()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                Log.v("lipin", 123 + "");
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