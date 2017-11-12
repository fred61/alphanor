package org.swissmail.fred;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueuePointStack extends AbstractPointStack {
	
	private final Queue<Point> queue;
	private final Comparator<Point> comparator;
			
    public QueuePointStack(int depth, boolean ascending, Point target)
    {
		super(depth, ascending, target);
    			
		if (ascending) {
			comparator= new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Long.compare(o2.getSquareDistance(), o1.getSquareDistance());
				}
			};
		} else {
			comparator= new Comparator<Point>() {

				@Override
				public int compare(Point o1, Point o2) {
					return Long.compare(o1.getSquareDistance(), o2.getSquareDistance());
				}
			};
		}
		
		queue= new PriorityQueue<>(depth, comparator);
    }

	@Override
	public void add(Point p) {
		p.setTarget(target);
		
		if (queue.size() < this.depth) {
			queue.add(p);
		} else {
			Point head= queue.peek();		//head != null
			if (comparator.compare(p, head) >= 0) {
				queue.poll();
				queue.add(p);
			}
		}
	}

	@Override
	public Point[] get() {
		Point[] result= new Point[depth];
		
		for(int i= result.length - 1; i >= 0; i--) {
			result[i]= queue.poll();
		}
		
		return result;
	}

}
