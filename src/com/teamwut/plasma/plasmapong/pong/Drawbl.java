package com.teamwut.plasma.plasmapong.pong;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.teamwut.plasma.plasmapong.PActivity;
import com.teamwut.plasma.plasmapong.PlasmaFluid;

public class Drawbl {

	public static SoftReference<PlasmaFluid> FLUID = new SoftReference<PlasmaFluid>(null);
	public static PlasmaFluid getFluidSim(int width, int height) {
//		PlasmaFluid fluid = FLUID.get();
//		if (fluid != null) {
//			return fluid;
//		}
//		fluid = new PlasmaFluid(p);
//		FLUID = new SoftReference<PlasmaFluid>(fluid);
//		return fluid;
		return new PlasmaFluid(width, height);
	}
	
	
	public static SoftReference<Bitmap> GOAL_TOP = new SoftReference<Bitmap>(null);
	public static Bitmap getGoalTop(final PActivity p) {
		return fillSoftReference(p, GOAL_BOTTOM, "TopGoalLine_5pxSq.png", false, true);
	}
	
	public static SoftReference<Bitmap> GOAL_BOTTOM = new SoftReference<Bitmap>(null);
	public static Bitmap getGoalBottom(final PActivity p) {
		return fillSoftReference(p, GOAL_BOTTOM, "BottomGoalLine_5pxSq.png", false, true);

	}
	
	public static SoftReference<Bitmap> GOAL_MIDDLE = new SoftReference<Bitmap>(null);
	public static Bitmap getGoalMiddle(final PActivity p) {
		return fillSoftReference(p, GOAL_MIDDLE, "MiddleLine_Small.png", false, true);
	}
	
	
	
	
	public static Bitmap fillSoftReference(final PActivity p, SoftReference<Bitmap> softref, final String name, final boolean scaleVert, final boolean scaleHoriz) {
		Bitmap Bitmaple = softref.get();
		if (Bitmaple != null) return Bitmaple;
		if (Const.IS_PORTRAIT)
			Bitmaple = p.loadImage(name);
		else
			Bitmaple = p.loadImage("ROT90_"+name);
		if (scaleHoriz) {
			if (Bitmaple != null) {
				if (Const.IS_PORTRAIT) {
					Bitmaple = Bitmap.createScaledBitmap(Bitmaple, p.width, Bitmaple.getHeight(),false);
				} else {
					Bitmaple = Bitmap.createScaledBitmap(Bitmaple, Bitmaple.getWidth(), p.height, false);
					
				}
			}
		}		
		softref = new SoftReference<Bitmap>(Bitmaple);
		return Bitmaple;
	}
	
	
	

}
