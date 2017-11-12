package org.swissmail.fred;

public interface IPointStack {

	int getDepth();
	
	boolean isAscending();
	
	Point getTarget();
	
	void add(Point p);

	Point[] get();

}