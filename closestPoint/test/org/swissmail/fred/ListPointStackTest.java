package org.swissmail.fred;

public class ListPointStackTest extends AbstractPointStackTest {

	@Override
	IPointStack createStack(int depth, boolean ascending, Point target) {
		return new ListPointStack(depth, ascending, target);
	}
}
