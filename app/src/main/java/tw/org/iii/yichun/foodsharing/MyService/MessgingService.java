package tw.org.iii.yichun.foodsharing.MyService;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import es.dmoral.toasty.Toasty;
import tw.org.iii.yichun.foodsharing.Item.User;
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
        if (remoteMessage.getNotification() != null) {

            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
            uIhandler.sendEmptyMessage(i++);
            User.setI(i);
            Log.v("lipin",User.getI()+"");
        }
    }


    UIhandler uIhandler = new UIhandler();
    private class UIhandler extends Handler {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {

            Toasty.info(getApplicationContext(),body,Toasty.LENGTH_LONG,true)
                    .show();


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
