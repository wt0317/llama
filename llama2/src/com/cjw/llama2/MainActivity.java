package com.cjw.llama2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ImageView imageView;
	Node[][] grid;
	
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
//            	imageView.startAnimation(upAnimation);
//                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            	evaluate(false, -1);
            }
            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            	evaluate(true, 1);
            }
            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            	evaluate(true, -1);
            }
            public void onSwipeBottom() {
//                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            	evaluate(false, 1);
            }
        });
        
        grid = new Node[4][4];
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		grid[i][j] = new Node(0,false);
        	}
        }
        int x = (int) (Math.random()*4 % 4);
        Log.d("WI", x+"");
        int y = (int) (Math.random()*4 % 4);
        Log.d("WI", y+"");
        grid[x][y] = new Node(1, false);
        int x1, y1;
        do {
        	x1 = (int) (Math.random()*4 % 4);
            y1 = (int) (Math.random()*4 % 4);
        } while(x1 == x && y1 == y);
        Log.d("WI", x1+"");
        Log.d("WI", y1+"");
        grid[x1][y1] = new Node(1,false);
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		int resId = getResources().getIdentifier(String.format("textView%s%s", i+1, j+1), "id", getPackageName());
        		TextView textView = (TextView) findViewById(resId);
//        		Log.d("WI", textView.getText()+"");
        		textView.setText(grid[i][j].value+"");
        	}
        }
        
//        TextView textView = (TextView) findViewById(R.id.textView12);
//		textView.setText("hello");
        
        
    }
	
	private class Node {
		public int value = 0;
		public boolean flag = false;
		
		public Node(int value, boolean flag) {
			this.value = value;
			this.flag = flag;
		}
	}
	
	private void evaluate(boolean orientation, int increment) {
//		orientation true => horizontal
//		increment negative => down
		
		int temp = (increment == 1) ? 1 : 2;
		for (int a = 0; a < 4; a++) {
			for (int b = temp; b > -1 && b < 4; b+=increment){
				Node current;
				Node neighbour;
				if (orientation) {
					current = grid[a][b];
					neighbour = grid[a][b+increment];
				} else {
					current = grid[b][a];
					neighbour = grid[b+increment][a];
				}
				
				if (current.value > 0 && neighbour.value == 0) {
					// move
					neighbour.value = current.value;
					current.value = 0;
					b = b - increment * 2;
					Log.d("cw","going to " + b);
					
				} else if (!neighbour.flag && neighbour.value == current.value && current.value > 0) {
					// crush
					neighbour.value++;
					current.value = 0;
					neighbour.flag = true;
					Log.d("cw","crush!");
				}
				
				Log.d("cw","finished");
			}
		}
		for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		int resId = getResources().getIdentifier(String.format("textView%s%s", i+1, j+1), "id", getPackageName());
        		TextView textView = (TextView) findViewById(resId);
        		textView.setText(grid[i][j].value+"");
        		grid[i][j].flag = false;
        	}
        }
	}
	
	private void move() {
		
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
