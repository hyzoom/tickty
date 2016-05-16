package com.ishraq.gosport;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import static com.ishraq.gosport.CommonUtilities.SENDER_ID;
import static com.ishraq.gosport.CommonUtilities.SERVER_URL;

/**
 * Created by root on 5/14/16.
 */
public class LogIn extends Activity {

    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Internet detector
    com.ishraq.gosport.ConnectionDetector cd;
    AsyncTask<Void, Void, Void> mRegisterTask;
    // UI elements
    EditText txtName;
    EditText txtPass;
    final String url = "http://tickety.esy.es/logInApp.php";
    // Register button
    Button logIn;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        cd = new com.ishraq.gosport.ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(LogIn.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(LogIn.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtPass = (EditText) findViewById(R.id.txtPass);
        logIn = (Button) findViewById(R.id.btnLogIn);

		/*
         * Click event on Register button
		 * */
        logIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Read EditText dat
                String name = txtName.getText().toString();
                String pass = txtPass.getText().toString();

                loginAsync(name, pass);
            }
        });
    }

    public void loginAsync(final String name, final String pass) {
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
                log(name, pass);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
//                pDialog.dismiss();
                Toast.makeText(LogIn.this, "Please wait", Toast.LENGTH_SHORT).show();
                mRegisterTask = null;
            }
        };
        mRegisterTask.execute(null, null, null);
    }

    void log(String name, String pass) {
//        shaheed.esy.es/create_subject_from_user.php?title=test&content=test&image_url=test
        String randomUserURL = url;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("password", pass));

        JSONObject json = makeHttpRequest(randomUserURL, "GET", params);
        if (json != null) {
            Log.e("hazem", json + "");
            String ss = json + "";
            if (ss.equals("{\"success\":1}")) {
                Intent i = new Intent(LogIn.this, FeedListActivity.class);
                startActivity(i);
            } else if (ss.equals("{\"success\":0}")) {

            }

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
}
