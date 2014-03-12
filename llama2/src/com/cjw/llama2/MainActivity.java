package com.cjw.llama2;

import java.util.ArrayList;

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
	ArrayList <Point> pool;
	
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
            	evaluate(false, 1);
            }
            public void onSwipeRight() {
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            	evaluate(true, -1);
            }
            public void onSwipeLeft() {
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            	evaluate(true, 1);
            }
            public void onSwipeBottom() {
//                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            	evaluate(false, -1);
            }
        });
        
        grid = new Node[4][4];
        pool = new ArrayList <Point> ();
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		grid[i][j] = new Node(0,false,i,j);
        		pool.add(new Point(i,j));
        	}
        }

        seed(2);
        
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		int resId = getResources().getIdentifier(String.format("textView%s%s", i, j), "id", getPackageName());
        		TextView textView = (TextView) findViewById(resId);
        		textView.setText(grid[i][j].value+"");
        	}
        }
        
        
    }
	
	private class Node {
		public int value = 0;
		public boolean flag = false;
		public int x = 0;
		public int y = 0;
		
		public Node(int value, boolean flag, int x, int y) {
			this.value = value;
			this.flag = flag;
			this.x = x;
			this.y = y;
		}
	}
	
	private class Point {
		public int x;
		public int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private void seed(int iterations) {
		Log.d("cw",""+pool.size());
		int size = pool.size();
		int index = (int) (Math.random() * size % size);
		
		Point seeded = pool.get(index);
		grid[seeded.x][seeded.y].value = 1;
		
		pool.remove(index);
		
		if (iterations > 1) {
			seed(iterations-1);
		}
	}
	
	private void evaluate(boolean horizontal, int increment) {
//		horizontal true => horizontal
//		increment negative => down
		
		int temp = (increment == 1) ? 1 : 2;
		for (int a = 0; a < 4; a++) {
			for (int b = temp; b > -1 && b < 4; b+=increment){
				Node current;
				Node neighbour;
				if (horizontal) {
					current = grid[a][b];
					neighbour = grid[a][b-increment];
				} else {
					current = grid[b][a];
					neighbour = grid[b-increment][a];
				}
				
				if (current.value > 0) {
					if (neighbour.value == 0) {
						// move
						neighbour.value = current.value;
						current.value = 0;
						
						for (int k = 0; k < pool.size(); k++) {
							Point pt = pool.get(k);
							if (pt.x == neighbour.x && pt.y == neighbour.y) {
								pool.remove(k);
								break;
							}
						}
						
						pool.add(new Point(current.x, current.y));
						
						if (b-increment > 0 && b-increment < 3)
							b = b - increment * 2;
					} else if (!neighbour.flag && neighbour.value == current.value) {
						// crush
						neighbour.value++;
						current.value = 0;
						neighbour.flag = true;
						
						pool.add(new Point(current.x, current.y));
					}
				}
			}
		}
		seed(1);
		for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		int resId = getResources().getIdentifier(String.format("textView%s%s", i, j), "id", getPackageName());
        		TextView textView = (TextView) findViewById(resId);
        		textView.setText(grid[i][j].value+"");
        		grid[i][j].flag = false;
        	}
        }
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
