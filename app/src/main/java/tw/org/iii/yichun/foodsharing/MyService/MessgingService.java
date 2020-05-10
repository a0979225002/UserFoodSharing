package tw.org.iii.yichun.foodsharing.MyService;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import es.dmoral.toasty.Toasty;
import tw.org.iii.yichun.foodsharing.Item.User;
import tw.org.iii.yichun.foodsharing.Loading.LoadingActivity;
import tw.org.iii.yichun.foodsharing.MainActivity;
import tw.org.iii.yichun.foodsharing.R;

public class MessgingService extends FirebaseMessagingService {

    String title;
    String body;
    int i = User.getI();

    @Override
    public void onCreate() {
        super.onCreate();


    }

    /**
     * 如果程式使用中,會呼叫這個onMessageReceived,可從這裏拿取到對方傳送的通知內容
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.v("lipin", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.v("lipin", "Message data payload: " + remoteMessage.getData());
        }

        /**
         * remoteMessage.getNotification().getTitle() 拿取通知標題
         * remoteMessage.getNotification().getBody() 拿取通知內容
         */





        title = remoteMessage.getNotification().getTitle();
        body = remoteMessage.getNotification().getBody();

        setnotification();


//        if (remoteMessage.getNotification()!= null) {
//
//
//            uIhandler.sendEmptyMessage(i++);
//            User.setI(i);
//            Log.v("lipin",User.getI()+"");
//        }
    }


    private void setnotification(){
                //點擊推播會進入的頁面

        final String CHANNEL_ID = "lipin";



        Intent intent = new Intent(this, LoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


                //由Builder生成需發送的物件

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //由Manager發送物件
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, builder.build());
    }


    UIhandler uIhandler = new UIhandler();
    private class UIhandler extends Handler {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {

            Toasty.info(getApplicationContext(),body,Toasty.LENGTH_LONG,true)
                    .show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(intent);

                if (msg.what == 0) {
                    FloatWindow
                            .with(getApplicationContext())
                            .setView(LayoutInflater.from(MessgingService.this).inflate(R.layout.floatwindows, null))
                            .setWidth(200)
                            .setHeight(Screen.width, 0.2f)
                            .setX(100)
                            .setY(Screen.height, 0.3f)
                            .setDesktopShow(true)
                            .setViewStateListener(new ViewStateListener() {
                                @Override
                                public void onPositionUpdate(int i, int i1) {
                                    //移動監聽
                                }

                                @Override
                                public void onShow() {
                                    //顯示呼叫
                                }

                                @Override
                                public void onHide() {
                                    Log.v("lipin", "onHide");
                                }

                                @Override
                                public void onDismiss() {
                                    //關閉時呼叫
                                }

                                @Override
                                public void onMoveAnimStart() {
                                }

                                @Override
                                public void onMoveAnimEnd() {
                                    Log.v("lipin", "onMoveAnimEnd");
                                }

                                @Override
                                public void onBackToDesktop() {
                                    Log.v("lipin", "onBackToDesktop");
                                }
                            }).build();
                    FloatWindow.get().show();

                }

            }
        }

    /**
     * 拿取token才能知道是誰,發送的人是誰
     * @param s
     */
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.v("lipin",s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uIhandler.removeCallbacksAndMessages(null);
//        FloatWindow.destroy();
    }
}
