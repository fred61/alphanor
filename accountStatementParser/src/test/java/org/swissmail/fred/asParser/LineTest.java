package org.swissmail.fred.asParser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LineTest {

	String headerLine= "        Datum  Text                             Valuta   Belastung  Gutschrift        Saldo";
	
	
	String itemHeaderLine= "        20.02.18 Vergütung                        20.02.18                368.00       79'622.24";
	
	String compoundItemLine1= "               Jeremy Justin Flachsmann     650.00";
	String compoundItemLine2= "               Sandra GomesdeOliveira da Silva418.63";
	
	String pageEndLine= "                                                                        948'369'911Seite 3/5";
			
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHeaderLine() {
		Line l= new Line(headerLine);
		
		assertEquals("tokenCount", 6, l.tokens.length);
		assertTrue("headerLine", l.isHeaderLine());
		
		testToken("t0", 8, "Datum", l.tokens[0]);
		testToken("t1", 15, "Text", l.tokens[1]);
		testToken("t0", 48, "Valuta", l.tokens[2]);
		testToken("t0", 57, "Belastung", l.tokens[3]);
		testToken("t0", 68, "Gutschrift", l.tokens[4]);
		testToken("t0", 86, "Saldo", l.tokens[5]);
		
	}
	
	
	@Test
	public void testItemHeaderLine() {
		Line l= new Line(itemHeaderLine);
		
		assertEquals("tokenCount", 5, l.tokens.length);
		assertTrue("itemHeaderLine", l.isItemHeader());
	}
	
	@Test
	public void testCompoundItemLine1() {
		Line l= new Line(compoundItemLine1);
		
		assertEquals("tokenCount", 4, l.tokens.length);
		assertTrue("isCompoundItemStart", l.isCompoundItemStart());
		assertTrue("line equals all", l.equalsAll(new String[] {"Jeremy", "Justin", "Flachsmann", "650.00"}));
	}
	
	@Test
	public void testCompoundItemLine2() {
		Line l= new Line(compoundItemLine2);
		
		assertEquals("tokenCount", 5, l.tokens.length);
		assertTrue("isCompoundItemStart", l.isCompoundItemStart());
		
		assertTrue("line equals all", l.equalsAll(new String[] {"Sandra", "GomesdeOliveira", "da", "Silva", "418.63"}));
	}
	
	@Test
	public void testEndOfPageLine() {
		Line l= new Line(pageEndLine);
		
		assertTrue("isPageEnd", l.isEndOfPage());
	}
	
	void testToken(String messagePrefix, int startAt, String content, Token token)
	{
		assertEquals(messagePrefix + " start", startAt, token.startAt);
		assertEquals(messagePrefix + " content", content, token.content);
	}

}
