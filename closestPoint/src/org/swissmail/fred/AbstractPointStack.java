package org.swissmail.fred;

abstract public class AbstractPointStack implements IPointStack {

	protected final int depth;
	protected final boolean ascending;
	protected final Point target;
	
	protected AbstractPointStack(int depth, boolean ascending, Point target)
	{
		this.depth= depth;
		this.ascending= ascending;
		this.target= target;
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
	
}
