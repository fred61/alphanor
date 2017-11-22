package org.swissmail.fred;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ListPointStack extends AbstractPointStack {

	private final LinkedList<RelativePoint> pointList= new LinkedList<>();
	
	public ListPointStack(int depth, boolean ascending, Point target)
	{
		super(depth, ascending, target);
	}
	

	public void addRelativePoint(RelativePoint p)
	{
		if (pointList.size() == depth && comparator.compare(p, pointList.getLast()) <= 0) {
			// list is full and p1 is further / closer than last - discard
			return;
		}
		
		ListIterator<RelativePoint> pointListIterator= pointList.listIterator();
		boolean insert= false;
		
		while (pointListIterator.hasNext() && !insert) {
			RelativePoint p2= pointListIterator.next();
			
			insert= comparator.compare(p, p2) > 0;
		}
		
		if (!insert) {
			if (pointList.size() < depth) {
				pointList.addLast(p);
			}
		} else {
			pointListIterator.previous();
			pointListIterator.add(p);
			
			if (pointList.size() > depth) {
				pointList.removeLast();
			}
		}
	}
	
	@Override
	public RelativePoint[] getRelativePoints()
	{
		RelativePoint[] result= new RelativePoint[pointList.size()];
		return pointList.toArray(result);
	}
	
}
