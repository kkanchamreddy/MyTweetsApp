package com.kiran.mytweetsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends AppCompatActivity {

    EditText etTweetText;
    TextView tvCharCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etTweetText =(EditText)findViewById(R.id.editText);
        tvCharCount =(TextView)findViewById(R.id.tVCharCount);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.compose_toolbar);
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_logo);
        getSupportActionBar().setTitle(R.string.compose_tweet);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //optional

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
    }
}
