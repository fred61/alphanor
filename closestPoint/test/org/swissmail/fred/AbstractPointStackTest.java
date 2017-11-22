package org.swissmail.fred;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

abstract public class AbstractPointStackTest {

	Point[] points= new Point[] {
			new Point(1000,1000),
			new Point(2000,2000),
			new Point(3000,3000),
			new Point(4000,4000),
			new Point(5000,5000),
			new Point(-100,-100),
			new Point(-200,-200),
			new Point(-300,-300)
	};
	

	abstract IPointStack createStack(int depth, boolean ascending, Point target);
	
	@Test
	public void testClosest() {
		IPointStack subject= createStack(3, true, new Point(2100,4100));
		
		for( Point p : points) {
			subject.add(p);
		}
		
		assertArrayEquals(new Point[] {points[2], points[3], points[1]}, subject.get());
	}
	
	@Test
	public void testFurthest() {
		IPointStack subject= createStack(2, false, new Point(0,0));
		
		for( Point p : points) {
			subject.add(p);
		}
		
		assertArrayEquals(new Point[] {points[4], points[3]}, subject.get());
	}

}
