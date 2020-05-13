package tw.org.iii.yichun.foodsharing.GoogleMap;

import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import tw.org.iii.yichun.foodsharing.FoodinfoTaker;
import tw.org.iii.yichun.foodsharing.Global.MainUtils;
import tw.org.iii.yichun.foodsharing.Global.Utils;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.R;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    List<Address> maplist = null;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = null;

        MainUtils.showloading(this);
        nearbyUserCard();


        // Add a marker in Sydney and move the camera

        if (User.getLatitude()!=null){
            sydney = new LatLng(User.getLatitude(), User.getLongitude());
        }else {
            sydney = new LatLng(100, 100);
        }

        Marker m01 =  mMap.addMarker(new MarkerOptions().position(sydney).title("您現在所在的位置"));
        m01.setTag(100);

        mMap.setMinZoomPreference(12);//顯示地圖的俯視高度
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));//顯示當下第一次無拖移時的位置

        //標記監聽
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int tag = Integer.valueOf((Integer) marker.getTag());


                if (tag != 100) {
                    //拿取自訂的dialog
                    LayoutInflater inflater = LayoutInflater.from(Map.this);
                    View newview = inflater.inflate(R.layout.mydialog2, null);
                    //拿取自訂dialog的ID
                    ImageView imag = newview.findViewById(R.id.foodList_img);
                    TextView foodname = newview.findViewById(R.id.foodList_title);
                    TextView address = newview.findViewById(R.id.foodList_location);
                    TextView dueDate = newview.findViewById(R.id.foodList_deadline);
                    TextView split = newview.findViewById(R.id.foodList_quantity);
                    TextView qty = newview.findViewById(R.id.foodList_leftQuantity);

                    imag.setImageBitmap((Bitmap) list.get(tag).get("image"));
                    foodname.setText("名稱:" + (String) list.get(tag).get("title"));
                    address.setText("地區:" + (String) list.get(tag).get("address"));
                    dueDate.setText("期限:" + (String) list.get(tag).get("deadline"));
                    split.setText("可否拆領:" + (String) list.get(tag).get("quantity"));
                    qty.setText("預估剩餘份數:" + (String) list.get(tag).get("leftQuantity") + "份");


                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(Map.this)
                            .setTitle("食物資訊")
                            .setView(newview)
                            .setNegativeButton("前往排隊", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Map.this, FoodinfoTaker.class);
                                    intent.putExtra("position", tag);
                                    startActivity(intent);
                                }
                            })
                            .setPositiveButton("不排隊", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                }
                Log.v("lipin",marker.getTag()+"123");

                return false;
            }
        });

    }

    /**
     * 將各個食物卡片顯示在地圖中
     */
    private void SaveMap(){
        for (int i = 0; i <list.size();i++){
           String address =
                   list.get(i).get("city").toString()+
                           list.get(i).get("dist").toString()+
                                list.get(i).get("address").toString();

           Log.v("yichun",address);

            Geocoder geocoder = new Geocoder(this, Locale.TAIWAN);
            try {
                maplist = geocoder.getFromLocationName(address,5);
                LatLng sydney = new LatLng(maplist.get(0).getLatitude(), maplist.get(0).getLongitude());
                Marker marker =  mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title((String) list.get(i).get("title"))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.foodicon)));
                marker.setTag(i);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 抓user這附近有發布的食物卡片
     */
    private void nearbyUserCard(){
        String url  = "http://"+ Utils.ip +"/FoodSharing_war/Sql_getfoodcardCity";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonFoodcard(response);
                        Log.v("lipin",response);
                        SaveMap();
                        MainUtils.dimissloading();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("lipoin",error.toString());
                    }
                }
        ){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("usercity",User.getCity());
                params.put("userdist",User.getDist());
                params.put("userid",User.getId());

                return params;
            }
        };
        MainUtils.queue.add(request);
    }

    /**
     * 拿取這縣市所有食物卡片json欓
     */
    JSONObject row;
    HashMap<String, Object> hashMap;
    List<HashMap<String, Object>>list;
    private void JsonFoodcard(String response) {

        list = new ArrayList<HashMap<String, Object>>();

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                row = array.getJSONObject(i);

                getData(list);//將抓取的值一個一個放在listview裡面

            }
            MainUtils.setList(list);//儲存到靜態類中

        } catch (Exception e) {
            Log.v("lipin", "JsonFoodcard:" + e.toString());
        }

    }

    /**
     * 食物 ListView getData
     */
    // TODO: 2020/4/27 撈資料庫資料
    public void getData( List<HashMap<String, Object>>list) throws ParseException {
        //因為list加入的方式的比對方式是地址,重複的地址物件會被蓋過,所以需要每次尋訪時是產生新的hashMap,故在此new出來
        hashMap = new HashMap<String, Object>();
        //拿取拆領的數字,0 or 1
        int split = Integer.valueOf(row.optString("split"));

        //將拆領的值更改為字串
        String stringSplit = null;
        if (split == 1) {
            stringSplit = "可拆領";
        } else if (split == 0) {
            stringSplit = "不可拆領";
        }
        //將sql回來的截止時間,優化顯示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = format.parse(row.optString("due_date"));
        String dueDate = format.format(date);

        Log.v("lipin", dueDate);

        //取出base64的圖片
        String base64Img = row.optString("foodimg");
        //轉成bitmap
        Bitmap bitmap = MainUtils.base64Tobitmap(base64Img);

        //下面東東目的,當客戶端點擊後進入詳細資料時可查看
        hashMap.put("image", bitmap);
        hashMap.put("title", row.optString("name"));
        hashMap.put("city", row.optString("city"));
        hashMap.put("dist", row.optString("dist"));
        hashMap.put("deadline", dueDate);
        hashMap.put("quantity", stringSplit);
        hashMap.put("leftQuantity", row.optString("qty"));
        hashMap.put("account", row.optString("account"));
        hashMap.put("username", row.optString("username"));
        hashMap.put("address", row.optString("address"));
        hashMap.put("detail", row.optString("detail"));
        hashMap.put("category", row.optString("category"));
        hashMap.put("tag", row.optString("tag"));
        hashMap.put("foodid",row.optString("id"));
        hashMap.put("token", row.optString("token"));

        list.add(hashMap);
    }

}
