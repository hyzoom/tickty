package com.ishraq.gosport;

/**
 * Created by hp on 01/01/2016.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ishraq.gosport.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ishraq.gosport.CommonUtilities.EXTRA_MESSAGE;
import static com.ishraq.gosport.CommonUtilities.SENDER_ID;

public class FeedListActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;
    AsyncTask<Void, Void, Void> mRegisterTask;

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    final String url = "http://tickety.esy.es/get_all_subjects.php";//http://go-sport.esy.es/sport/get_sub_categ.php?categ_id=2
    SwipeRefreshLayout swipeView;
    private Dialog dialog;
    EditText shaheed_name, shaheed_image_url, shahed_story;
    String shaheed_nameSt, shaheed_image_urlSt, shahed_storySt;
    private ProgressDialog pDialog;
    Spinner categSpinner;

    TextView lblMessage;
    Button sendRandom;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    public static String name;
    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);
        registerGCM();
        pDialog = new ProgressDialog(FeedListActivity.this);
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        dialog = new Dialog(FeedListActivity.this);

        categSpinner = (Spinner) findViewById(R.id.categSpinner);
        categSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (categSpinner.getSelectedItemPosition() == 0) {
                    new AsyncHttpTask().execute(url);
                } else {
                    new AsyncHttpTask().execute("http://tickety.esy.es/get_sub_categ.php?categ_id=" + (categSpinner.getSelectedItemPosition() + 1));
                }
                swipeView.setRefreshing(false);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // Downloading data from below url
        //http://javatechig.com/?json=get_recent_posts&count=45
        new AsyncHttpTask().execute(url);


    }

    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        String idArrays[] = {"1", "10", "11", "12", "13"};

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (pos == 0) {
                new AsyncHttpTask().execute(url);
            } else {
                new AsyncHttpTask().execute("http://tickety.esy.es/get_sub_categ.php?categ_id=" + idArrays[pos]);
            }

//            Toast.makeText(parent.getContext(),
//                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView =
        //       (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(),
                        SearchResultsActivity.class)));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_chat) {
            Toast.makeText(FeedListActivity.this, "Go TO Chat Activity", Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_edit_account) {
            Toast.makeText(FeedListActivity.this, "Go TO Update Activity", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addDialog() {
//        shaheed.esy.es/create_subject_from_user.php?title=test&content=test&image_url=test
        dialog.setContentView(R.layout.add_shaheed);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        Display display = this.getWindowManager().getDefaultDisplay();
        window.setLayout(display.getWidth() * 5 / 6,
                display.getHeight() * 2 / 3);
        shaheed_name = (EditText) dialog.findViewById(R.id.shaheed_name);
        shaheed_image_url = (EditText) dialog.findViewById(R.id.shaheed_image_url);
        shahed_story = (EditText) dialog.findViewById(R.id.shahed_story);

        Button addButton = (Button) dialog.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shaheed_nameSt = shaheed_name.getText().toString();
                shaheed_image_urlSt = shaheed_image_url.getText().toString();
                shahed_storySt = shahed_story.getText().toString();
                if (shaheed_nameSt.equals("") || shaheed_image_urlSt.equals("") || shahed_storySt.equals("")) {
                    Toast.makeText(FeedListActivity.this, "Fill all data", Toast.LENGTH_LONG).show();
                } else {
                    addShaheedAsync(shaheed_nameSt, shaheed_image_urlSt, shahed_storySt);
                    dialog.dismiss();
                }
            }
        });

    }

    public void addShaheedAsync(final String name, final String image, final String content) {
        mRegisterTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                pDialog = new ProgressDialog(FeedListActivity.this);
//                pDialog.setMessage("جاري تنفيذ طلبك");
//                pDialog.setIndeterminate(false);
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // Register on our server
                // On server creates a new user
                addShaheed(name, image, content);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
//                pDialog.dismiss();
                mRegisterTask = null;
            }
        };
        mRegisterTask.execute(null, null, null);
    }

    void addShaheed(String name, String image, String content) {
//        shaheed.esy.es/create_subject_from_user.php?title=test&content=test&image_url=test
        String randomUserURL = "http://shaheed.esy.es/create_subject_from_user.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("title", name));
        params.add(new BasicNameValuePair("image_url", image));
        params.add(new BasicNameValuePair("content", content));

        JSONObject json = makeHttpRequest(randomUserURL, "GET", params);
        if (json != null) {
            try {
                JSONArray jsonArray = json
                        .getJSONArray("success");

//                for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject c = jsonArray.getJSONObject(0);
//
//                    FeedItem item = new FeedItem();
//                    item.setTitle(c.getString("title"));
//                    item.setContent(c.getString("content"));
//                    item.setThumbnail(c.getString("image_url"));
//                    feedsList.add(item);
//            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent, "Share"));
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            pDialog.show();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    Log.e(">>>>> ", response + "");
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
//                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (result == 1) {

                adapter = new MyRecyclerAdapter(FeedListActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(FeedListActivity.this, "Problem, try again later", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            JSONArray posts = response.optJSONArray("subjects");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setTitle(post.optString("title"));
                item.setContent(post.optString("content"));
                item.setThumbnail(post.optString("image_url"));

                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject makeHttpRequest(String url, String method,
                                             List<NameValuePair> params) {
        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public void registerGCM() {


        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(FeedListActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        Intent i = getIntent();

        name = i.getStringExtra("name");
        email = i.getStringExtra("email");

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

//        lblMessage = (TextView) findViewById(R.id.lblMessage);
//
//        sendRandom = (Button) findViewById(R.id.send_random);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.e("regId >>>>", regId);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            Log.e("regId1", regId);
            GCMRegistrar.register(this, SENDER_ID);
        } else {

            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Log.e("regId2", regId);
//                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                Log.e("regId3", regId);
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
//            lblMessage.append(newMessage + "\n");
//            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

}