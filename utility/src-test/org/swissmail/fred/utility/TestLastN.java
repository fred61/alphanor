package org.swissmail.fred.utility;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class TestLastN {
	private static final int leaderCount= 4;
	
	private long[] values;
	
	@Before
	public void setup() 
	{
		values= new long[] {44,12,56,61,96};
	}

	@Test
	public void testAddOnce() {
		LastN testSubject= new LastN(values.length);
		
		for (long value : values) {
			testSubject.add(value);
		}
		
		assertArrayEquals(values, testSubject.get());
	}
	
	@Test
	public void testAddTwice() {
		LastN testSubject= new LastN(values.length);
		
		for (long value : values) {
			testSubject.add(value);
		}
		for (long value : values) {
			testSubject.add(value);
		}
		
		assertArrayEquals(values, testSubject.get());
	}
	
	@Test
	public void testAddWithLeader() {
		LastN testSubject= new LastN(values.length);
		
		for(int i= 0; i < leaderCount; i++) {
			testSubject.add(0);
		}
		
		for (long value : values) {
			testSubject.add(value);
		}
		
		assertArrayEquals(values, testSubject.get());
	}

}
