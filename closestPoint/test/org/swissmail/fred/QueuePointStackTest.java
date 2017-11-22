package org.swissmail.fred;

public class QueuePointStackTest extends AbstractPointStackTest {

	@Override
	IPointStack createStack(int depth, boolean ascending, Point target) {
		return new QueuePointStack(depth, ascending, target);
	}


}