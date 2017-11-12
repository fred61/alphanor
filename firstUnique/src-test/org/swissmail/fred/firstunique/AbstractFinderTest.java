package org.swissmail.fred.firstunique;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractFinderTest {
	protected IFinder subject;
	
	@Test
	void testMiddle() {
		assertEquals(new Character('b'), subject.findFirstUnique("fobof"));
	}
	
	@Test
	void testFront() {
		assertEquals(new Character('f'), subject.findFirstUnique("fobo"));
	}
	
	@Test
	void testEnd() {
		assertEquals(new Character('f'), subject.findFirstUnique("obobf"));
	}
	
	@Test 
	void testNotFound() {
		assertEquals(null, subject.findFirstUnique("fobofb"));
	}

}
