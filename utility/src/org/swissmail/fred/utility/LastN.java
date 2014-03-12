package org.swissmail.fred.utility;

public class LastN {
	private final long[] values;
	private int index;
	
	public LastN(int count) {
		values= new long[count];
		index= 0;
	}
	
	public void add(long value) {
		values[index]= value;
		index= (index + 1) % values.length;
	}
	
	public long[] get() {
		// expectation here is unclear if fewer than values.length values have been added.
		// what do we return? 
		
		long[] result= new long[values.length];
		int i= values.length; int j= index;
		
		while (i > 0) {
			i= i - 1;
			if (j == 0) {
				j+= values.length;
			}
			j= j - 1;
			result[i]= values[j];
		}
		
		return result;
	}
}
