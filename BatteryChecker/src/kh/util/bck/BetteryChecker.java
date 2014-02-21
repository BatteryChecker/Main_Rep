package kh.util.bck;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class BetteryChecker {
	public static BetteryChecker mInstance = new BetteryChecker();
	public Context mContext;
	private BetteryChecker(){
		
	}
	public static BetteryChecker getInstance(){
		return mInstance;
	}
	public void initContext(Context context){
		mContext = context;
	}
	public Notification makeNoti(String title, String sub, String tick) {
		// TODO Auto-generated method stub
		int icon = R.drawable.img1;	//임시 이미지

		String contentTitle = mContext.getString(R.string.noti_title);	// expanded message title
		String contentSub = mContext.getString(R.string.noti_sub);     	// expanded message text
		String ticker = mContext.getString(R.string.service_on);
		PendingIntent mPI = PendingIntent.getActivity(mContext
				, 0	, new Intent().setAction("android.intent.action.ECOMODE_SETTINGS")
				,PendingIntent.FLAG_CANCEL_CURRENT);
		if(title !=null)	contentTitle = title;
		if(sub != null)		contentSub = sub;
		if(tick != null)	ticker = tick;
		
		Notification noti = new Notification.Builder(mContext)
		.setContentIntent(null)
		.setSmallIcon(icon)
		.setTicker(ticker)
		.setWhen(System.currentTimeMillis())
		.setContentTitle(contentTitle)
		.setContentText(contentSub)
		.setOngoing(true)
		.setContentIntent(mPI)
		.getNotification();
		
		return noti;

	}
}
