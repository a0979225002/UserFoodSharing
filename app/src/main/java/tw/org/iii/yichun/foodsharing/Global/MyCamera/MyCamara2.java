package tw.org.iii.yichun.foodsharing.Global.MyCamera;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.sl.utakephoto.compress.CompressConfig;
import com.sl.utakephoto.crop.CropOptions;
import com.sl.utakephoto.exception.TakeException;
import com.sl.utakephoto.manager.ITakePhotoResult;
import com.sl.utakephoto.manager.UTakePhoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.AddFoodActivity;
import tw.org.iii.yichun.foodsharing.Item.AddFood;
import tw.org.iii.yichun.foodsharing.R;

public class MyCamara2 extends AppCompatActivity {

    @BindView(R.id.imgview)
    ImageView imgview;

    private String Imgname;
    private AddFood addFood;
    private Intent intent;
    private String editFoodcard;
    private int position;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camara2);
        ButterKnife.bind(this);

        addFood = (AddFood) getIntent().getSerializableExtra("savefood");
        intent = getIntent();
        position  = intent.getIntExtra("position",-1);

        editFoodcard = intent.getStringExtra("FoodinfoGiver");
        comera();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMddHHmmss");
        Date date = new Date();
        Imgname = "FoodSharing"+simpleDateFormat.format(date)+".jpg";

        File sdcard = new File(Environment.getExternalStorageDirectory(),Imgname);
        //將file轉為uri,因為部分手機無法轉出正確uri,自寫一個相對路徑xml來轉
        imageFilePath = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", sdcard);

        Log.v("lipin", "照片路徑：" + imageFilePath.toString());
        return imageFilePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void comera() {

        Uri uri = createImagePathUri(this);
        UTakePhoto.with(this)
                .openCamera()
                .setCrop(new CropOptions.Builder().create())
                .setCompressConfig(new CompressConfig.Builder().setTargetUri(uri).create())
                .build(new ITakePhotoResult() {
                    @Override
                    public void takeSuccess(List<Uri> uriList) {
                        Log.v("lipin", uriList.get(0) + "");


                        intent = new Intent(MyCamara2.this, AddFoodActivity.class);

                        Log.v("lipin", Imgname);

                        if (Imgname != null) addFood.setAddFoodImg(Imgname);

                        intent.putExtra("savefood",addFood);
                        if (editFoodcard!=null){
                            intent.putExtra("FoodinfoGiver_preview", "editFoodcard2");//通知intent的b介面能更改
                            intent.putExtra("position", position);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivityForResult(intent,321);
                        MyCamara2.this.finish();
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }

                    @Override
                    public void takeFailure(TakeException ex) {
                        Log.v("lipin", ex.toString());
                    }

                    @Override
                    public void takeCancel() {
                        Log.v("lipin", "取消");
                        intent = new Intent(MyCamara2.this,AddFoodActivity.class);
                        if (editFoodcard!=null){
                            intent.putExtra("FoodinfoGiver_preview", "editFoodcard2");//通知intent的b介面能更改
                            intent.putExtra("position", position);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivityForResult(intent,321);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    }


                });
    }

    @Override
    public void onBackPressed() {


    }
}
