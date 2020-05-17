package tw.org.iii.yichun.foodsharing.profile;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.org.iii.yichun.foodsharing.FragmentAdapter;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private CircleImageView profileImg;
    private TextView username,userfraction,Total_score;
    private ViewPager viewPager;
    private Fragment fragment1, fragment2, fragment3;
    private TabLayout tabLayout;
    private View view;
    private ArrayList views;
    private ArrayList<String> title;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImg = view.findViewById(R.id.profile_img);
        tabLayout = view.findViewById(R.id.profile_tabLayout);
        viewPager = view.findViewById(R.id.profile_viewPager);
        userfraction = view.findViewById(R.id.profile_score);//user平均分數
        ratingBar = view.findViewById(R.id.comment_ratingBar);//評價星星
        Total_score = view.findViewById(R.id.Total_score);//user總分
        username = view.findViewById(R.id.profile_userName);
        username.setText(User.getAccount());
        addlist();//將標題與fragment加入陣列

        getUserFraction();

        setFragment();//adapter三個fragment,實施監聽

        return view;
    }

    /**
     * 拿取該user完成交易的比數
     */
    private void getTransactionRatio(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/getTransactionRatio";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getcount(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("userid",User.getId());
                return params;
            }
        };
        MainUtils.queue.add(request);
    }
    private void getcount(String response){
        int count = Integer.valueOf(response.trim());//拿取user已交易完成的比數
        float Total = Float.valueOf((String) Total_score.getText());//拿取user的現在總分數轉int
        float newFraction = Total/count/10;
        ratingBar.setStepSize(0.25f);
        ratingBar.setRating(newFraction);//將分數顯示在星星中
        userfraction.setText(newFraction+"");//將平均分顯示出來



    }

    /**
     * 拿取user自己的總評分
     */
    private void getUserFraction(){
        String url = "http://"+ Utils.ip +"/FoodSharing_war/getUserFraction";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonUserFraction(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipin",error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("userid",User.getId());
                return params;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 拿取json出來的user自己的總評分
     * @param respone
     */
    private void JsonUserFraction(String respone) {
        Log.v("lipin",respone);
        try {
            JSONArray array = new JSONArray(respone);
            JSONObject jsonObject = array.getJSONObject(0);
            //如果抓不到user的分數,將給予0分顯示
             if (jsonObject.optString("fraction",null).equals("0")){
                 ratingBar.setRating(0);
                 userfraction.setText("0.0");
                 Total_score.setText("0");
             }else if (!jsonObject.optString("fraction",null).equals("0")){
                Total_score.setText(jsonObject.optString("fraction"));
                 getTransactionRatio();
             }
        }catch (Exception e){
        }
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

        // 禁止 viewPager 左右滑動


        viewPager.setAdapter(pagerAdapter);

    }
}