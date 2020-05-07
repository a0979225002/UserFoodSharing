package tw.org.iii.yichun.foodsharing;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sl.utakephoto.compress.CompressConfig;
import com.sl.utakephoto.crop.CropOptions;
import com.sl.utakephoto.exception.TakeException;
import com.sl.utakephoto.manager.ITakePhotoResult;
import com.sl.utakephoto.manager.UTakePhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.org.iii.yichun.foodsharing.Item.AddFood;

public class MyCameraActivity extends AppCompatActivity {
//    @BindView(R.id.imgview)
    ImageView imgview;
    private Camera camera;
    private CameraPreview cameraPreview;
    private FrameLayout container;
    private File sdroot;
    private SensorManager sensorManager;
    private Sensor sensor;
    //    private MySensorListener mySensorListener;
    private Bitmap bmp;
    private ImageView imageView;
    private String Imgname;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        ButterKnife.bind(this);
//        createImagePathUri(this);
        findID();

//        comera();


        Intent intent = getIntent();

    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private static File createImagePathUri(Context context) {
//        File imageFilePath = null;
//        String status = Environment.getExternalStorageState();
//        SimpleDateFormat timeFormatter = new SimpleDateFormat(
//                "yyyyMMdd_HHmmss", Locale.CHINA);
//        long time = System.currentTimeMillis();
//        String imageName = timeFormatter.format(new Date(time));
//        // ContentValues是我们希望这条记录被创建时包含的数据信息
//        ContentValues values = new ContentValues(3);
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
//        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//        File sdcard = Environment.getExternalStorageDirectory();
//        imageFilePath = new File(sdcard,"12345");
//
//        Log.v("lipin", "生成的照片输出路径：" + imageFilePath.toString());
//        return imageFilePath;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void comera() {
//        Uri uri = Uri.fromFile(createImagePathUri(this));
//        UTakePhoto.with(this)
//                .openCamera()
//                .setCrop(new CropOptions.Builder().create())
//                .setCompressConfig(new CompressConfig.Builder().setTargetUri(uri).create())
//                .build(new ITakePhotoResult() {
//                    @Override
//                    public void takeSuccess(List<Uri> uriList) {
//                        Log.v("lipin", uriList.get(0)+"");
//                        imageView.setImageURI(uriList.get(0));
//
//                    }
//
//                    @Override
//                    public void takeFailure(TakeException ex) {
//                        Log.v("lipin", ex.toString());
//                    }
//
//                    @Override
//                    public void takeCancel() {
//                        Log.v("lipin", "取消");
//                    }
//
//
//                });
//    }


    private void findID(){
        camera = getCameraInstance();
//        camera.getParameters().setFocusMode();

        container = findViewById(R.id.preview);
        cameraPreview = new CameraPreview(this, camera);
        container.addView(cameraPreview, 0);
        camera.setDisplayOrientation(90);
        sdroot = Environment.getExternalStorageDirectory();

        Log.v("lipin",sdroot.toString());

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        // 控制相機旋轉不失真
        int r = getWindowManager().getDefaultDisplay().getRotation();
        Log.v("yichun", "r = " + r);
        if (r == 0){
            camera.setDisplayOrientation(90);
        }else if (r == 1){
            camera.setDisplayOrientation(0);
        }else {
            camera.setDisplayOrientation(180);
        }
    }

    MySensorListener mySensorListener = new MySensorListener();

    private class MySensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float v = values[0];
            Log.v("yichun","O = " + v);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(mySensorListener);
        releaseCamera();
    }

    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    public void takPic(View view) {
        camera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
//                Log.v("yichun","take photo");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
//                Log.v("yichun","debug1");
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
//                Log.v("yichun","debug2");
                savePic(data);
            }
        });
    }

    private void savePic(byte[] data){
        Log.v("yichun","file: " + data.length);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMddHHmmss");
            Date date = new Date();
            Imgname = "FoodSharing"+simpleDateFormat.format(date)+".jpg";
            FileOutputStream fout = new FileOutputStream(new File(sdroot, Imgname));

                        Log.v("lipin",new File(sdroot, Imgname).toString());

            fout.write(data);
            fout.flush();
            fout.close();
            setResult(RESULT_OK);

            showDialog();

        } catch (IOException e) {
            Log.v("yichun", e.toString());
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyCameraActivity.this);
        builder.setPositiveButton("使用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.v("lipin",Imgname+"照片地址");

                intent = getIntent();

                AddFood addFood = (AddFood) getIntent().getSerializableExtra("savefood");

                Log.v("lipin",addFood.getAddFoodName());



                intent = new Intent(MyCameraActivity.this, AddFoodActivity.class);



                if (Imgname != null) addFood.setAddFoodImg(Imgname);

                intent.putExtra("savefood",addFood);

                startActivityForResult(intent,321);

                finish();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = builder.create();
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.camera_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        ImageView img = dialog.findViewById(R.id.camera_dialog_img);
        Bitmap bmp = BitmapFactory.decodeFile(sdroot.getAbsolutePath() + "/"+Imgname+".jpg");
        img.setImageBitmap(bmp);

    }
}
