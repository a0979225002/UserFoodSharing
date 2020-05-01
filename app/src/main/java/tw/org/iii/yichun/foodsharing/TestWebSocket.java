package tw.org.iii.yichun.foodsharing;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class TestWebSocket extends WebSocketServer {
    public TestWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public TestWebSocket(InetSocketAddress address) {
        super(address);
    }

    public static void main(String[] args) {
//        WebSocketImpl.DEBUG = true;
        try {
            int port = 843; // 843 flash policy port
            TestWebSocket s = new TestWebSocket(port);
            s.start();
            System.out.println("ChatServer started on port: " + s.getPort());

            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String in = sysin.readLine();
                s.broadcast(in);
                if (in.equals("exit")) {
                    s.stop(1000);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        broadcast("new connection: " + clientHandshake.getResourceDescriptor());
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        broadcast(webSocket + " onClose");
        System.out.println(webSocket + " onClose");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        broadcast(s);
        System.out.println(webSocket + ": " + s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
        if (webSocket != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
    }
}
