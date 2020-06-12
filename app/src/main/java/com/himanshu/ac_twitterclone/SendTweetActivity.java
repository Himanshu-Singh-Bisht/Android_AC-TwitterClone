package com.himanshu.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;

    private ListView viewTweetListView;
    private Button btnViewTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        viewTweetListView = findViewById(R.id.viewTweetsListView);
        btnViewTweets = findViewById(R.id.btnViewTweets);

        btnViewTweets.setOnClickListener(this);
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

    @Override
    public void onClick(View v)
    {
        final ArrayList<HashMap<String , String>> tweetList = new ArrayList<>();      // arrayList of Hashmap with key and value , string and string

        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this , tweetList , android.R.layout.simple_list_item_2 ,
                new String[]{"tweetUserName" , "tweetValue"} , new int[]{android.R.id.text1 , android.R.id.text2});

        // adapter to change the arrayList into simpleAdapter which can be converted to listView.

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("myTweets");       // getting query from myTweets class.
            parseQuery.whereContainedIn("username" , ParseUser.getCurrentUser().getList("fanOf"));      // username should match with the fanOf class username

            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size() > 0 && e == null)
                    {
                        for(ParseObject tweetObjects : objects)
                        {
                            HashMap<String , String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName" , tweetObjects.getString("username"));
                            userTweet.put("tweetValue" , tweetObjects.getString("tweets"));

                            tweetList.add(userTweet);       // adding the userTweet to ArrayList tweetList
                        }

                        viewTweetListView.setAdapter(adapter);      // setting adapter for the listView
                    }
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}