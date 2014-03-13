package com.cjw.llama2;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        //added these to eventually be used to find the tile sizes/coordinates
        int margin_h = 20;
        int view_w = mainView.getWidth()-2*margin_h;
        int view_h = mainView.getHeight();
        int margin_v = (int) Math.round(((double) (view_h - view_w)) / 2);
        int tile_sidelength = (int) Math.round(((double) view_w) / 4);
        
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
		boolean validMove = false;
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
						
						pool.add(current);

						if (b-increment > 0 && b-increment < 3)
							b = b - increment * 2;
						
						validMove = true;
					} else if (!neighbour.flag && neighbour.value == current.value) {
						// crush
						neighbour.flag = true;
						neighbour.value++;
						current.value = 0;
						
						pool.add(current);
						
						validMove = true;
					}
				}
			}
		}
		if (validMove)
			seed(1);
		for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		int resId = getResources().getIdentifier(String.format("textView%s%s", i, j), "id", getPackageName());
        		TextView textView = (TextView) findViewById(resId);
        		textView.setText(grid[i][j].value+"");
        		grid[i][j].flag = false;
        	}
        }
		if (pool.isEmpty() && isNoMoreMoves(horizontal)) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	        alertDialog.setTitle("Game Over");
	        alertDialog.setMessage("Play again?");
	        alertDialog.show();
		}
	}
	
	private boolean isNoMoreMoves(boolean horizontal) {
		Tile current;
		Tile neighbour;
		for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 3; j++) {
        		if (horizontal) {	// check vertical first (by probability)
					current = grid[j][i];
					neighbour = grid[j+1][i];
				} else {
					current = grid[i][j];
					neighbour = grid[i][j+1];
				}
        		
        		if (current.value == neighbour.value)
        			return false;
        	}
		}
		for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 3; j++) {
        		if (horizontal) {	// check vertical first (by probability)
        			current = grid[i][j];
					neighbour = grid[i][j+1];
				} else {
					current = grid[j][i];
					neighbour = grid[j+1][i];
				}
        		
        		if (current.value == neighbour.value)
        			return false;
        	}
		}
		return true;
	}
}
