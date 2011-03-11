package com.teamwut.plasma.plasmapong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.teamwut.plasma.plasmapong.mt.Cursor;
import com.teamwut.plasma.plasmapong.mt.MTCallback;
import com.teamwut.plasma.plasmapong.mt.MTManager;
import com.teamwut.plasma.plasmapong.pong.Const;

public abstract class PActivity extends Activity implements MTCallback, SurfaceHolder.Callback {



	PlasmaFluid fluid;
	
	MTManager mtManager;
	SurfaceHolder holder;
	
	public int width, height;
	final Random r = new Random();
		
	public void onCreate(final Bundle savedinstance) {
		super.onCreate(savedinstance);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        createSurface();
		
	}
	
	protected void createSurface() {
		final SurfaceView surface = new SurfaceView(this);
		holder = surface.getHolder();
		holder.addCallback(this);
		this.setContentView(surface);
	}
	boolean alreadyone = false;
	boolean keeprunning = true;
	
	protected class UpdateThread extends Thread {
		public void run() {
			if (!alreadyone) {
				alreadyone = true;
				Canvas c;
				while(keeprunning) {
					c = holder.lockCanvas();
					if (c != null) {
						drawAndUpdate(c);
						holder.unlockCanvasAndPost(c);
					}
					try {
						Thread.sleep(Const.ANIMATION_THREAD_SLEEP_MS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				alreadyone = false;
			}
		}
	}

	private void setupUI() {
	    mtManager = new MTManager();

		setup();
		
	    new UpdateThread().start();
	}
	public abstract void setup();
		
	//mt version
	public boolean onTouchEvent(final MotionEvent me) {
		if (mtManager != null) mtManager.surfaceTouchEvent(me);
		return true;
	}
	
	public void addForce(final float x, final float y) {
		float vx, vy;	
		if (width < height) {
			if (y/height > 0.5f) 
				vx = -25;
			else 
				vx = 25;
			vy = 0;
		} else {
			if (x/width > 0.5f) 
				vy = -25;
			else 
				vy = 25;
			vx = 0;

		}
		
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	    
	    vy = -4;
	    vx = vx / 10;
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	    
	    vy = 4;
	    fluid.addForce( x/width, y/height, vy/width, vx/height);
	}
	
	public void addRandomForce() {
		final float x = r.nextInt(width);
		final float y = r.nextInt(height);
		final int max = 200;
		final float dx = r.nextInt(max) - max/2;
		final float dy = r.nextInt(max) - max/2;
		fluid.addForce( x/width, y/height, dy/width, dx/height, 0, 50);
	}
	
	public void updateCursors() {
		final ArrayList<Cursor> cursors = (ArrayList<Cursor>) mtManager.cursors.clone();
		for (final Cursor c : cursors ) {
			if (c != null && c.currentPoint != null)
				addForce(c.currentPoint.x, c.currentPoint.y);
		}
	}
	
	
	private void drawAndUpdate(Canvas c) {
		updateCursors();	
		draw(c);
	}
	public abstract void draw(Canvas c);

	@Override
	public void touchEvent(final MotionEvent me, final int i, final float x, final float y, final float vx,
			final float vy, final float size) {
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		this.width = width;;
		this.height = height;;
		if (width > height) {
			Const.IS_PORTRAIT = false;
		} else {
			Const.IS_PORTRAIT = true;
		}
		setupUI();
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	
	
	/**
	 * Helper functions!
	 */
	public Bitmap loadImage(String filename) {
		// return loadImage(filename, null);
		InputStream stream = createInput(filename);
		if (stream == null) {
			System.err.println("Could not find the image " + filename + ".");
			return null;
		}
		// long t = System.currentTimeMillis();
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(stream);
		} finally {
			try {
				stream.close();
				stream = null;
			} catch (final IOException e) {
			}
		}
		return bitmap;
	}

	public InputStream createInput(final String filename) {
		final InputStream input = createInputRaw(filename);
		if ((input != null) && filename.toLowerCase().endsWith(".gz")) {
			try {
				return new GZIPInputStream(input);
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return input;
	}

	/**
	 * Call createInput() without automatic gzip decompression.
	 */
	public InputStream createInputRaw(final String filename) {
		// Additional considerations for Android version:
		// http://developer.android.com/guide/topics/resources/resources-i18n.html
		InputStream stream = null;

		if (filename == null)
			return null;

		if (filename.length() == 0) {
			// an error will be called by the parent function
			// System.err.println("The filename passed to openStream() was empty.");
			return null;
		}

		// safe to check for this as a url first. this will prevent online
		// access logs from being spammed with GET /sketchfolder/http://blahblah
		if (filename.indexOf(":") != -1) { // at least smells like URL
			try {
				final URL url = new URL(filename);
				stream = url.openStream();
				return stream;

			} catch (final MalformedURLException mfue) {
				// not a url, that's fine

			} catch (final FileNotFoundException fnfe) {
				// Java 1.5 likes to throw this when URL not available. (fix for
				// 0119)
				// http://dev.processing.org/bugs/show_bug.cgi?id=403

			} catch (final IOException e) {
				// changed for 0117, shouldn't be throwing exception
				e.printStackTrace();
				// System.err.println("Error downloading from URL " + filename);
				return null;
				// throw new RuntimeException("Error downloading from URL " +
				// filename);
			}
		}

		/*
		 * // Moved this earlier than the getResourceAsStream() checks, because
		 * // calling getResourceAsStream() on a directory lists its contents.
		 * // http://dev.processing.org/bugs/show_bug.cgi?id=716 try { // First
		 * see if it's in a data folder. This may fail by throwing // a
		 * SecurityException. If so, this whole block will be skipped. File file
		 * = new File(dataPath(filename)); if (!file.exists()) { // next see if
		 * it's just in the sketch folder file = new File(sketchPath, filename);
		 * } if (file.isDirectory()) { return null; } if (file.exists()) { try {
		 * // handle case sensitivity check String filePath =
		 * file.getCanonicalPath(); String filenameActual = new
		 * File(filePath).getName(); // make sure there isn't a subfolder
		 * prepended to the name String filenameShort = new
		 * File(filename).getName(); // if the actual filename is the same, but
		 * capitalized // differently, warn the user. //if
		 * (filenameActual.equalsIgnoreCase(filenameShort) &&
		 * //!filenameActual.equals(filenameShort)) { if
		 * (!filenameActual.equals(filenameShort)) { throw new
		 * RuntimeException("This file is named " + filenameActual + " not " +
		 * filename + ". Rename the file " + "or change your code."); } } catch
		 * (IOException e) { } }
		 * 
		 * // if this file is ok, may as well just load it stream = new
		 * FileInputStream(file); if (stream != null) return stream;
		 * 
		 * // have to break these out because a general Exception might // catch
		 * the RuntimeException being thrown above } catch (IOException ioe) { }
		 * catch (SecurityException se) { }
		 */

		// Using getClassLoader() prevents Java from converting dots
		// to slashes or requiring a slash at the beginning.
		// (a slash as a prefix means that it'll load from the root of
		// the jar, rather than trying to dig into the package location)

		/*
		 * // this works, but requires files to be stored in the src folder
		 * ClassLoader cl = getClass().getClassLoader(); stream =
		 * cl.getResourceAsStream(filename); if (stream != null) { String cn =
		 * stream.getClass().getName(); // this is an irritation of sun's java
		 * plug-in, which will return // a non-null stream for an object that
		 * doesn't exist. like all good // things, this is probably introduced
		 * in java 1.5. awesome! //
		 * http://dev.processing.org/bugs/show_bug.cgi?id=359 if
		 * (!cn.equals("sun.plugin.cache.EmptyInputStream")) { return stream; }
		 * }
		 */

		// Try the assets folder
		final AssetManager assets = getAssets();
		try {
			stream = assets.open(filename);
			if (stream != null) {
				return stream;
			}
		} catch (final IOException e) {
			// ignore this and move on
			// e.printStackTrace();
		}

		// Maybe this is an absolute path, didja ever think of that?
		final File absFile = new File(filename);
		if (absFile.exists()) {
			try {
				stream = new FileInputStream(absFile);
				if (stream != null) {
					return stream;
				}
			} catch (final FileNotFoundException fnfe) {
				// fnfe.printStackTrace();
			}
		}


		// Attempt to load the file more directly. Doesn't like paths.
		final Context context = getApplicationContext();
		try {
			// MODE_PRIVATE is default, should we use something else?
			stream = context.openFileInput(filename);
			if (stream != null) {
				return stream;
			}
		} catch (final FileNotFoundException e) {
			// ignore this and move on
			// e.printStackTrace();
		}

		return null;
	}
}
