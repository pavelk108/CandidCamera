package ru.pavelkr.candidcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	private OnClickListener ocl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ocl = new OnClickListener(getApplicationContext());
		findViewById(R.id.buttonStart).setOnClickListener(ocl);
		findViewById(R.id.buttonStop).setOnClickListener(ocl);
		findViewById(R.id.buttonSave).setOnClickListener(ocl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
class OnClickListener implements View.OnClickListener{
	private Context context;
	private Intent intent;
	OnClickListener(Context context){
		this.context = context;
		intent = new Intent(context, CameraService.class);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonStart:
			context.startService(intent);
			break;
		case R.id.buttonStop:
			context.stopService(intent);
			break;
		case R.id.buttonSave:
			break;
		}
	}
}
