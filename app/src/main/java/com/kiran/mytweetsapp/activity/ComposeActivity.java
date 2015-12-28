package com.kiran.mytweetsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweetText;
    TextView tvCharCount;
    Button btnTweet;

    private TwitterClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        String inReplyTo = getIntent().getStringExtra("in_reply_to");

        etTweetText =(EditText)findViewById(R.id.editText);
        tvCharCount =(TextView)findViewById(R.id.tVCharCount);
        btnTweet = (Button)findViewById(R.id.button);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.compose_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.compose_tweet);


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        client = TwitterApplication.getRestClient();
        //Add click listener for Tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tweetText = etTweetText.getText().toString();
                client.postTweet(tweetText, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("Compose Success", response.toString());
                        // Prepare data intent
                        Intent data = new Intent();

                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); // closes the activity, pass data to parent
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("Compose Failure", errorResponse.toString());
                    }

                });
            }
        });

        //Add  listener to keep track of how many characters left
        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                tvCharCount.setText(140 - s.toString().length() + "/140");
            }
        });

        if(inReplyTo != null) {
            etTweetText.setText(inReplyTo);
        }


    }
}
