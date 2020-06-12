package com.himanshu.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity  implements View.OnClickListener {


    private EditText edtSignUpEmail , edtSignUpName , edtSignUpPassword;
    private Button btnSignUp , btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");        // to set string n action bar.

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpName = findViewById(R.id.edtSignUpName);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignUp);
                }
                return false;
            }
        });
        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null)
        {
//            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(SignUp.this , TwiiterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnSignUp :

                if(edtSignUpEmail.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this , "Eamil Id is required!!!",
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR,true).show();
                }
                else if(edtSignUpName.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this , "Name is required!!!",
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR,true).show();
                }
                else if(edtSignUpPassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this , "Password is required!!!",
                            FancyToast.LENGTH_SHORT , FancyToast.ERROR,true).show();
                }
                else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtSignUpEmail.getText().toString());
                    appUser.setUsername(edtSignUpName.getText().toString());
                    appUser.setPassword(edtSignUpPassword.getText().toString());


                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Signing Up " + edtSignUpName.getText().toString());
                    dialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                            {
                                FancyToast.makeText(SignUp.this, appUser.getUsername() + " is Signed Up.",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                                Intent intent = new Intent(SignUp.this , TwiiterActivity.class);
                                startActivity(intent);


                            }
                            else
                            {
                                FancyToast.makeText(SignUp.this, e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }

                            dialog.dismiss();       // to dismiss the dialog
                        }
                    });
                }
                break;

            case R.id.btnLogIn:

                Intent intent = new Intent(SignUp.this , LogIn.class);
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
