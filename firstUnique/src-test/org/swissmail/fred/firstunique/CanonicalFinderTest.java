package org.swissmail.fred.firstunique;

import org.junit.jupiter.api.BeforeEach;

public class CanonicalFinderTest extends AbstractFinderTest {
	@BeforeEach
	void setUp() throws Exception {
		subject= new CanonicalFinder();
	}

}
