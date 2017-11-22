package org.swissmail.fred;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;



abstract public class AbstractPointStack implements IPointStack {
	private static NumberFormat distanceFormat= new DecimalFormat("#.000");

	protected final int depth;
	protected final boolean ascending;
	protected final Point target;
	protected final Comparator<RelativePoint> comparator;
	
	static protected class RelativePoint {
		final Point p;
		final long squareDistanceFromTarget;
		
		RelativePoint(Point p, Point target) {
			this.p= p;
			
			this.squareDistanceFromTarget= p.squareDistanceFrom(target);
			
		}

		@Override
		public String toString() {
			StringBuilder b= new StringBuilder();
			
			b.append(p);
			b.append(';');
			b.append(distanceFormat.format(Math.sqrt(squareDistanceFromTarget)));
			
			return b.toString();
		}
		
		
	}
	
	protected AbstractPointStack(int depth, boolean ascending, Point target)
	{
		this.depth= depth;
		this.ascending= ascending;
		this.target= target;
		
		if (ascending) {
			comparator= new Comparator<RelativePoint>() {

				@Override
				public int compare(RelativePoint o1, RelativePoint o2) {
					return Long.compare(o2.squareDistanceFromTarget, o1.squareDistanceFromTarget);
				}
			};
		} else {
			comparator= new Comparator<RelativePoint>() {

				@Override
				public int compare(RelativePoint o1, RelativePoint o2) {
					return Long.compare(o1.squareDistanceFromTarget, o2.squareDistanceFromTarget);
				}
			};
		}
	}

	public int getDepth() {
		return depth;
	}

	public boolean isAscending() {
		return ascending;
	}

	public Point getTarget() {
		return target;
	}

	@Override
	public void add(Point p) {
		RelativePoint rp= new RelativePoint(p, target);
		addRelativePoint(rp);
	}

	@Override
	public Point[] get() {
		RelativePoint[] rps= getRelativePoints();
		
		Point[] result= new Point[rps.length];
		for(int i= 0; i < rps.length; i++) {
			result[i]= rps[i].p;
		}
		
		return result;
	}
	
	
	@Override
	public String getResult() {
		StringBuilder b= new StringBuilder();
		
		b.append('[');
		
		for(RelativePoint rp : getRelativePoints()) {
			b.append(rp);
		}
		
		b.setLength(b.length() - 1);
		b.append(']');
		
		return b.toString();
	}

	abstract protected void addRelativePoint(RelativePoint rp);
	abstract protected RelativePoint[] getRelativePoints();
	
}
