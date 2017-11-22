package org.swissmail.fred;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueuePointStack extends AbstractPointStack {
	
	private final Queue<RelativePoint> queue;
			
    public QueuePointStack(int depth, boolean ascending, Point target)
    {
		super(depth, ascending, target);
		queue= new PriorityQueue<>(depth, comparator);
    }

	@Override
	protected void addRelativePoint(RelativePoint p) {
		
		if (queue.size() < this.depth) {
			queue.add(p);
		} else {
			RelativePoint head= queue.peek();		//head != null
			if (comparator.compare(p, head) >= 0) {
				queue.poll();
				queue.add(p);
			}
		}
	}

	@Override
	protected RelativePoint[] getRelativePoints() {
		RelativePoint[] result= new RelativePoint[depth];
		
		for(int i= result.length - 1; i >= 0; i--) {
			result[i]= queue.poll();
		}
		
		return result;
	}
	
	

}
