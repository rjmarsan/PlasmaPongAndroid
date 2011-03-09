package com.teamwut.plasma.plasmapong.pong;

import java.lang.ref.SoftReference;

import processing.core.PApplet;
import processing.core.PImage;

import com.teamwut.plasma.plasmapong.PlasmaFluid;

public class Drawbl {

	public static SoftReference<PlasmaFluid> FLUID = new SoftReference<PlasmaFluid>(null);
	public static PlasmaFluid getFluidSim(final PApplet p) {
//		PlasmaFluid fluid = FLUID.get();
//		if (fluid != null) {
//			return fluid;
//		}
//		fluid = new PlasmaFluid(p);
//		FLUID = new SoftReference<PlasmaFluid>(fluid);
//		return fluid;
		return new PlasmaFluid(p.width, p.height);
	}
	
	
	public static SoftReference<PImage> GOAL_TOP = new SoftReference<PImage>(null);
	public static PImage getGoalTop(final PApplet p) {
		return fillSoftReference(p, GOAL_BOTTOM, "TopGoalLine_5pxSq.png", false, true);
	}
	
	public static SoftReference<PImage> GOAL_BOTTOM = new SoftReference<PImage>(null);
	public static PImage getGoalBottom(final PApplet p) {
		return fillSoftReference(p, GOAL_BOTTOM, "BottomGoalLine_5pxSq.png", false, true);

	}
	
	public static SoftReference<PImage> GOAL_MIDDLE = new SoftReference<PImage>(null);
	public static PImage getGoalMiddle(final PApplet p) {
		return fillSoftReference(p, GOAL_MIDDLE, "MiddleLine_Small.png", false, true);
	}
	
	
	
	
	public static PImage fillSoftReference(final PApplet p, SoftReference<PImage> softref, final String name, final boolean scaleVert, final boolean scaleHoriz) {
		PImage pimagele = softref.get();
		if (pimagele != null) return pimagele;
		pimagele = p.loadImage(name);
		if (scaleHoriz)
			pimagele.resize(p.width, pimagele.height);
		softref = new SoftReference<PImage>(pimagele);
		return pimagele;
	}
	
	
	

}
