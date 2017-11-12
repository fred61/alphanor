package org.swissmail.fred.kata.kata2;

public class ClassicBinarySearcherTest extends AbstractBinarySearcherTest {

	@Override
	protected BinarySearcher createSearcher() {
		return new ClassicBinarySearcher();
	}

}
