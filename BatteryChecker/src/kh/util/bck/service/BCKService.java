package kh.util.bck.service;


import java.util.ArrayList;

import kh.util.bck.BetteryChecker;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class BCKService extends Service {
	protected String[] strText = new String[3];
	private final static String TAG = "BCKService";
	private final static int NOTI_NUM  = 0221;
	private int cnt = 0 ;
	private ArrayList<TimeStat> preLevel;
	public BetteryChecker mBetteryChk;
	public BCKService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	private void init() {
		// TODO Auto-generated method stub
		IntentFilter batteryLevelFilter = new IntentFilter();  
		batteryLevelFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		batteryLevelFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		batteryLevelFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);  
		preLevel = new ArrayList<TimeStat>();
		mBetteryChk = BetteryChecker.getInstance();
		mBetteryChk.initContext(getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		init();
		Notification mNoti = mBetteryChk.makeNoti(null,null,null);
		startForeground(NOTI_NUM, mNoti);
		
		return START_STICKY;
	}

	

	BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {  
		@Override
		public void onReceive(Context context, Intent intent) {  
			//	            context.unregisterReceiver(this);  
			cnt++;
			int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);  
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);  
			int level = -1;  
			if (rawlevel >= 0 && scale > 0) {  
				level = (rawlevel * 100) / scale;  
			}  
			int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);  
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);  
			int onplug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);  
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;  
			boolean onUSB = onplug == BatteryManager.BATTERY_PLUGGED_USB;  
			boolean onAC = onplug == BatteryManager.BATTERY_PLUGGED_AC;  
			String strStatus = "Charging on ";  
			if (isCharging && onUSB)  
				strStatus += "USB";  
			else if (isCharging && onAC)  
				strStatus += "AC Power";  
			else  
				strStatus = "Battery Discharging";  
			strText [0] = "Battery Level: " + Integer.toString(level) + "%";  
			strText[1] = "Voltage: " + Integer.toString(voltage) + "mV";  
			strText[2] = strStatus;  
			Log.d(TAG,strText[0]+" count :"+cnt);
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			int size = preLevel.size();
			if(size == 0)	{
				preLevel.add(new TimeStat(level,today));
				Toast.makeText(getApplicationContext(), "Current Level is "+level, Toast.LENGTH_SHORT).show();

			}else if(preLevel.get(size-1).level > level){
				Time preTime = preLevel.get(size-1).time;
				int i = checkTime(preTime, today);

				Log.d(TAG," time :"+i);
				int hour = 0;
				int min= 0;
				int sec = 0;
				i = i * 100;
				hour = i/3600;
				min = (i%3600)/60;
				sec = i%60;
				String t = hour+"시간, "+min+"분, "+sec+"초 ";
				String msg = "앞으로 "+t+" 남았습니다.";
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				
				Notification mNoti = mBetteryChk.makeNoti(null,msg,null);
				startForeground(NOTI_NUM, mNoti);
				
				preLevel.add(new TimeStat(level,preTime));
			}
		}

		private int checkTime(Time pre, Time now) {
			// TODO Auto-generated method stub
			int sec = 0;

			if(pre.hour == now.hour){
				if(pre.hour == now.hour){
					if(pre.minute == now.minute){
						if(pre.second == now.second){

						}else{
							sec = sec + now.second - pre.second;
						}
					}else{

						sec = sec + (now.minute - pre.minute)*60;
					}
				}else{
					if(now.hour == 1 && pre.hour == 24){
						sec = sec + 3600;
					}else{
						sec = sec + (now.hour - pre.hour)*3600;
					}
				}
			}


			return sec;
		}  
	};  
}
