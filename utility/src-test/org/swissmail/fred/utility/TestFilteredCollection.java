package org.swissmail.fred.utility;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestFilteredCollection {
	static private int LIST_SIZE= 10;
	
	private List<Integer> testData;
	
	private FilteredCollection.CollectionFilter<Integer> evenFilter= new FilteredCollection.CollectionFilter<Integer>() {

		@Override
		public boolean pass(Integer element) {
			return element % 2 == 0;
		}
		
	};

	private FilteredCollection.CollectionFilter<Integer> oddFilter= new FilteredCollection.CollectionFilter<Integer>() {

		@Override
		public boolean pass(Integer element) {
			return element % 2 != 0;
		}
		
	};
	
	@Before
	public void setup()
	{
		testData= new ArrayList<Integer>(LIST_SIZE);
		
		for (int i= 0; i < LIST_SIZE; i++) 
		{
			testData.add(i);
		}
	}
	
	@Test
	public void testReadNoFilter() {
		FilteredCollection<Integer> testSubject= new FilteredCollection<Integer>(testData);
		
		int expected= 0;
		for (Integer actual : testSubject) 
		{
			assertEquals(expected, actual.intValue());
			expected+= 1;
		}
	}
	
	@Test
	public void testReadEvens() {
		FilteredCollection<Integer> testSubject= new FilteredCollection<Integer>(testData);
		
		testSubject.setFilter(evenFilter);
		
		int expected= 0;
		for (Integer actual : testSubject) 
		{
			assertEquals(expected, actual.intValue());
			expected+= 2;
		}
	}
	
	@Test
	public void testFilterChange() {
		FilteredCollection<Integer> testSubject= new FilteredCollection<Integer>(testData);
		testSubject.setFilter(evenFilter);
		
		int expected= 0;
		for (Integer actual : testSubject) 
		{
			assertEquals(expected, actual.intValue());
			expected+= 2;
		}
		
		testSubject.setFilter(oddFilter);
		
		expected= 1;
		for (Integer actual : testSubject) 
		{
			assertEquals(expected, actual.intValue());
			expected+= 2;
		}
		
	}

}
