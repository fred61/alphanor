package org.swissmail.fred.kata.kata2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

abstract public class AbstractBinarySearcherTest {

	static private class SearcherTestData {
		int needle;
		int [] haystack;
		int expected;
		
		SearcherTestData(int needle, int[] haystack, int expected)
		{
			this.needle= needle;
			this.haystack = haystack;
			this.expected = expected;
		}
	}
	
	static private SearcherTestData[] testData;
	
	private BinarySearcher subject;
	
	abstract protected BinarySearcher createSearcher();
	
	@BeforeClass
	static public void setupClass()
	{
		testData= new SearcherTestData[] {
				new SearcherTestData(4, new int[] {3,5}, -1),
				new SearcherTestData(4, new int[] {3,4,5,6,7}, 1),
				new SearcherTestData(3, new int[] {}, -1),
				new SearcherTestData(3, new int[] {1}, -1),
				new SearcherTestData(1, new int[] {1}, 0),
				new SearcherTestData(1, new int[] {1,3,5}, 0),
				new SearcherTestData(3, new int[] {1,3,5}, 1),
				new SearcherTestData(5, new int[] {1,3,5}, 2),
				new SearcherTestData(-1, new int[] {1,3,5}, -1),
				new SearcherTestData(0, new int[] {1,3,5}, -1),
				new SearcherTestData(2, new int[] {1,3,5}, -1),
				new SearcherTestData(4, new int[] {1,3,5}, -1),
				new SearcherTestData(6, new int[] {1,3,5}, -1),
				new SearcherTestData(0, new int[] {-2,0,2,4,6}, 1),
				new SearcherTestData(2, new int[] {-2,0,2,4,6}, 2),
				new SearcherTestData(4, new int[] {-2,0,2,4,6}, 3),
				new SearcherTestData(6, new int[] {-2,0,2,4,6}, 4),
				new SearcherTestData(-2, new int[] {-2,0,2,4,6}, 0),
				new SearcherTestData(-3, new int[] {-2,0,2,4,6}, -1),
				new SearcherTestData(-1, new int[] {-2,0,2,4,6}, -1),
				new SearcherTestData(1, new int[] {-2,0,2,4,6}, -1),
				new SearcherTestData(3, new int[] {-2,0,2,4,6}, -1),
				new SearcherTestData(5, new int[] {-2,0,2,4,6}, -1),
				new SearcherTestData(7, new int[] {-2,0,2,4,6}, -1),
		};
	}
	
	@Before
	public void setup()
	{
		subject= createSearcher();
	}
	
	
	@Test
	public void testSearcher()
	{
		for (SearcherTestData testItem : testData)
		{
			assertEquals(testItem.expected, subject.search(testItem.needle, testItem.haystack));
		}
	}
}
