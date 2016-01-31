package com.kiran.mytweetsapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kiran.mytweetsapp.R;
import com.kiran.mytweetsapp.TwitterApplication;
import com.kiran.mytweetsapp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by kkanchamreddy on 12/27/15.
 */
public class ComposeFragment  extends DialogFragment {

    EditText etTweetText;
    TextView tvCharCount;
    Button btnTweet;


    private TwitterClient client;
    public ComposeFragment(){}

    public static ComposeFragment newInstance(String ineplyTo) {
        ComposeFragment frag = new ComposeFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("in_reply_to", ineplyTo);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String inReplyTo = getArguments().getString("in_reply_to");
        etTweetText =(EditText)view.findViewById(R.id.editText);
        tvCharCount =(TextView)view.findViewById(R.id.tVCharCount);
        btnTweet = (Button)view.findViewById(R.id.button);

        client = TwitterApplication.getRestClient();
        //Add click listener for Tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tweetText = etTweetText.getText().toString();
                client.postTweet(tweetText, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Prepare data intent
                        Intent data = new Intent();
                        getDialog().dismiss();
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

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
