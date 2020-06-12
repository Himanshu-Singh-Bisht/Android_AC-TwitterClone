package com.himanshu.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwiiterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;  // to populate the listview with the arrayList we need ArrayAdapter

    private String followedUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twiiter);

        FancyToast.makeText(this , "Welcome " + ParseUser.getCurrentUser().getUsername() ,
                FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();

        // setting array adapter for the arrayList
        arrayAdapter = new ArrayAdapter(TwiiterActivity.this, android.R.layout.simple_list_item_checked, arrayList);
        // we have used checked type row in listView as want the users to have check option.

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);       // as item of the listView will have two choices (checked or unchecked).



        // ParseQuery to get the users from the server.
        try {  // used try and catch because there might be no user except current user then it will give us error.
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());      // as we don't want to show the current user.
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null) {
                        if (users.size() > 0) {
                            for (ParseUser user : users) {
                                arrayList.add(user.getUsername());          // adding the user to the arrayList
                            }
                            listView.setAdapter(arrayAdapter);          // now set arrayAdapter for the listView

                            // for checking the users followed by the current user even after refreshing the app.
                            for(String twiiterUser : arrayList)
                            {
                                if(ParseUser.getCurrentUser().getList("fanOf") != null)             // as if it is null then we can't compare its elements
                                {
                                    if(ParseUser.getCurrentUser().getList("fanOf").contains(twiiterUser))
                                    {
                                        followedUser = followedUser + twiiterUser + "\n";
                                        listView.setItemChecked(arrayList.indexOf(twiiterUser) , true);         // checking the index of arraylist which is already followed.

                                    }
                                }
                            }

                            FancyToast.makeText(TwiiterActivity.this , ParseUser.getCurrentUser().getUsername() + " is following - " + followedUser,
                                    FancyToast.LENGTH_SHORT , FancyToast.INFO , true).show();
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // setting onItemClickListener
        listView.setOnItemClickListener(this);
    }


    // TO CREATE OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    // TO SELECT THE PARTICULAR OPTION FROM THE MENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logoutUserItem :

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Intent intent = new Intent(TwiiterActivity.this , SignUp.class);
                            startActivity(intent);
                            finish();       // to finish current activity
                        }
                        else
                        {
                            e.getStackTrace();
                        }
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }


    // AS WHEN THE ITEM (USER) OF LISTVIEW WILL BE TAPPED IT WILL RUN THIS METHOD
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        CheckedTextView checkedTextView = (CheckedTextView) view;       // as the listView will contain the items of CheckedTextView type.

        if(checkedTextView.isChecked())
        {
            FancyToast.makeText(TwiiterActivity.this , arrayList.get(position) + " is Followed now !!!" ,
                    FancyToast.LENGTH_SHORT , FancyToast.INFO, true).show();

            ParseUser.getCurrentUser().add("fanOf" , arrayList.get(position));
        }
        else
        {
            FancyToast.makeText(TwiiterActivity.this , arrayList.get(position) + " is Unfollowed now !!!" ,
                    FancyToast.LENGTH_SHORT , FancyToast.INFO, true).show();


            // to unfollow the user
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));

            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf" , currentUserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                if(e == null)
                {
                    FancyToast.makeText(TwiiterActivity.this , "Saved" ,
                            FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                }
            }
        });
    }
}