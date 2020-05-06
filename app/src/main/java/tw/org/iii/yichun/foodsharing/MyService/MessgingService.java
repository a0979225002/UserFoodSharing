package tw.org.iii.yichun.foodsharing.MyService;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessgingService extends FirebaseMessagingService {


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
            Log.v("lipin", "Message Notification: " +remoteMessage.getNotification().getTitle()+ remoteMessage.getNotification().getBody());
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
}
