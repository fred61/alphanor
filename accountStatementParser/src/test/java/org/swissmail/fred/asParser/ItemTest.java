package org.swissmail.fred.asParser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ItemTest {

	String headerLineText= "        Datum  Text                               Valuta    Belastung  Gutschrift        Saldo";
	String itemLineText=   "        15.03.18 Vergütung                        16.03.18     31.80                   82'549.48";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Line headerLine= new Line(headerLineText);
		
		assertTrue("headerLine", headerLine.isHeaderLine());
		
		Line itemLine= new Line(itemLineText);
		
		Item item= new Item(itemLine,headerLine);
		
		assertEquals("itemDate", "15.03.18", item.date);
		assertEquals("valueDate", "16.03.18", item.valueDate);
		assertEquals("debit", "31.80", item.debit);
		assertEquals("credit", null, item.credit);
		assertEquals("balance", "82'549.48", item.balance);
	}

}
