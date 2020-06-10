package com.himanshu.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
                ParseUser.logInInBackground(edtLogInEmail.getText().toString(), edtLogInPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e)
                    {
                        if(user != null &&  e == null)
                        {
                            FancyToast.makeText(LogIn.this , user.getUsername() + " is Logged in." ,
                                    FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

                            Intent intent = new Intent(LogIn.this , TwiiterActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            FancyToast.makeText(LogIn.this , e.getMessage(),
                                    FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                        }
                    }
                });
                break;
            case R.id.btnLogInSignUp :

                break;
        }
    }
}