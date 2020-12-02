package com.brainyapps.xtremebucketlist.push;

import com.brainyapps.xtremebucketlist.listener.ExceptionListener;
import com.brainyapps.xtremebucketlist.model.ParseConstants;
import com.brainyapps.xtremebucketlist.model.ParseErrorHandler;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

public class PushNoti {

	public static void sendPush(int type, String objectId, ParseUser receiver, String message,
                                String senderId, final ExceptionListener listener) {
		HashMap<String,String> params = new HashMap<String, String>();
		params.put(ParseConstants.KEY_EMAIL, receiver.getUsername().toString());
		params.put(ParseConstants.NOTI_ALERT, message);
		params.put(ParseConstants.NOTI_TYPE, String.valueOf(type));
		params.put(ParseConstants.NOTI_DATA, objectId);
		params.put(ParseConstants.NOTI_BADGE, "Increment");
		params.put(ParseConstants.NOTI_SOUND, senderId);
		ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
			@Override
			public void done(Object object, ParseException e) {
				// handle callback
				if (listener != null)
					listener.done(ParseErrorHandler.handle(e));
			}
		});
	}
}
