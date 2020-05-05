package tw.org.iii.yichun.foodsharing;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import java.io.File;
import java.text.SimpleDateFormat;
import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Item.AddFood;


/**
 * 新增食物卡片畫面
 */
public class AddFoodActivity extends AppCompatActivity {
    @BindView(R.id.addFood_img)
    ImageView addFoodImg;                //加入照片
    @BindView(R.id.addFood_category)
    AutoCompleteTextView addFoodCategory;//食物分類
    @BindView(R.id.addFood_top)
    LinearLayout addFoodTop;             //此頁layout
    @BindView(R.id.addFood_city)
    AutoCompleteTextView addFoodCity;    //縣市
    @BindView(R.id.addFood_dist)
    AutoCompleteTextView addFoodDist;    //區域
    @BindView(R.id.address)
    TextInputEditText address;           //地址
    @BindView(R.id.addFood_datetime)
    TextInputEditText addFoodDatetime;   //加入按鈕選擇的截止時間
    @BindView(R.id.addFood_tag)
    TextInputEditText addFoodTag;        //標籤,查詢用
    @BindView(R.id.addFood_amount)
    TextInputEditText addFoodAmount;     //數量
    @BindView(R.id.share_it)
    MaterialCheckBox shareIt;            //選擇是否拆分分享
    @BindView(R.id.addFood_memo)
    TextInputEditText addFoodMemo;       //備註
    @BindView(R.id.addFood_name)
    TextInputEditText addFoodName;       //食物名
    @BindView(R.id.allview)
    LinearLayout allview;                //整個頁面


    private File sdroot;//獲得加入照片的位置

    private String selectedCity;//下拉選單監聽,獲得客戶端選擇的城市名
    private String selectedDist;//下拉選單監聽,獲得客戶端選擇的區域名
    private String selectedCategory;//下拉選單監聽,獲得客戶端選擇的食物分類
    private boolean getshareIt;//取得客戶端是否有有打勾
    private Bitmap bitmap;
    private Snackbar snackbar;
    private Intent intent,getintent;
    private AddFood addFood;
    private String Imgname;
    private String merge_arrdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        menuHeight();//更改下拉選單顯示的長度

        getAddFood();//拿取客戶端寫的欄位資料

        FoodCategoryList();//將value的值給予食物分類的下拉選單,監聽客戶端選擇的食物分類

        CheckBox();//監聽客戶端是否有打勾

        FoodCityList();//監聽客戶端選擇的縣市

        FoodDistList();//監聽客戶端選擇的區域

        addFood = new AddFood();

        camera();//點擊相片給予重新拍照,將會面引導去拍照頁面

//        addFoodImg.setImageResource(R.drawable.ic_add_a_photo_grey_24dp); //預設相機圖示

        dismissBar();

    }

    /**
     * 讓整頁擁有關掉snackbar的功能
     */


    /**
     * 從其他頁面回來可以拿回原本已寫好的值
     */
    private void getAddFood(){
        getintent = getIntent();
        addFood = (AddFood) getIntent().getSerializableExtra("savefood");
        if (addFood != null) {

            Imgname = addFood.getAddFoodImg();

            addFoodCategory.setText(addFood.getAddFoodCategory());
            selectedCategory = addFood.getAddFoodCategory();

            addFoodCity.setText(addFood.getAddFoodCity());
            selectedCity = addFood.getAddFoodCity();

            addFoodDist.setText(addFood.getAddFoodDist());
            selectedDist = addFood.getAddFoodDist();

            address.setText(addFood.getAddress());

            addFoodDatetime.setText(addFood.getAddFoodDatetime());

            addFoodTag.setText(addFood.getAddFoodTag());

            addFoodAmount.setText(addFood.getAddFoodAmount());

            shareIt.setChecked(addFood.isShareIt());

            addFoodMemo.setText(addFood.getAddFoodMemo());

            addFoodName.setText(addFood.getAddFoodName());

            Log.v("lipin",addFood.isShareIt()+"123");
        }
    }

    /**
     * 將所有客戶端輸入值的存成物件保存,（目的）跳轉後返回還是原來的值
     */
    private void SaveAddFood(){

        addFood.setAddFoodImg(Imgname);
        addFood.setAddFoodCategory(selectedCategory);
        addFood.setAddFoodCity(selectedCity);
        addFood.setAddFoodDist(selectedDist);
        addFood.setMerge_arrdress(merge_arrdress);

        addFood.setAddress(address.getText().toString().trim());
        addFood.setAddFoodDatetime(addFoodDatetime.getText().toString());
        addFood.setAddFoodTag(addFoodTag.getText().toString());
        addFood.setAddFoodAmount(addFoodAmount.getText().toString());
        addFood.setShareIt(shareIt.isChecked());
        addFood.setAddFoodMemo(addFoodMemo.getText().toString());
        addFood.setAddFoodName(addFoodName.getText().toString());



        Log.v("lipin","123"+getshareIt);
        Log.v("lipin","123"+addFoodName.getText().toString());
    }


    /**
     * 讓整頁擁有關掉snackbar的功能
     */
    private void dismissBar(){
        allview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                snackbar.dismiss();
            }
        });
    }

    /**
     * 更改下拉選單顯示的長度
     */
    private void menuHeight() {

        addFoodCity.setDropDownHeight(400);//給予下拉選單寬度
        addFoodDist.setDropDownHeight(400);
    }

    /**
     * 監聽客戶端是否有打勾分享食物
     */
    private void CheckBox() {
        shareIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getshareIt = shareIt.isChecked();
            }
        });

    }


    /**
     * 點擊相片給予重新拍照,將會面引導去拍照頁面
     */
    private void camera() {
        sdroot = Environment.getExternalStorageDirectory();//拿取sd卡路徑

        Log.v("lipin",Imgname+"地址");

        if (Imgname != null) {

            bitmap = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/" + Imgname);//拿出sd卡位置的圖片
            addFoodImg.setImageBitmap(bitmap);//拿取存放的照片

            Log.v("lipin", "bitmap" + bitmap);
        }

        //點擊照片回到照相頁面
        addFoodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddFoodActivity.this, MyCameraActivity.class);

                SaveAddFood();//將所有客戶端輸入值的存成物件保存,（目的）跳轉後返回還是原來的值

                intent.putExtra("savefood",addFood);

                startActivityForResult(intent,321);
                finish();
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

        //將資源檔內的文字加入食物分類中
        addFoodCategory.setAdapter(adapter);

        addFoodCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                Log.v("lipin", selectedCategory);

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
                    case "台北市":
                        addFoodDist.setAdapter(new ArrayAdapter<String>(AddFoodActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taipei)));
                        break;
                    case "台中市":
                        addFoodDist.setAdapter(new ArrayAdapter<String>(AddFoodActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Taichung)));
                        break;
                    case "高雄市":
                        addFoodDist.setAdapter(new ArrayAdapter<String>(AddFoodActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                getResources().getStringArray(R.array.foodCity_Kouhsiung)));
                        break;
                }

                addFoodDist.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 監聽區域的選擇,拿取客戶端選擇的區域
     */
    public void FoodDistList() {
        addFoodDist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDist = parent.getItemAtPosition(position).toString();
                Log.v("lipin", selectedDist);
            }
        });
    }

    /**
     * 顯示選取時間的dialog
     */
    public void showDatetimePicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.ALL)//樣式
                .setCallBack(new OnDateSetListener() {//監聽客戶點擊確認後的時間
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String duedate = simpleDateFormat.format(millseconds);
                        addFoodDatetime.setText(duedate);

                        Log.v("lipin", timePickerView + ":::::" + duedate);
                    }
                })
                .setTitleStringId("請輸入截止時間")
                .setThemeColor(getResources().getColor(R.color.colorSecondary))//樣式顏色
                .setWheelItemTextSelectorColor(Color.BLACK)//字體顏色
                .build();

        timePickerDialog.show(getSupportFragmentManager(), "all");

    }


    /**
     * 預覽按鈕
     * @param view
     */
    public void toAddFoodPreview(View view) {
        verify();

    }

    /**
     * 判斷是否有欄位空值
     */
    private void verify() {
        Log.v("lipin",bitmap+":圖片");
        if (bitmap != null
                && !addFoodAmount.getText().toString().trim().isEmpty()
                && !addFoodCity.getText().toString().isEmpty()
                && !addFoodDist.getText().toString().isEmpty()
                && !address.getText().toString().trim().isEmpty()
                && !addFoodCategory.getText().toString().isEmpty()
                && !addFoodDatetime.getText().toString().isEmpty()
                && !addFoodName.getText().toString().trim().isEmpty()) {

            merge_arrdress = selectedCity+" "+ selectedDist+" "+ address.getText().toString();//將縣市區域地址整合成一條字串

            intent = new Intent(this, newPreview.class);
            SaveAddFood();
            intent.putExtra("savefood",addFood);
            startActivityForResult(intent,321);
            finish();

        } else if (bitmap == null
                || addFoodAmount.getText().toString().trim().isEmpty()
                || addFoodCity.getText().toString().isEmpty()
                || addFoodDist.getText().toString().isEmpty()
                || address.getText().toString().trim().isEmpty()
                || addFoodCategory.getText().toString().isEmpty()
                || addFoodDatetime.getText().toString().isEmpty()
                || addFoodName.getText().toString().trim().isEmpty()) {

            if (bitmap == null) {
                Log.v("lipin","123");
                snackbar = Snackbar.make(allview, "發布時須給予照片", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }

            if (addFoodAmount.getText().toString().trim().isEmpty())
                addFoodAmount.setError("數量欄位不能為空");
            if (addFoodCity.getText().toString().isEmpty())
                addFoodCity.setError("請選擇縣市");
            if (addFoodDist.getText().toString().isEmpty())
                addFoodDist.setError("請選擇區域");
            if (address.getText().toString().trim().isEmpty())
                address.setError("地址欄位不能為空");
            if (addFoodCategory.getText().toString().isEmpty())
                addFoodCategory.setError("請選擇食物類型");
            if (addFoodDatetime.getText().toString().isEmpty()) {
                snackbar = Snackbar.make(allview, "請選擇截止日期", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (addFoodName.getText().toString().trim().isEmpty())
                addFoodName.setError("食物名稱不能為空");

            Log.v("lipin","截止日期" +addFoodDatetime.getText().toString());
        }
    }

    /**
     * 截止時間按鈕
     */
    public void datePickerBtn(View view) {
        showDatetimePicker();
    }

    /**
     * 改寫返回鍵
     */
    @Override
    public void onBackPressed() {
         MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("確定要離開？")
                .setMessage("系統將不會保留您輸入的資訊")
                .setNegativeButton("不離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(AddFoodActivity.this,MainActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        AddFoodActivity.this.finish();
                    }
                });
               dialog.show();


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("確定要離開發布頁面嗎？ 系統將不會保存您輸入的資料！");
//        builder.setPositiveButton("不要", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("離開", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AddFoodActivity.this.finish();
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();


    }
}

