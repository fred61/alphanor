package org.swissmail.fred.firstunique;

import java.util.HashMap;
import java.util.Map;

public class CanonicalFinder implements IFinder {

	public Character findFirstUnique(String s) {
		Map<Character, Integer> characterCounts= new HashMap<>();
		
		int i;
		
		for(i= 0; i < s.length(); i++) {
			if (characterCounts.get(s.charAt(i)) == null) {
				characterCounts.put(s.charAt(i), 0);
			}
			characterCounts.put(s.charAt(i), characterCounts.get(s.charAt(i)) + 1);
		}
		
		for(i= 0; i < s.length() && characterCounts.get(s.charAt(i)) > 1; i++); 
		
		if (i >= s.length()) {
			return null;
		} else {
			return s.charAt(i);
		}
	}

}
