package com.example.hqbui.robopi;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.* ;

public class MainActivity extends AppCompatActivity {
    private final static int PACKETSIZE = 100;
    public final static String ServerIP = "192.168.1.45";
    public final static String PortNum = "8888";
    public static String Msg = "up";
    private final static String ControlUp = "up";
    private final static String ControlDown = "d";
    private final static String ControlLeft = "f";
    private final static String ControlRigth = "r";
    private final static String ControlStop = "s";

    private GestureDetector GD;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        __ControlPi(ServerIP, PortNum, ControlUp);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        GD = new GestureDetector(this, SOGL);
       // __ControlPi(ServerIP, PortNum, ControlStop);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GD.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    private GestureDetector.SimpleOnGestureListener SOGL = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            __ControlPi(ServerIP, PortNum, ControlStop);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float veloccityX, float veloccityY) {

            float x1 = e1.getX();
            float x2 = e2.getX();
            float y1 = e1.getY();
            float y2 = e2.getY();
            float distanceX = Math.abs(x1 - x2);
            float distanceY = Math.abs(y1 - y2);
            Log.e("hqbui",x1 + " " + x2 + " " + y1 + " " + y2);
            if(distanceX > distanceY) {
                if (x1 > x2) { // right to left
                    Log.e("hqbui", "right to left");
                    __ControlPi(ServerIP, PortNum, ControlLeft);

                }

                if (x1 < x2) { // left to right
                    Log.e("hqbui", "left to right");
                    __ControlPi(ServerIP, PortNum, ControlRigth);
                }

                try {
                    Thread.sleep((long)distanceX);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                __ControlPi(ServerIP, PortNum, ControlUp);

            }else {
                if (y1 < y2) { // up to down
                    Log.e("hqbui", "up to down");
                    __ControlPi(ServerIP, PortNum, ControlDown);

                }

                if (y1 > y2) { // down to up
                    Log.e("hqbui", "down to up");
                    __ControlPi(ServerIP, PortNum, ControlUp);
                }
            }

            return false;
        }

    };

    public static void __ControlPi(String ServerIP, String PortNum, String Msg2) {
        Msg = Msg2;
        //-----UDP send thread
        new Thread(new Runnable() {

            @Override
            public void run() {
                DatagramSocket socket = null;

                try {
                    // Convert the arguments first, to ensure that they are valid
                    InetAddress host = InetAddress.getByName( MainActivity.ServerIP);
                    int port = Integer.parseInt(MainActivity.PortNum);

                    // Construct the socket
                    socket = new DatagramSocket(port);

                    // Construct the datagram packet
                    byte[] data = Msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, host, port);

                    // Send it
                    socket.send(packet);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null)
                        socket.close();
                }

            }
        }).start();
      //  udpSendThread.run();
        Log.e("RoboPi2", "ControlPi " + ServerIP + ":" + PortNum + ":" + Msg);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.hqbui.robopi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.hqbui.robopi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

