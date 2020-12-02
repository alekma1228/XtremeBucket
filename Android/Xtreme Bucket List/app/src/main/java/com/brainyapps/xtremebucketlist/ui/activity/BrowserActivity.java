package com.brainyapps.xtremebucketlist.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.ImageResult;
import com.brainyapps.xtremebucketlist.ui.adapter.ImageResultArrayAdapter;
import com.brainyapps.xtremebucketlist.ui.fragment.BrowserFragment;
import com.brainyapps.xtremebucketlist.utility.BaseTask;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.brainyapps.xtremebucketlist.utility.MessageUtil;
import com.brainyapps.xtremebucketlist.utility.ResourceUtil;
import com.google.gson.JsonObject;
import com.parse.ParseFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrowserActivity extends BaseActivity {
    public static BrowserActivity instance;

    @BindView(R.id.txt_search)
    EditText txt_search;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.layout_no_photo)
    TextView layout_no_photo;

    private ArrayList<ImageResult> imageResults;
    private ImageResultArrayAdapter imageAdapter;
    private String selectedImageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);
        instance = this;

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonUtil.hideKeyboard(instance, txt_search);
                    getDataForSearch(txt_search.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageResult imageResult = imageResults.get(position);
                selectedImageURL = imageResult.getFullUrl();
                downloadImage(imageResult);
            }
        });

        imageResults = new ArrayList<>();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gridView.setAdapter(imageAdapter);

    }
    @Override
    public void onBackPressed(){
        onBack();
    }

    @OnClick(R.id.btn_back)
    public void onBack(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        finish();
        overridePendingTransition(R.anim.exit_to_left, R.anim.exit_to_right);
    }
    private void getDataForSearch(String key){
        CommonUtil.hideKeyboard(this, txt_search);

        if (!DeviceUtil.isNetworkAvailable(this)){
            return;
        }

        if (TextUtils.isEmpty(key)){
            return;
        }

        key  = key.replace(" ", "+");
        String urlString = AppConstant.SEARCH_API_BASE_URL + key +"&searchType=image"+ "&key=" + AppConstant.GOOGLE_BROWSER_API_KEY + "&cx=" + AppConstant.CX_KEY + "&alt=json";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e("aaa", "ERROR converting String to URL " + e.toString());
        }
        Log.d("aaa", "Url = "+  urlString);


        // start AsyncTask
        GoogleSearchAsyncTask searchTask = new GoogleSearchAsyncTask();
        searchTask.execute(url);
    }

    static String result = null;
    Integer responseCode = null;
    String responseMessage = "";
    private class GoogleSearchAsyncTask extends AsyncTask<URL, Integer, String> {
        protected void onPreExecute(){ showProgressDialog(); }
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            // Http connection
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                Log.e("aaa", "Http connection ERROR " + e.toString());
            }
            try {
                responseCode = conn.getResponseCode();
                responseMessage = conn.getResponseMessage();
            } catch (IOException e) {
                Log.e("aaa", "Http getting response code ERROR " + e.toString());
            }

            Log.d("aaa", "Http response code =" + responseCode + " message=" + responseMessage);
            try {
                if(responseCode == 200) {
                    // response OK
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = rd.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    rd.close();
                    conn.disconnect();
                    result = sb.toString();
                    Log.d("aaa", "result=" + result);
                    return result;
                }else{
                    // response problem
                    String errorMsg = "Http ERROR response " + responseMessage + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID";
                    Log.e("aaa", errorMsg);
                    result = errorMsg;
                    return  result;
                }
            } catch (IOException e) {
                Log.e("aaa", "Http Response ERROR " + e.toString());
            }
            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            Log.d("aaa", "AsyncTask - onProgressUpdate, progress=" + progress);

        }
        protected void onPostExecute(String result) {
            Log.d("aaa", "AsyncTask - onPostExecute, result=" + result);
            hidProgressDialog();
            try {
                JSONObject response = new JSONObject(result);
                JSONArray imageJsonResults;
                if (response != null) {
                    imageAdapter.clear();
                    imageJsonResults = response.getJSONArray("items");
                    ArrayList<ImageResult> imageResults = ImageResult.fromJSONArray(imageJsonResults);
                    imageAdapter.addAll(imageResults);
                    if (imageResults.size() > 0){
                        layout_no_photo.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                    } else {
                        gridView.setVisibility(View.GONE);
                        layout_no_photo.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void downloadImage(final ImageResult imageResult){
        showProgressDialog();
        BaseTask.run(new BaseTask.TaskListener() {
            @Override
            public Object onTaskRunning(int taskId, Object data) {
                String ret = "";
                try {
                    java.net.URL img_value = new java.net.URL(imageResult.getFullUrl());
                    Bitmap mIcon = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
                    if (mIcon != null) {
                        ret = ResourceUtil.getAvatarFilePath();
                        ResourceUtil.saveBitmapToSdcard(mIcon, ret);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ret;
            }
            @Override
            public void onTaskResult(int taskId, Object result) {
                hidProgressDialog();
                String strResult = (String)result;
                if (TextUtils.isEmpty(strResult)){
                    MessageUtil.showError(instance, "This image can't be downloaded.");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("return", strResult);
                    intent.putExtra("url", selectedImageURL);
                    setResult(RESULT_OK, intent);

                    finish();
                    overridePendingTransition(R.anim.exit_to_left, R.anim.exit_to_right);
                }
            }
            @Override
            public void onTaskPrepare(int taskId, Object data) { }
            @Override
            public void onTaskProgress(int taskId, Object... values) { }
            @Override
            public void onTaskCancelled(int taskId) { }
        });
    }
}
