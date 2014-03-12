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

public class MainActivity extends Activity {
	
	Tile[][] grid;
	ArrayList<Tile> pool;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final View mainView  = findViewById(R.id.RelativeLayout1);
        mainView.setOnTouchListener(new OnSwipeTouchListener(this.getApplicationContext()) {
            public void onSwipeTop() {
            	evaluate(false, 1);
            }
            public void onSwipeRight() {
            	evaluate(true, -1);
            }
            public void onSwipeLeft() {
            	evaluate(true, 1);
            }
            public void onSwipeBottom() {
            	evaluate(false, -1);
            }
        });
        
        grid = new Tile[4][4];
        pool = new ArrayList<Tile>();
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		grid[i][j] = new Tile(0,false,i,j);
        		pool.add(grid[i][j]);
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
	
	private void seed(int iterations) {
		int size = pool.size();
		int index = (int) (Math.random() * size % size);
		
		Tile seeded = pool.get(index);
		seeded.value = 1;
		
		pool.remove(index);
		
		if (iterations > 1) {
			seed(--iterations);
		}
	}
	
	private void evaluate(boolean horizontal, int increment) {
		Tile current;
		Tile neighbour;
		int temp = (increment == 1) ? 1 : 2;
		for (int a = 0; a < 4; a++) {
			for (int b = temp; b > -1 && b < 4; b+=increment){
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
							Tile pt = pool.get(k);
							if (pt.equals(neighbour)) {
								pool.remove(k);
								break;
							}
						}

						if (b-increment > 0 && b-increment < 3)
							b = b - increment * 2;
					} else if (!neighbour.flag && neighbour.value == current.value) {
						// crush
						neighbour.flag = true;
						neighbour.value++;
						current.value = 0;
					}
					pool.add(current);
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
}
