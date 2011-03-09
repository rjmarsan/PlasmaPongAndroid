package com.teamwut.plasma.plasmapong.mt;

public class Point {
	public final float x; 
	public final float y; 
	public long time;

	public Point(final float x, final float y) {
		this.x = x;
		this.y = y;
		time = System.currentTimeMillis();
	}
}