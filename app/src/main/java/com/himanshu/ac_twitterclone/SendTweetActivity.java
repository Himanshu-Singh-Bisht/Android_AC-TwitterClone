package com.himanshu.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity {

    private EditText edtTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTweet = findViewById(R.id.edtTweet);
    }

    public void sendTweet(View view)            // always make the function public which send via onClick from .xml file.
    {
        ParseObject parseObject = new ParseObject("myTweets");     // creating new parse object in My Tweets class

        // adding tweet in column Tweets for parseObject
        parseObject.put("tweets" , edtTweet.getText().toString());
        // adding username in cloumn username for parseObject
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

        final ProgressDialog progressDialog = new ProgressDialog(SendTweetActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // saving parseObbject in background
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    FancyToast.makeText(SendTweetActivity.this ,
                            ParseUser.getCurrentUser().getUsername() + "'s Tweet " + "(" +
                            edtTweet.getText().toString() + ")" + " is saved." ,
                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                }
                else
                {
                    FancyToast.makeText(SendTweetActivity.this , e.getMessage() ,
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                }
                progressDialog.dismiss();       // to dismiss the progress dialog.
            }
        });
    }
}