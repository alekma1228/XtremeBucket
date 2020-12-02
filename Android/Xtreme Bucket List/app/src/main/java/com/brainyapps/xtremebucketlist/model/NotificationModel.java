package com.brainyapps.xtremebucketlist.model;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppPreference;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.XtremeBucketListApp;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.parse.ParseUser;

import java.util.List;

public class NotificationModel {
	// type
	public static int TYPE_REQUEST_CONSENT 	= 0x001;
	public static int TYPE_ACCEPT_CONSENT 	= 0x002;
	public static int TYPE_DECLINE_CONSENT 	= 0x003;

	public int type;
	public String message;

	public static String foregroundActivityName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		return componentInfo.getClassName();
	}
	public static boolean isRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo task : tasks) {
			if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
				return true;
		}
		return false;
	}
	public static void showNotification(final int type, final String objectId, final String message, final String senderId) {
		ParseUser curUser = ParseUser.getCurrentUser();
		if (curUser == null)
			return;

		if (TextUtils.isEmpty(message))
			return;

		final Context context = XtremeBucketListApp.getContext();

//		if (foregroundActivityName(context).equals("com.brainyapps.safeconsent.ui.activity.MainActivity")){
//			if (MainActivity.instance != null
//					&& MainActivity.instance.consentFragment != null
//					&& MainActivity.instance.getCurrentPageIndex() == 0){
//				MainActivity.instance.consentFragment.refresh();
//			} else if (MainActivity.instance != null
//					&& MainActivity.instance.notificationFragement != null
//					&& MainActivity.instance.getCurrentPageIndex() == 2){
//				MainActivity.instance.notificationFragement.refresh();
//			}
//		}

		NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(XtremeBucketListApp.getContext())
				.setSmallIcon(R.drawable.noti_icon)
				.setContentTitle(XtremeBucketListApp.getContext().getString(R.string.app_name))
				.setContentText(message)
				.setAutoCancel(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notiBuilder.setSmallIcon(R.drawable.noti_icon);
		} else {
			notiBuilder.setSmallIcon(R.drawable.noti_icon);
		}

		// get notification sound
		Uri alarmSound = null;
		RingtoneManager ringtoneMgr = new RingtoneManager(context);
		ringtoneMgr.getCursor();

		// get default notification sound of device
		if (alarmSound == null) {
			alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		notiBuilder.setSound(alarmSound);

		// vibrate
		boolean isVibrate = true;
		if (isVibrate) {
			long[] pattern = {500};
			notiBuilder.setVibrate(pattern);
		}
		// open page when click the notification
		Intent resultIntent = null;
		resultIntent = new Intent(XtremeBucketListApp.getContext(), MainActivity.class);
		resultIntent.putExtra(AppConstant.EK_TYPE, type);
		resultIntent.putExtra(AppConstant.EK_OBJECTID, objectId);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		if (resultIntent != null) {
			PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    XtremeBucketListApp.getContext(),
					0,
					resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

			notiBuilder.setContentIntent(resultPendingIntent);
		}

		// show notification
		NotificationManager notificationManager = (NotificationManager) XtremeBucketListApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(objectId, type, notiBuilder.build());

		AppPreference appPreference = new AppPreference(XtremeBucketListApp.getContext());
		int count = AppPreference.getInt(AppPreference.KEY.NOTIFICATION_COUNT, 0);
		count++;
		AppPreference.setInt(AppPreference.KEY.NOTIFICATION_COUNT, count);

//		if (foregroundActivityName(context).equals("com.brainyapps.safeconsent.ui.activity.MainActivity")) {
//			if (MainActivity.instance != null) {
//				MainActivity.instance.refreshNotifications();
//			}
//		}
	}
}

