package com.brainyapps.xtremebucketlist.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brainyapps.xtremebucketlist.AppConstant;
import com.brainyapps.xtremebucketlist.AppGlobals;
import com.brainyapps.xtremebucketlist.R;
import com.brainyapps.xtremebucketlist.model.SearchResult;
import com.brainyapps.xtremebucketlist.ui.activity.BucketDetailActivity;
import com.brainyapps.xtremebucketlist.ui.activity.MainActivity;
import com.brainyapps.xtremebucketlist.ui.activity.NewBucketActivity;
import com.brainyapps.xtremebucketlist.ui.adapter.BrowserItemsAdapter;
import com.brainyapps.xtremebucketlist.utility.CommonUtil;
import com.brainyapps.xtremebucketlist.utility.DeviceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BrowserFragment extends Fragment {
    private MainActivity mainActivity;
    private View mView = null;
    public static LayoutInflater inflater;

    @BindView(R.id.tab_all)
    RelativeLayout tab_all;
    @BindView(R.id.txt_tab_all)
    TextView txt_tab_all;
    @BindView(R.id.view_all)
    View view_all;

    @BindView(R.id.tab_images)
    RelativeLayout tab_images;
    @BindView(R.id.txt_tab_images)
    TextView txt_tab_images;
    @BindView(R.id.view_images)
    View view_images;

    @BindView(R.id.tab_maps)
    RelativeLayout tab_maps;
    @BindView(R.id.txt_tab_maps)
    TextView txt_tab_maps;
    @BindView(R.id.view_maps)
    View view_maps;

    @BindView(R.id.tab_news)
    RelativeLayout tab_news;
    @BindView(R.id.txt_tab_news)
    TextView txt_tab_news;
    @BindView(R.id.view_news)
    View view_news;

    @BindView(R.id.txt_search)
    EditText txt_search;
    @BindView(R.id.listView)
    ListView listView;

    private ArrayList<SearchResult> data = new ArrayList<>();
    private BrowserItemsAdapter browserItemsAdapter;

    public void setMainActivity(MainActivity activity) { mainActivity = activity; }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_browser, container, false);

        this.inflater = inflater;

        mainActivity = (MainActivity) getActivity();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = MainActivity.instance;
    }
    @Override
    public void onResume(){
        super.onResume();

        if (mView == null)
            return;
        if (inflater == null)
            return;

        if (listView == null)
            initUI(mView);

//        onRefresh();
    }
    public void onRefresh(){
        data.clear();
        browserItemsAdapter.setData(data);
        browserItemsAdapter.notifyDataSetChanged();
        txt_search.setText("");
    }
    private void initUI(View view){
        ButterKnife.bind(this, mView);
        browserItemsAdapter = new BrowserItemsAdapter(mainActivity);
        browserItemsAdapter.setData(data);
        listView.setAdapter(browserItemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(mainActivity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_web);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                TextView btn_open = (TextView) dialog.findViewById(R.id.btn_open);
                TextView btn_open_new_tab = (TextView) dialog.findViewById(R.id.btn_open_new_tab);
                TextView btn_copy = (TextView) dialog.findViewById(R.id.btn_copy);
                TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
                btn_open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SearchResult searchResult = browserItemsAdapter.getItem(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchResult.getLink()));
                        startActivity(browserIntent);
                    }
                });
                btn_open_new_tab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SearchResult searchResult = browserItemsAdapter.getItem(position);
                        Intent intent = new Intent(mainActivity, NewBucketActivity.class);
                        intent.putExtra(AppConstant.EK_DATA, searchResult.getSnippet());
                        mainActivity.startActivity(intent);
                        mainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
//                        mainActivity.bucketListTabPressed();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonUtil.hideKeyboard(mainActivity, txt_search);
                    getDataForSearch(txt_search.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        CommonUtil.hideKeyboard(mainActivity, txt_search);
    }

    private void resetSubTabs(){
        txt_tab_all.setTextColor(getResources().getColor(R.color.gray));
        txt_tab_images.setTextColor(getResources().getColor(R.color.gray));
        txt_tab_maps.setTextColor(getResources().getColor(R.color.gray));
        txt_tab_news.setTextColor(getResources().getColor(R.color.gray));

        view_all.setBackgroundColor(getResources().getColor(R.color.gray));
        view_images.setBackgroundColor(getResources().getColor(R.color.gray));
        view_maps.setBackgroundColor(getResources().getColor(R.color.gray));
        view_news.setBackgroundColor(getResources().getColor(R.color.gray));
    }
    @OnClick(R.id.tab_all)
    public void onTabAll(){
        resetSubTabs();
        txt_tab_all.setTextColor(getResources().getColor(R.color.blue_dark));
        view_all.setBackgroundColor(getResources().getColor(R.color.blue_dark));
    }
    @OnClick(R.id.tab_images)
    public void onTabImages(){
        resetSubTabs();
        txt_tab_images.setTextColor(getResources().getColor(R.color.blue_dark));
        view_images.setBackgroundColor(getResources().getColor(R.color.blue_dark));
    }
    @OnClick(R.id.tab_maps)
    public void onTabMaps(){
        resetSubTabs();
        txt_tab_maps.setTextColor(getResources().getColor(R.color.blue_dark));
        view_maps.setBackgroundColor(getResources().getColor(R.color.blue_dark));
    }
    @OnClick(R.id.tab_news)
    public void onTabNews(){
        resetSubTabs();
        txt_tab_news.setTextColor(getResources().getColor(R.color.blue_dark));
        view_news.setBackgroundColor(getResources().getColor(R.color.blue_dark));
    }

    private void getDataForSearch(String key){
        CommonUtil.hideKeyboard(mainActivity, txt_search);

        if (!DeviceUtil.isNetworkAvailable(mainActivity)){
            return;
        }

        if (TextUtils.isEmpty(key)){

            return;
        }
        key  = key.replace(" ", "+");
        String urlString = AppConstant.SEARCH_API_BASE_URL + key + "&key=" + AppConstant.GOOGLE_BROWSER_API_KEY + "&cx=" + AppConstant.CX_KEY + "&alt=json";
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
        protected void onPreExecute(){
            mainActivity.showProgressDialog();
        }
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
            mainActivity.hidProgressDialog();
            try {
                JSONObject response = new JSONObject(result);
                JSONArray imageJsonResults;
                if (response != null) {
                    imageJsonResults = response.getJSONArray("items");
                    data.clear();
                    data = SearchResult.fromJSONArray(imageJsonResults);
                    browserItemsAdapter.setData(data);
                    browserItemsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }
}
