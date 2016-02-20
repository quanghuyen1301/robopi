package com.example.hqbui.robopi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity implements SurfaceHolder.Callback, OnPreparedListener{
 
 MediaPlayer mediaPlayer;
 SurfaceHolder surfaceHolder;
 SurfaceView playerSurfaceView;
 String videoSrc = "rtsp://192.168.43.172:8554/unicast";

 private final static int PACKETSIZE = 100;
 public  static String ServerIP = "Scan...";
 public final static String PortNum = "8888";
 public static String Msg = "up";
 private final static String ControlUp = "up";
 private final static String ControlDown = "d";
 private final static String ControlLeft = "f";
 private final static String ControlRigth = "r";
 private final static String ControlStop = "s";
 private final static String TAG = "RoboPi";
 public  static int ACT = 0;
 private GestureDetector GD;
 public TextView text;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  playerSurfaceView = (SurfaceView)findViewById(R.id.surfaceView);


  text=(TextView)findViewById(R.id.textView);
  text.setText(ServerIP);
//
//        __ControlPi(ServerIP, PortNum, ControlUp);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
  getWifiApIpAddress();
  GD = new GestureDetector(this, SOGL);

  surfaceHolder = playerSurfaceView.getHolder();

 }

 public String getWifiApIpAddress() {
  new Thread(new Runnable() {

   @Override
   public void run() {


    try {
     for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
             .hasMoreElements(); ) {
      NetworkInterface intf = en.nextElement();
      //if (intf.getName().contains("wlan"))
      {
       for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
               .hasMoreElements(); ) {
        InetAddress inetAddress = enumIpAddr.nextElement();
        if (!inetAddress.isLoopbackAddress()
                && (inetAddress.getAddress().length == 4)) {
         Log.e(TAG, inetAddress.getHostAddress());


         byte[] ip = inetAddress.getAddress();

         for (int i = 2; i <= 254; i++) {
          try {
           ip[3] = (byte) i;
           InetAddress address = InetAddress.getByAddress(ip);
           String addressbystr = address.toString().substring(1);
           //if (address.isReachable(100))
           {
            ServerIP = addressbystr;
            text.post(new Runnable() {
             public void run() {
              text.setText(ServerIP);
             }
            });
            //Log.e(TAG, addressbystr + " is on the network");
            if (UDPACK(addressbystr, MainActivity.PortNum) == 0) {
             ServerIP = addressbystr;
             text.post(new Runnable() {
              public void run() {
               text.setText(ServerIP);
              }
             });



             break;
            }
           }
          } catch (IOException e) {
           e.printStackTrace();
          }
         }

         //  return inetAddress.getHostAddress();
        }
       }
      }
     }
    } catch (SocketException ex) {
     Log.e(TAG, ex.toString());
    }

   }
  }).start();

  return null;
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
    __ControlPi(ServerIP, PortNum, ControlStop);

   }else {
    if (y1 < y2) { // up to down
     Log.e("hqbui", "up to down");
     __ControlPi(ServerIP, PortNum, ControlDown);

    }

    if (y1 > y2) { // down to up
     Log.e("hqbui", "down to up");
     __ControlPi(ServerIP, PortNum, ControlUp);
    }
    try {
     Thread.sleep((long)distanceY*2);
    } catch (InterruptedException e) {
     e.printStackTrace();
    }
    __ControlPi(ServerIP, PortNum, ControlStop);
   }

   return false;
  }

 };
 public int UDPACK(String serverip,String portnum){
  int ret = 1;
  String msg = "ACT";
  DatagramSocket socket = null;

  try {
   // Convert the arguments first, to ensure that they are valid
   InetAddress host = InetAddress.getByName(serverip);
   int port = Integer.parseInt(portnum);

   // Construct the socket
   socket = new DatagramSocket(port);

   // Construct the datagram packet
   byte[] data = msg.getBytes();
   DatagramPacket packet = new DatagramPacket(data, data.length, host, port);

   // Send it
   socket.send(packet);

   socket.setSoTimeout(100);
   byte[] receiveData = new byte[1024];
   DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
   socket.receive(receivePacket);
   String msg_receive= new String(receivePacket.getData());
   //msg_receive[receivePacket.getLength()] = "\0";
   Log.e(TAG,"MSG " + msg_receive);
   if(receiveData[0] == 'D'){
    ret = 0;
   }
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
  return ret;
 }
 public static void  __ControlPi(String ServerIP, String PortNum, String Msg2) {

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
     byte[] receiveData = new byte[1024];
     DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
     socket.receive(receivePacket);
     String msg_receive= new String(receivePacket.getData());
     Log.e(TAG,"MSG " + msg_receive);

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
 public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
  // TODO Auto-generated method stub
  
 }

 @Override
 public void surfaceCreated(SurfaceHolder arg0) {

        try {
         mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
   mediaPlayer.setDataSource(videoSrc);
   mediaPlayer.prepare();
   mediaPlayer.setOnPreparedListener(this);
   mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
  } catch (IllegalArgumentException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (SecurityException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IllegalStateException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }

 @Override
 public void surfaceDestroyed(SurfaceHolder arg0) {
  // TODO Auto-generated method stub
  
 }

 @Override
 public void onPrepared(MediaPlayer mp) {
  mediaPlayer.start();
 }

}
