package ru.pavelkr.candidcamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CameraServiceStarter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, CameraService.class));
	}

}
