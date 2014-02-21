package kh.util.bck;

import java.util.List;

import kh.util.bck.service.BCKService;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static String PACKAGE_NAME = "kh.util.bck.service.BCKService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.sss).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isRunningService(MainActivity.this)){
					Toast.makeText(getApplicationContext(), "Stop Service", Toast.LENGTH_SHORT).show();
					stopService(new Intent(MainActivity.this,BCKService.class));
				}else{
					Toast.makeText(getApplicationContext(), "Start Service", Toast.LENGTH_SHORT).show();
					startService(new Intent(MainActivity.this,BCKService.class));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean isRunningService(Context context){
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> rsi =
				manager.getRunningServices(100);
		for(int i=0; i<rsi.size();i++){
			ActivityManager.RunningServiceInfo rsInfo = rsi.get(i);
			if(rsInfo.service.getClassName().equals(
					PACKAGE_NAME )){
				return true;
			}
		}
		return false;
	}
}
