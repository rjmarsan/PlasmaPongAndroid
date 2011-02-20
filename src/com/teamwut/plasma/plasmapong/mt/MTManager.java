package com.teamwut.plasma.plasmapong.mt;

import java.util.ArrayList;

import android.util.Log;
import android.view.MotionEvent;



public class MTManager {
	
	public ArrayList<Point> points;
	public ArrayList<Cursor> cursors;
	
	private ArrayList<TouchListener> listeners;
	
	
	public MTManager() {
		this.points = new ArrayList<Point>(8);
		this.cursors = new ArrayList<Cursor>(8);
		this.listeners = new ArrayList<TouchListener>(8);
	}

	public void addTouchListener(TouchListener t) {
		if (t == null) {
			System.out.println("Hey jerk! Quit trying to add a null touch listener to MTManager");
		}
		listeners.add(t);
	}
	
	public void surfaceTouchEvent(MotionEvent me) {
		synchronized (cursors) {
			int numPointers = me.getPointerCount();
			if (numPointers == 0) {
				//callback.touchEvent(me, 0, 0,0,0,0,0);
			}
			for (int i = 0; i < numPointers; i++) {
				touchEvent(me, i);
			}
			if (me.getPointerCount() == 1 && me.getAction() == MotionEvent.ACTION_UP) {//if the final finger is lifted...
				cursors.clear();
			}
			if (me.getPointerCount() < cursors.size()) {
				for (int i = 0; i < cursors.size(); i++) {
					int pointerId = me.getPointerId(i);
					int index = me.findPointerIndex(pointerId);
					if (index < 0) {
						cursors.remove(i);
					}
					else if (pointerId != index && i >= 1) {
						cursors.remove(i-1);
					}
				}
			}
		}
	}
	
	
	public void touchEvent(MotionEvent me, int i) {		
		int pointerId = me.getPointerId(i);
		float x = me.getX(i);
		float y = me.getY(i);
		
		float vx = 0;
		float vy = 0;
		
		int index = me.findPointerIndex(pointerId);
		
		maybeAddCapacity(cursors,index);
		
		Cursor c = cursors.get(index);
		long ctime = System.currentTimeMillis();
		if (c != null && c.curId == pointerId && ctime - c.currentPoint.time < 100 ) {
			c.updateCursor(new Point(x,y));

		}
		else {
			c = new Cursor(new Point(x,y), pointerId);
		}
		cursors.set(index, c);
		if (me.getAction() == MotionEvent.ACTION_UP) {
			if (me.getPointerId(me.getActionIndex()) == pointerId)
				cursors.remove(index);
		}
		
		vx = c.velX;
		vy = c.velY;
		

		float size = me.getSize(i);

		fireTouchEvent(me, c);
	}

	public void maybeAddCapacity(ArrayList<?> something, int index) {
		if (something.size() < index+1) {
			//points.ensureCapacity(index+4);
			something.add(null);
			something.add(null);
			something.add(null);
			something.add(null);
		}
	}
	
	
	public void fireTouchEvent(MotionEvent me, Cursor c) {
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			fireTouchDown(me, c);
		}
		else if (me.getAction() == me.ACTION_MOVE) {
			fireTouchMoved(me, c);
		}
		else if (me.getAction() == me.ACTION_UP) {
			fireTouchUp(me, c);
		}
		
		if (me.getPointerCount() == 1 && me.getAction() == me.ACTION_UP) {//if the final finger is lifted...
			fireTouchAllUp(me, c);
		}	
	}
	
	

	public void fireTouchDown(MotionEvent e, Cursor c) {
		for (TouchListener l : listeners) l.touchDown(c);
	}

	public void fireTouchMoved(MotionEvent e, Cursor c) {
		for (TouchListener l : listeners) l.touchMoved(c);
	}

	public void fireTouchUp(MotionEvent e, Cursor c) {
		for (TouchListener l : listeners) l.touchUp(c);
	}

	public void fireTouchAllUp(MotionEvent e, Cursor c) {
		for (TouchListener l : listeners) l.touchAllUp(c);
	}	
	
	
	
	
	
}
