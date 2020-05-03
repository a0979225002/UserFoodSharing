package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFilterActivity extends AppCompatActivity {
    @BindView(R.id.search_foodcategory)
    AutoCompleteTextView search_foodcategory;//食物分類
    @BindView(R.id.search_city)
    AutoCompleteTextView search_city;    //縣市
    @BindView(R.id.search_dist)
    AutoCompleteTextView search_dist;    //區域
    @BindView(R.id.search_period)
    AutoCompleteTextView search_period;  //分享時間

    private String selectedCategory;//下拉選單監聽,獲得客戶端選擇的食物分類
    private String selectedCity;//下拉選單監聽,獲得客戶端選擇的城市名
    private String selectedDist;//下拉選單監聽,獲得客戶端選擇的區域名
    private String selectedPeriod;//下拉選單監聽,獲得客戶端選擇的分享時間

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        ButterKnife.bind(this);

        setToolbar();
        FoodCategoryList();//將value的值給予食物分類的下拉選單,監聽客戶端選擇的食物分類
        FoodCityList();//監聽客戶端選擇的縣市
        FoodDistList();//監聽客戶端選擇的區域
        PeriodList();//監聽客戶端選擇的時間區間

    }


    /**
     * 將 values 內 string的文字加入食物分類的欄位中
     */
    public void FoodCategoryList() {
        Resources resources = getResources();
        String[] list = resources.getStringArray(R.array.foodCategory);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        list);

        //將資源檔內的文字加入食物分類中
        search_foodcategory.setAdapter(adapter);
        search_foodcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                Log.v("flora", selectedCategory);

            }
        });


    }

    /**
     * 將values 內 string的文字加入縣市的欄位中,並監聽客戶端點選的縣市
     */
    public void FoodCityList() {
        Resources resources = getResources();
        //將values 內 string的文字加入陣列
        String[] list = resources.getStringArray(R.array.foodCity);


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        list);

        search_city.setAdapter(adapter);

        /**
         * 下拉選單監聽，selectedCity ＝ 字串（城市名）
         */
        search_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = parent.getItemAtPosition(position).toString();
                Log.v("lipin", selectedCity);

                switch (selectedCity) {
                    case "台北":
                        search_dist.setAdapter(new ArrayAdapter<String>(SearchFilterActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taipei)));
                        break;
                    case "台中":
                        search_dist.setAdapter(new ArrayAdapter<String>(SearchFilterActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taichung)));
                        break;
                    case "高雄":
                        search_dist.setAdapter(new ArrayAdapter<String>(SearchFilterActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Kouhsiung)));
                        break;
                }

                search_dist.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 監聽區域的選擇,拿取客戶端選擇的區域
     */
    public void FoodDistList() {
        search_dist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDist = parent.getItemAtPosition(position).toString();
                Log.v("lipin", selectedDist);
            }
        });
    }

    /**
     * 將 values 內 string的文字加入食物分類的欄位中
     */
    public void PeriodList() {
        Resources resources = getResources();
        String[] list = resources.getStringArray(R.array.periodCategory);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        list);

        //將資源檔內的文字加入時間分類中
        search_period.setAdapter(adapter);
        search_period.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPeriod = parent.getItemAtPosition(position).toString();
                Log.v("flora", selectedPeriod);

            }
        });


    }



    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.foodinfoTaker_toolbar);
        String title = "進階搜尋";
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //回復前頁的設定
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    //toolbar切換設定
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();//回復前頁
        }
        return super.onOptionsItemSelected(item);
    }

}
