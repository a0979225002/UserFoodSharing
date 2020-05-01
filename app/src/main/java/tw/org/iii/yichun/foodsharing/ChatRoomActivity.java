package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView showMessage;
    private EditText editText;
    private WebSocketClient webSocketClient;
    private StringBuilder sb = new StringBuilder();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            sb.append("服務器返回數據： ");
            sb.append(msg.obj.toString());
            sb.append("\n");
            showMessage.setText(sb.toString());
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        setToolbar();

        showMessage = findViewById(R.id.chatRoom_show_msg);
        editText = findViewById(R.id.chatRoom_text);
        findViewById(R.id.chatRoom_send).setOnClickListener(this);
        URI serverURI = URI.create("ws://192.168.0.116:8810");

        webSocketClient = new WebSocketClient(serverURI) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                sb.append("onOpen at time: ");
//                sb.append(new Data());
                sb.append("服務器狀態：");
                sb.append(handshakedata.getHttpStatusMessage());
                sb.append("\n");
                showMessage.setText(sb.toString());
            }

            @Override
            public void onMessage(String message) {
                Message handlerMessage = Message.obtain();
                handlerMessage.obj = message;
                handler.sendMessage(handlerMessage);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                sb.append("onClose at time：");
//                sb.append(new Date());
                sb.append("\n");
                sb.append("onClose info:");
                sb.append(code);
                sb.append(reason);
                sb.append(remote);
                sb.append("\n");
                showMessage.setText(sb.toString());
            }

            @Override
            public void onError(Exception ex) {
                sb.append("onError at time：");
//                sb.append(new Date());
                sb.append("\n");
                sb.append(ex);
                sb.append("\n");
//                showMessage.setText(sb.toString());
            }
        };
        webSocketClient.connect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatRoom_send:
                if(webSocketClient.isClosed() || webSocketClient.isClosing()){
                    Snackbar.make(v,"Client 正在關閉",Snackbar.LENGTH_SHORT).show();
                    webSocketClient.connect();
                    break;
                }
                webSocketClient.send(editText.getText().toString().trim());
                sb.append("客户端发送消息：");
//                sb.append(new Date());
                sb.append("\n");
                sb.append(editText.getText().toString().trim());
                sb.append("\n");
                showMessage.setText(sb.toString());
                editText.setText("");
                break;
            default:
                break;
        }
    }

    /**
     * 設定 Toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.chatRoom_toolbar);
        String title = "李大華"; // 聊天對象名字
        String subtitle = "愛心便當" + " - 索取者"; //聊天對象身份

        toolbar.setTitle(title);
        toolbar.setSubtitle(subtitle);

    }

}
