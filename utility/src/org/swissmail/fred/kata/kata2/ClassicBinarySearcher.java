package org.swissmail.fred.kata.kata2;

public class ClassicBinarySearcher implements BinarySearcher {

	@Override
	public int search(int needle, int[] haystack) {
		int lower= 0, upper= haystack.length - 1;
		
		if (haystack.length == 0) {
			return -1;
		}
		
		int middle= (upper + lower) / 2;

		while (lower < middle && middle < upper)
		{
			if (haystack[middle] == needle) {
				return middle;		// shortcut;
			} else if (haystack[middle] < needle) {
				lower = middle;
			} else {
				// haystack[middle] > needle
				upper= middle;
			}
			middle= (upper + lower) / 2;
		} 
		
		if (haystack[lower] == needle) {
			return lower;
		} else if (haystack[upper] == needle) {
			return upper;
		} else {
			return -1;
		}
	}
}
