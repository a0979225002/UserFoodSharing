package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bmView;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmView = findViewById(R.id.bottom_nav_view);
        viewPager = findViewById(R.id.view_pager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new NotificationFragment());
        fragments.add(new MessageFragment());
        fragments.add(new HomeFragment());
        fragments.add(new ProfileFragment());
        fragments.add(new ShopFragment());

        FragmentAdapter adapter = new FragmentAdapter(fragments,getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 點擊 Bottom Nav 事件
        changeBottomNav();

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.nav_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.nav_add){

                }else if (i == R.id.nav_about){

                }else if (i == R.id.nav_feedback){

                }else if (i == R.id.nav_logout){

                }

                return false;
            }
        });

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true); //隱藏 toolbar default title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //是否顯示返回鍵
        getSupportActionBar().setHomeButtonEnabled(true); // 左上圖示是否可以點擊

    }


    // 點擊 Bottom Nav 事件
    private void changeBottomNav(){
        bmView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuId = menuItem.getItemId();
                switch (menuId){
                    case R.id.tabNotification:viewPager.setCurrentItem(0);break;
                    case R.id.tabMessage:viewPager.setCurrentItem(1);break;
                    case R.id.tabHome:viewPager.setCurrentItem(2);break;
                    case R.id.tabProfile:viewPager.setCurrentItem(3);break;
                    case R.id.tabShop:viewPager.setCurrentItem(4);break;
                }
                return false;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bmView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


}


