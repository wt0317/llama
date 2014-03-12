package com.cjw.llama2;

public class Tile {
	public int value = 0;
	public boolean flag = false;
	public int x = 0;
	public int y = 0;
	
	public Tile(int value, boolean flag, int x, int y) {
		this.value = value;
		this.flag = flag;
		this.x = x;
		this.y = y;
	}
	
	public boolean isLocation(int x, int y) {
		return this.x == x && this.y == y;
	}
}