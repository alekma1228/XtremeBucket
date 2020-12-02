package com.brainyapps.xtremebucketlist.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.brainyapps.xtremebucketlist.XtremeBucketListApp;
import com.brainyapps.xtremebucketlist.listener.OnUploadCompletedListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class CommonUtil {
    /*
	 * Hide keyboard
	 */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
     * Mail
     */
    // check validation
    public static boolean isValidEmail(String emailAddr) {
        return !TextUtils.isEmpty(emailAddr) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches();
    }

    // check include number
    public static boolean isIncludeNumber(String value) {
        return value.matches(".*\\d+.*");
    }

    // check include letter
    public static boolean isIncludeLetter(String value) {
        return value.matches(".*[a-zA-Z]+.*");
    }
    public static boolean isIncludeLowercaseLetter(String value) {
        return value.matches(".*[a-z]+.*");
    }
    public static boolean isIncludeUppercaseLetter(String value) {
        return value.matches(".*[A-Z]+.*");
    }
    public static byte[] readStream(String path) {
        byte[] bytes = null;

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];

            for (int readNum; (readNum = fis.read(b)) != -1; ) {
                bos.write(b, 0, readNum);
            }

            bytes = bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bytes;
    }
    public static void uploadFileToParse(byte[] data, final OnUploadCompletedListener listener) {
        // Create the ParseFile
        final ParseFile file = new ParseFile("file", data);

        // Upload the image into Parse Cloud
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (listener != null) {
                    listener.onFileUploadFinished(file, e);
                }
            }
        });
    }

    // send email
    public static void SendEmail(Context context, ArrayList<String> to, String subject, String body, String attachment_url) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        if (TextUtils.isEmpty(attachment_url)) {
            intent.setType("message/rfc822");

        } else {
            intent.setType("vnd.android.cursor.dir/email");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + attachment_url));
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (to != null && to.size() > 0) {
            String[] toArr = to.toArray(new String[to.size()]);
            intent.putExtra(Intent.EXTRA_EMAIL, toArr);
        }

        context.startActivity(Intent.createChooser(intent, "Send Information"));
    }
    public static void SendEmail(Context context, String toEmail, String subject, String body, String attachment_url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:"
                + toEmail
                + "?subject=" + subject + "&body=" + body +"");
        intent.setData(data);
        if (!TextUtils.isEmpty(attachment_url)) {
            Uri photoURI = Uri.fromFile(new File(attachment_url));
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        }
        context.startActivity(intent);
    }
    /* SMS */
    public static void SendSMS(Context context, String body) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        if (!TextUtils.isEmpty(body))
            intent.putExtra("sms_body", body);

        context.startActivity(intent);
    }
    public static void sendSmsInBackground(String phoneNumber, String msg) {
        SmsManager sms = SmsManager.getDefault();
        if (!TextUtils.isEmpty(phoneNumber)) {
            sms.sendTextMessage(phoneNumber, null, msg, null, null);
        }
    }

    // calling
    @SuppressLint("MissingPermission")
    public static void Call(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
    // show web browser
    public static void ShowBrowser(Context context, String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent();
        browserIntent.setAction(Intent.ACTION_VIEW);
        browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        browserIntent.setData(Uri.parse(url));
        context.startActivity(browserIntent);
    }
    // show map
    public static void showMap(Context context, double latitude1, double longitude1){
//        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+latitude1+","+longitude1+"&daddr="+latitude2+","+longitude2;
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+latitude1+","+longitude1;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(Intent.createChooser(intent, "Select an application"));
    }

    /* Launch market */
    public static void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + XtremeBucketListApp.getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            XtremeBucketListApp.getContext().startActivity(goToMarket);

        } catch (Exception e) {
            // open with browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + XtremeBucketListApp.getContext().getPackageName()));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            XtremeBucketListApp.getContext().startActivity(browserIntent);
        }
    }

    // parse file url change
    public static String getCDNUrlFromParseUrl(String path) {
        path = path.replace("https://parse.brainyapps.com:20019", "https://d2zvprcpdficqw.cloudfront.net/process/10019");
        return path;
    }

    // Double To Double
    public static Double D2D(double value) {
        Double result = Double.valueOf((int)(value * 100)) / 100;
        return result;
    }

    public static int TYPE_ALL_PERMISSION = 0;
    public static int TYPE_CAMERA_PERMISSION =1;
    public static int TYPE_LOCATION_PERMISSION =2;
    public static int TYPE_STORAGE_PERMISSION =3;
    public static int TYPE_CONTACT_PERMISSION =4;
    public static int TYPE_SMS_PERMISSION =5;
    public static boolean verifyStoragePermissions(final int type, final Activity activity) {
        // Check if we have write permission
        int permission0 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        int permission3 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission4 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int permission5 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);
        if (type == TYPE_ALL_PERMISSION) {
            if (permission0 != PackageManager.PERMISSION_GRANTED
                    || permission1 != PackageManager.PERMISSION_GRANTED
                    || permission2 != PackageManager.PERMISSION_GRANTED
                    || permission3 != PackageManager.PERMISSION_GRANTED
                    || permission4 != PackageManager.PERMISSION_GRANTED
                    || permission5 != PackageManager.PERMISSION_GRANTED) {

//                ActivityCompat.requestPermissions(
//                        activity,
//                        PERMISSIONS,
//                        REQUEST_PERMISSION
//                );

                return false;
            }
        } else if (type == TYPE_CAMERA_PERMISSION) {
            if (permission0 != PackageManager.PERMISSION_GRANTED
                    || permission1 != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } else if (type == TYPE_STORAGE_PERMISSION) {
            if (permission0 != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } else if (type == TYPE_LOCATION_PERMISSION) {
            if (permission3 != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } else if (type == TYPE_CONTACT_PERMISSION) {
            if (permission4 != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        } else if (type == TYPE_SMS_PERMISSION) {
            if (permission5 != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
