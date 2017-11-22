package org.swissmail.fred;

public class Point {
	private final short x;
	private final short y;
	
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
	
	public long squareDistanceFrom(Point other)
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
		
		b.append(')');
		
		return b.toString();
	}
	
	
}
