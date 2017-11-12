package org.swissmail.fred.firstunique;

public class BruteForceFinder implements IFinder {

	@Override
	public Character findFirstUnique(String s) {
		char[] characters= s.toCharArray();
		int i= 0;
		
		Character res= null;
		while (res == null && i < characters.length) {
			char c= characters[i];
			int j= 0;
			while (j < characters.length && (j == i || characters[j] != c)) {
				j+= 1;
			}
			
			if (j >= characters.length) {
				res= c;
			} else {
				i+= 1;
			}
		}
		
		return res;
	}

}
