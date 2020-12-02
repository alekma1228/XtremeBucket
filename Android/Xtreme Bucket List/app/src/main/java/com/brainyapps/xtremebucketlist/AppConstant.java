package com.brainyapps.xtremebucketlist;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {
    public static String DEFAULT_USERNAME = "user@gmail.com";
    public static String DEFAULT_PASSWORD = "xtreme123";
    public static final String REPORT_EMAIL = "adrian.gora2015@yandex.com";
//    public static final String REPORT_EMAIL = "MrLAGreenXBL@gmail.com";

    public static final String SEARCH_API_BASE_URL = "https://www.googleapis.com/customsearch/v1?q=";
//    public static final String GOOGLE_APP_API_KEY = "AIzaSyAMfvzVVQP2gZ17lqYgY1lcIwYEhnJsxTg";
    public static final String GOOGLE_BROWSER_API_KEY = "AIzaSyDRMh8ZxfSyu0MXuZSsAtBEhv9aEpJsH5k";
    public static final String CX_KEY = "015656756453464077867:kcfp5ncgncc";


    public static final String APP_STORE_URL = "https://play.google.com/store/apps/details?id=com.brainyapps.xtremebucketlist";

    // Extra key
    public static final String EK_TYPE = "TYPE";
    public static final String EK_OBJECTID = "OBJECTID";
    public static final String EK_DATA = "EK_DATA";
    public static final String EK_USER_ID = "EK_USER_ID";
    public static final String EK_FILEPATH = "EK_FILEPATH";

    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String PLACE = "place";
    public static final String EMAIL = "email";
    public static final String VALUE = "value";

    public static final long MINIMUM_TIME_BETWEEN_UPDATES = 60000;
    public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10;

    public static final int CAPTURE_PHOTO = 1;
    public static final int CAPTURE_VIDEO = 2;
    public static final int CAPTURE_VIDEO_START = 3;
    public static final int CAPTURE_VIDEO_STOP = 4;

    public static final List<String> FbPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_photos");
//        add("user_birthday");
//        add("user_location");
        add("user_friends");
    }};
}
