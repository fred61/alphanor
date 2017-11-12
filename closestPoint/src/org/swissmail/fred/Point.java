package org.swissmail.fred;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Point {
	private static NumberFormat distanceFormat= new DecimalFormat("#.000");
	
	private final short x;
	private final short y;
	private long squareDistance= -1;
	
	public Point(short x, short y) {
		this.x= x;
		this.y= y;
	}
	
	public Point(Point other) {
		this(other.x, other.y);
	}
	
	Point(int x, int y)
	{
		this((short)x, (short)y);
	}
	
	public void setTarget(Point target)
	{
		this.squareDistance= squareDistanceFrom(target);
	}
	
	long getSquareDistance()
	{
		return squareDistance;
	}
	
	private long squareDistanceFrom(Point other)
	{
		long xDistance, yDistance;
		
		xDistance= this.x - other.x;
		yDistance= this.y - other.y;
		
		return xDistance*xDistance + yDistance*yDistance;
	}

	@Override
	public String toString() {
		StringBuilder b= new StringBuilder();
		
		b.append('(');
		b.append(this.x);
		b.append(',');
		b.append(this.y);
		
		if (squareDistance >= 0) {
			b.append(';');
			b.append(distanceFormat.format(Math.sqrt(squareDistance)));
		}
		
		b.append(')');
		
		
		return b.toString();
	}
	
	
}
