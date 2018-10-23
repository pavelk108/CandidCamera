package ru.pavelkr.candidcamera;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class CameraService extends Service implements Camera.PictureCallback{
	private static final int NOTIFICATION_ID=1;
	
	private NotificationManager manager;
	private NotificationCompat.Builder builder;
	private BroadcastReceiver receiver = new CameraServiceStarter();
	private int count = 0;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		{
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			PendingIntent pendIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, intent, 0);

			builder = new NotificationCompat.Builder(this);
			builder.setContentIntent(pendIntent)
					.setSmallIcon(R.drawable.ic_launcher)
					.setTicker("CandidCamera сервис запущен")
					.setWhen(System.currentTimeMillis())
					.setContentTitle("Azaza")
					.setContentText("Сервис работает");
		}
		count = 0;
		
		startForeground(NOTIFICATION_ID, builder.build());
		getApplicationContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}
	
	@Override
	public void onDestroy(){
		getApplicationContext().unregisterReceiver(receiver);
		manager.cancelAll();
		Toast.makeText(this, "CandidCamera's service have stoped", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		builder.setContentText("#"+count);
		startForeground(NOTIFICATION_ID, builder.build());
		count++;
		Log.d(getPackageName(), "onStart");
		Camera camera = null;
		try 
		{	
			camera = Camera.open(1);
			SurfaceTexture st = new SurfaceTexture(0);
			
			camera.setPreviewTexture(st);
			camera.startPreview();
			camera.takePicture(null, null, this); //take jpeg
			Log.d(getPackageName(), "camera take");
		}
		catch (Exception e){
			if (camera != null){
				camera.release();
			}
			Log.e(getPackageName(), "camera error");
		}
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		camera.release();
		Log.d(getPackageName(), "picture taken "+data.length);
		FileOutputStream fos = null;
		FileChannel fc = null;
		try {
			fos = new FileOutputStream("/sdcard/Pictures/PHT"+count+".jpg");
			fc = fos.getChannel();
			
			ByteBuffer bb = ByteBuffer.allocate(data.length);
			bb.put(data, 0, data.length);
			fc.write(bb);
			
		} catch (IOException e) {
			Log.e(getPackageName(), "FS error");
		}
		finally {
			if (fc!=null)
				try {
					fc.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
