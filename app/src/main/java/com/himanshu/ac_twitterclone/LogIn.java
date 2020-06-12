package com.himanshu.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity implements View.OnClickListener
{
    private EditText edtLogInEmail ,edtLogInPassword;
    private Button btnLogInSignUp , btnLogInLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setTitle("LogIn");

        edtLogInEmail = findViewById(R.id.edtLogInEmail);
        edtLogInPassword = findViewById(R.id.edtLogInPassword);

        btnLogInSignUp = findViewById(R.id.btnLogInSignUp);
        btnLogInLogIn = findViewById(R.id.btnLogInLogIn);

        edtLogInPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnLogInLogIn);
                }
                return false;
            }
        });
        btnLogInSignUp.setOnClickListener(this);
        btnLogInLogIn.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null)
        {
            ParseUser.getCurrentUser().logOut();
//            Intent intent = new Intent(LogIn.this , TwiiterActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnLogInLogIn :
                if(edtLogInEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(LogIn.this , "Eamil Id is required!!!",
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR,true).show();
                }
                else if(edtLogInPassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(LogIn.this , "Password is required!!!",
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR,true).show();
                }
                else {
                    ParseUser.logInInBackground(edtLogInEmail.getText().toString(), edtLogInPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
//                                final ProgressDialog dialog = new ProgressDialog(LogIn.this);
//                                dialog.setMessage("Logging In...");
//                                dialog.show();

                                FancyToast.makeText(LogIn.this, user.getUsername() + " is Logged in.",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                                Intent intent = new Intent(LogIn.this, TwiiterActivity.class);
                                startActivity(intent);

//                                dialog.dismiss();       // to dismiss the dialog box

                                finish();       // to finish current activity
                            } else {
                                FancyToast.makeText(LogIn.this, e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }
                        }
                    });
                }
                break;

            case R.id.btnLogInSignUp :
                Intent intent = new Intent(LogIn.this , SignUp.class);
                startActivity(intent);

                finish();       // to finish current activity
                break;
        }
    }

    // TO HIDE THE KEYBOARD WHEN USER TAPS ON THE LAYOUT
    public void rootLayoutTapped(View view)
    {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}