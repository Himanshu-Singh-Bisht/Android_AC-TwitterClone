package com.himanshu.ac_twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Flm716IVvo88mMQTNIGCHlsOWjtBZmY5lKme45fI")          // paste applicationId
                .clientKey("6ZEpYs73vZBP7BsRJLHRjwm57g2xOIffL9J8wBd4")              // paste client key
                .server("https://parseapi.back4app.com")                            // server address
                .build()
        );
    }
}
