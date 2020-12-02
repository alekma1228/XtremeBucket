package com.brainyapps.xtremebucketlist.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.brainyapps.xtremebucketlist.model.NotificationModel;
import com.brainyapps.xtremebucketlist.model.ParseConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class PushIntentService extends IntentService {

	public PushIntentService() {
		super("PushIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String data = extras != null ? extras.getString("com.parse.Data") : "";

		try {
			if (!TextUtils.isEmpty(data)) {
				JSONObject jObject = new JSONObject(data);
				int type = jObject.getInt(ParseConstants.NOTI_TYPE);
				String message = jObject.getString(ParseConstants.NOTI_ALERT);
				String objectId = jObject.getString(ParseConstants.NOTI_DATA);
				String senderId = jObject.getString(ParseConstants.NOTI_SOUND);
				String requestId = null;

				NotificationModel.showNotification(type, objectId, message, senderId);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Release the wake lock provided by the WakefulBroadcastReceiver.
		PushReceiver.completeWakefulIntent(intent);
	}
}