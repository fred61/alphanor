package org.swissmail.fred.wordleSolver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class BitmapIndexTest {

	@Test
	void testMany() {
		BitmapIndex testIndex= new BitmapIndex(new long[] {23, 5});
		Set<Integer> setIndices= Set.of(0,1,2,4,64,66);
		
		for(int i= 0; i < 127; i++) {
			if (setIndices.contains(i)) {
				assertTrue(testIndex.isSet(i), "" + i);
			} else {
				assertFalse(testIndex.isSet(i), "" + i);
			}
		}
	}

	@Test
	void testSet() {
		BitmapIndex testIndex= new BitmapIndex(new long[] {23, 5});
		Set<Integer> setIndices= Set.of(0,1,2,4,64,66, 75);
		
		testIndex.set(75);
		
		for(int i= 0; i < 127; i++) {
			if (setIndices.contains(i)) {
				assertTrue(testIndex.isSet(i), "" + i);
			} else {
				assertFalse(testIndex.isSet(i), "" + i);
			}
		}
	}

	@Test
	void testUnset() {
		BitmapIndex testIndex= new BitmapIndex(new long[] {23, 13});
		Set<Integer> setIndices= Set.of(0,1,2,4,64,67);
		
		testIndex.unset(66);
		
		for(int i= 0; i < 127; i++) {
			if (setIndices.contains(i)) {
				assertTrue(testIndex.isSet(i), "" + i);
			} else {
				assertFalse(testIndex.isSet(i), "" + i);
			}
		}
	}
	
	@Test
	void testAllSet() {
		BitmapIndex testIndex= BitmapIndex.allSet(312);
		
		for (int i= 0; i < 312; i++) {
			assertTrue(testIndex.isSet(i), "" + i);
		}
	}
	
	@Test
	void testAllUnset() {
		BitmapIndex testIndex= new BitmapIndex(312);
		
		for (int i= 0; i < 312; i++) {
			assertFalse(testIndex.isSet(i), "" + i);
		}
	}	
	
	@Test 
	void testCountSet() {
		BitmapIndex testIndex= new BitmapIndex(new long[] {0x8000_0000_0000_0000l});
		assertEquals(1, testIndex.countSet());
		
		testIndex= new BitmapIndex(new long[] {0xFFFF_FFFF_FFFF_FFFFl});
		assertEquals(64, testIndex.countSet());
	}
	
	@Test
	void testCopy() {
		BitmapIndex source= new BitmapIndex(new long[] {23,13});
		BitmapIndex copy= new BitmapIndex(source);
		
		assertEquals(source, copy);
		
		source.unset(66);
		
		assertNotEquals(source, copy);
	}
	
	@Test
	void testOutOfBoundsAccess() {
		final BitmapIndex testIndex= new BitmapIndex(133);
		
		assertDoesNotThrow(() -> {testIndex.isSet(132);}, "132");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.isSet(133);}, "133");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.isSet(-1);}, "-1");
		
		assertDoesNotThrow(() -> {testIndex.set(132);}, "132");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.set(133);}, "133");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.set(-1);}, "-1");
		
		assertDoesNotThrow(() -> {testIndex.unset(132);}, "132");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.unset(133);}, "133");
		assertThrows(IndexOutOfBoundsException.class, () -> {testIndex.unset(-1);}, "-1");
	}
	
	@Test
	void testIntersectWith() {
		BitmapIndex a= new BitmapIndex(new long[] {59});
		BitmapIndex b= new BitmapIndex(new long[] {21});
		
		assertEquals(a.intersectWith(b), new BitmapIndex(new long[] {17}));
	}
	
	@Test
	void testUniteWith() {
		BitmapIndex a= new BitmapIndex(new long[] {59});
		BitmapIndex b= new BitmapIndex(new long[] {21});
		
		assertEquals(a.uniteWith(b), new BitmapIndex(new long[] {63}));
	}
	
	void testInvert() {
		BitmapIndex a= new BitmapIndex(new long[] {59});
		
		assertEquals(a, new BitmapIndex(new long[] {-60}));
	}
}
