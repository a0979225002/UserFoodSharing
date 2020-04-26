package tw.org.iii.yichun.foodsharing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;

public class AddFoodActivity extends AppCompatActivity {
    private TextInputEditText datetime;
    private Button datePickerBtn;
    private ImageView foodimg;
    private File sdroot;
    private AutoCompleteTextView addFoodCity, addFoodDist,addFoodCategory;
    private String selectedCity;//下拉選單監聽,城市名
    private String selectedDist;//下拉選單監聽,區域名
    private String selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);


        FindID();//findbyid 拉出去寫

        camera();//點擊相片給予重新拍照,將會面引導去拍照頁面

        FoodCategoryList();//將value的值給予給予食物分類的下拉選單

        FoodCityList();//監聽客戶端選擇的縣市

        FoodDistList();//監聽客戶端選擇的區域


    }

    private void FindID(){
        datetime = findViewById(R.id.addFood_datetime);//截止時間
        datePickerBtn = findViewById(R.id.datePickerBtn);//截止時間按鈕
        addFoodCity = findViewById(R.id.addFood_city);//縣市輸入
        addFoodDist = findViewById(R.id.addFood_dist);//區域輸入
        addFoodCategory = findViewById(R.id.addFood_category);//食物分類選擇
        foodimg = findViewById(R.id.addFood_img);//照片位置

        addFoodCity.setDropDownHeight(400);//給予下拉選單寬度
        addFoodDist.setDropDownHeight(400);
    }


    /**
     *點擊相片給予重新拍照,將會面引導去拍照頁面
     */
    private void camera(){
        sdroot = Environment.getExternalStorageDirectory();//拿取sd卡路徑
        Bitmap bmp = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/iii02.jpg");//將照片存放sd卡中,並加入照片名
        foodimg.setImageBitmap(bmp);//拿取存放的照片


        foodimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFoodActivity.this, MyCameraActivity.class);
                startActivity(intent);
            }
        });
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

        //將資源欓內的文字加入食物分類中
        addFoodCategory.setAdapter(adapter);

        addFoodCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                Log.v("lipin",selectedCategory);
            }
        });



    }

    /**
     *將values 內 string的文字加入縣市的欄位中,並監聽客戶端點選的縣市
     */
    public void FoodCityList() {
        Resources resources = getResources();
        //將values 內 string的文字加入陣列
        String[] list = resources.getStringArray(R.array.foodCity);

        //將陣列的文字加入layout中
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        list);

        addFoodCity.setAdapter(adapter);

        /**
         * 下拉選單監聽，selectedCity ＝ 字串（城市名）
         */
        addFoodCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = parent.getItemAtPosition(position).toString();
                Log.v("lipin", selectedCity);

                switch (selectedCity) {
                    case "台北":
                        addFoodDist.setAdapter(new ArrayAdapter<String>(AddFoodActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taipei)));
                        break;
                    case "台中":
                        addFoodDist.setAdapter(new ArrayAdapter<String>(AddFoodActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taichung)));
                        break;
                }

                addFoodDist.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 監聽區域的選擇,拿取客戶端選擇的區域
     */
    public void FoodDistList(){
        addFoodDist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDist = parent.getItemAtPosition(position).toString();
                Log.v("lipin",selectedDist);
            }
        });
    }




    public void showDatetimePicker(){
            DatePickerDialog dialog = new DatePickerDialog(this,
                    DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    datetime.setText(year + "/" + (month+1) + "/" + dayOfMonth);
                }
            },
                    2020, 4 - 1, 12);

            DatePicker picker = dialog.getDatePicker();
            Calendar limit = Calendar.getInstance();
            limit.set(2020,3,12);

            picker.setMinDate(limit.getTimeInMillis());

            dialog.show();
    }

    public void toAddFoodPreview(View view) {

    }

    /**
     * 截止時間按鈕
     */
    public void datePickerBtn(View view) {
        showDatetimePicker();
    }
}

