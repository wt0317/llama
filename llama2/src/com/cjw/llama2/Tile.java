package com.cjw.llama2;

public class Tile {
	public int value = 0;
	public boolean flag = false;
	public int i = 0;
	public int j = 0;
	
	public Tile(int value, boolean flag, int i, int j) {
		this.value = value;
		this.flag = flag;
		this.i = i;
		this.j = j;
	}
}