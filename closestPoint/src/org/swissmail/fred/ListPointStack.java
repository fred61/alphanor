package org.swissmail.fred;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ListPointStack extends AbstractPointStack {

	private final Comparator<Point> pointComparator;
	
	private final LinkedList<Point> pointList= new LinkedList<>();
	
	public ListPointStack(int depth, boolean ascending, Point target)
	{
		super(depth, ascending, target);
		
		if (ascending) {
			this.pointComparator= new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Long.compare(o1.getSquareDistance(), o2.getSquareDistance());
				}
			};
		} else {
			this.pointComparator= new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Long.compare(o2.getSquareDistance(), o1.getSquareDistance());
				}
			};
		}
	}
	
	/* (non-Javadoc)
	 * @see org.swissmail.fred.IPointStack#add(org.swissmail.fred.Point)
	 */
	@Override
	public void add(Point p)
	{
		Point p1= new Point(p);
		
		p1.setTarget(this.target);
		
		if (pointList.size() == depth && pointComparator.compare(p1, pointList.getLast()) > 0) {
			// list is full and p1 is further / closer than last - discard
			return;
		}
		
		ListIterator<Point> pointListIterator= pointList.listIterator();
		boolean insert= false;
		
		while (pointListIterator.hasNext() && !insert) {
			Point p2= pointListIterator.next();
			
			insert= pointComparator.compare(p1, p2) <= 0;
		}
		
		if (!insert) {
			if (pointList.size() < depth) {
				pointList.addLast(p1);
			}
		} else {
			pointListIterator.previous();
			pointListIterator.add(p1);
			
			if (pointList.size() > depth) {
				pointList.removeLast();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.swissmail.fred.IPointStack#get()
	 */
	@Override
	public Point[] get()
	{
		Point[] result= new Point[pointList.size()];
		return pointList.toArray(result);
	}
	
}
