package com.ishraq.gosport;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

/**
 * Created by hp on 02/01/2016.
 */
public class SingleSubject extends AppCompatActivity {
    TextView titleTV, contentTV, count;
    ImageView imageURL;
    String title, content, image_url;
    Button buyButton;
    int r =7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_activity);
        Bundle extras = getIntent().getExtras();

        title = extras.getString("sentTitle");
        content = extras.getString("sentContent");
        image_url = getIntent().getStringExtra("sentImage");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        design();
    }

    public void design() {
        titleTV = (TextView) findViewById(R.id.titleTV);
        contentTV = (TextView) findViewById(R.id.contentTV);
        imageURL = (ImageView) findViewById(R.id.imageURL);

        count = (TextView) findViewById(R.id.count);
        buyButton = (Button) findViewById(R.id.buyButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count.setText("" + r++);
            }
        });
        Picasso.with(getApplicationContext()).load(image_url)
                .error(R.drawable.back)
                .placeholder(R.drawable.back)
                .into(imageURL);


        titleTV.setText(title);
        contentTV.setText(content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                shareText(title, content + "\n" + image_url + "\n" + "حمل تطبيق بل أحياءٌ من هنا \n" + "https://play.google.com/store/apps/details?id=com.ishraq.kessatshaheed");
                shareText(title, content + "\n" + image_url);
            }
        });

    }

    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent, "Share"));
    }
}
