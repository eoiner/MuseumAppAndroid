package com.example.museumexperience;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MuseumSplashActivity extends MainActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ImageView splashImage = (ImageView)findViewById(R.id.splashImage);
        splashImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),MainboardActivity.class));
				finish();
			}
		});
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() { 
             public void run() { 
            	 startActivity(new Intent(getApplicationContext(),MainboardActivity.class));
            	 finish();
             } 
        }, 4000); 
    }
    
    
}