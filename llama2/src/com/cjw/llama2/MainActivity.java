package com.cjw.llama2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ImageView imageView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        imageView = (ImageView) findViewById(R.id.imageView1);
        
        final TranslateAnimation upAnimation = new TranslateAnimation(0, 0, 0, -100);
        upAnimation.setDuration(1000);
        upAnimation.setFillAfter(false);
        upAnimation.setAnimationListener(new MyAnimationListener());
        
        final View mainView  = findViewById(R.id.RelativeLayout1);
        mainView.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()) {
            public void onSwipeTop() {
            	imageView.startAnimation(upAnimation);
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
    }           
	
	private class MyAnimationListener implements AnimationListener{

	    @Override
	    public void onAnimationEnd(Animation animation) {
	        imageView.clearAnimation();
	        LayoutParams lp = new LayoutParams(imageView.getWidth(), imageView.getHeight());
	        lp.setMargins(imageView.getLeft(), imageView.getTop()-100, 0, 0);
	        imageView.setLayoutParams(lp);
	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
	    }

	    @Override
	    public void onAnimationStart(Animation animation) {
	    }

	}
}
