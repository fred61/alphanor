package org.swissmail.fred.wordleSolver;

import java.util.Arrays;

public class BitmapIndex {

	static BitmapIndex allSet(int size) {
		BitmapIndex result= new BitmapIndex(size);
		
		for(int i= 0; i < result.bitmap.length; i++) {
			result.bitmap[i]= ~0L;
		}
		
		return result;
	}
	
	private final int maxIndex;
	private long[] bitmap;
	
	public BitmapIndex(int size) {
		bitmap= new long[size / 64 + 1];
		this.maxIndex= size - 1;
	}
	
	public BitmapIndex(BitmapIndex other) {
		maxIndex= other.maxIndex;
		bitmap= new long[other.bitmap.length];
		System.arraycopy(other.bitmap, 0, bitmap, 0, bitmap.length);
	}
	
	BitmapIndex(long[] index) {
		this.bitmap= index;
		maxIndex= index.length * 64;
	}
	
	public void set(int index) {
		assertIndex(index);
		int i= arrayPos(index);
		bitmap[i]= bitmap[i] | bitMask(index);
	}
	
	public void unset(int index) {
		assertIndex(index);
		int i= arrayPos(index);
		bitmap[i]= bitmap[i] & ~bitMask(index);
	}
	
	public boolean isSet(int index) {		
		assertIndex(index);
		return (bitmap[arrayPos(index)] & bitMask(index)) != 0;
	}
	
	public BitmapIndex intersectWith(BitmapIndex other) {
		if (other.maxIndex != this.maxIndex) {
			throw new IllegalArgumentException("can't intersect with index of different size");
		} else {
			for(int i= 0; i < bitmap.length; i++) {
				bitmap[i]= bitmap[i] & other.bitmap[i];
			}
		}
		return this;
	}
	
	public BitmapIndex uniteWith(BitmapIndex other) {
		if (other.maxIndex != this.maxIndex) {
			throw new IllegalArgumentException("can't unite with index of different size");
		} else {
			for(int i= 0; i < bitmap.length; i++) {
				bitmap[i]= bitmap[i] | other.bitmap[i];
			}
		}
		return this;
	}
	
	public BitmapIndex invert() {
		for(int i= 0; i < bitmap.length; i++) {
			bitmap[i]= ~bitmap[i];
		}
		
		return this;
	}
	
	public int countSet() {
		int count= 0; 
		for(int i= 0; i < bitmap.length; i++) {
			long n= bitmap[i];
			while (n != 0) {		//must be != not >, n could be < 0
				count+= n & 0b1;	// n % 2 is not correct
				n= n >>> 1;
			}
		}
		return count;
	}
	
	private int arrayPos(int index) {
		return index / 64;
	}
	
	private long bitMask(int index) {
		return 1L << (index % 64);
	}
	
	private void assertIndex(int index) {
		if (index < 0 || index > maxIndex) {
			throw new IndexOutOfBoundsException(index);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bitmap);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitmapIndex other = (BitmapIndex) obj;
		return Arrays.equals(bitmap, other.bitmap);
	}

	@Override
	public String toString() {
		return Arrays.toString(bitmap);
	}
	
	
}
