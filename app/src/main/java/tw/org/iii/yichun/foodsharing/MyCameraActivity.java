package tw.org.iii.yichun.foodsharing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tw.org.iii.yichun.foodsharing.Item.AddFood;

public class MyCameraActivity extends AppCompatActivity {
    private Camera camera;
    private CameraPreview cameraPreview;
    private FrameLayout container;
    private File sdroot;
    private SensorManager sensorManager;
    private Sensor sensor;
    private MySensorListener mySensorListener;
    private Bitmap bmp;
    private ImageView imageView;
    private String Imgname;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);

        findID();

        Intent intent = getIntent();

    }



    private void findID(){
        camera = getCameraInstance();
//        camera.getParameters().setFocusMode();

        container = findViewById(R.id.preview);
        cameraPreview = new CameraPreview(this, camera);
        container.addView(cameraPreview, 0);
        camera.setDisplayOrientation(90);
        sdroot = Environment.getExternalStorageDirectory();

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
