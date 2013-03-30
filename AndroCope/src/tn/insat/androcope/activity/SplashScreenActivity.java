package tn.insat.androcope.activity;

import tn.insat.androcope.R;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.splashscreen.CountDown;

public class SplashScreenActivity extends Activity {
	
	 public void onAttachedToWindow() {
			super.onAttachedToWindow();
			Window window = getWindow();
			window.setFormat(PixelFormat.RGBA_8888);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.splash);
    	CountDown _tik;
    	_tik=new CountDown(1000,1000,this,AndroCopeActivity.class);
    	_tik.start();
    	startAnimations();
    }
    
    private void startAnimations() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);
        
        animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        animation.reset();
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.clearAnimation();
        logo.startAnimation(animation);
    }
   
}
