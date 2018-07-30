package org.swissmail.fred.asParser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
	
	class TestItemSink implements IItemSink {
		List<Item> items= new ArrayList<>();
		
		@Override
		public void open() throws Exception {
		}

		@Override
		public void writeItem(Item i) {
			if (i.subItems == null) {
				items.add(i);
			} else {
				for(Item j: i.subItems) {
					items.add(j);
				}
			}
		}

		@Override
		public void close() {
		}
	}
	
	class TestLineSource implements ILineSource {
		String [] lines;
		int lineInd;
		
		TestLineSource(String[] lines) {
			this.lines= lines;
		}
		
		TestLineSource(String lines) {
			this.lines= lines.split("\n");
		}

		@Override
		public void open() throws Exception {
			lineInd= 0;
		}

		@Override
		public String nextLine() {
			if (lineInd >= lines.length) {
				return null;
			} else {
				return lines[lineInd++];
			}
		}

		@Override
		public void close() {
		}
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void singlePageTest() throws Exception {
		Item[] items= getItems(singlePageLines);
		
		assertEquals("itemCount", 1, items.length);
		
		testItem(items[0], "15.03.18", "16.03.18", "31.80", null, "82'549.48");
	}
	
	@Test
	public void multiPageTest() throws Exception {
		Item[] items= getItems(multiPageLines);
		
		assertEquals("itemCount", 3, items.length);
		
		testItem(items[0], "01.03.18", "01.03.18", "1'210.00", null, "73'920.28");
		testItem(items[1], "14.03.18", "14.03.18", null, "620.00", "81'361.28");
		testItem(items[2], "15.03.18", "15.03.18", "31.80", null, "82'549.48");
	}
	
	@Test
	// test that "Postgiro" is recognised as a compound item
	public void compoundItemTest1() throws Exception
	{
		Item[] items= getItems(compoundItemLines1);
		
		assertEquals("itemCount", 2, items.length);
		
		testItem(items[0], "09.04.18", "09.04.18", null, "276.00", "79'832.94");
		testItem(items[1], "09.04.18", "09.04.18", null, "276.00", "79'832.94");
	}
	
	@Test
	// test that "Vergütung" is recognised as a compound item and that "Saldo per" is not a part of such a compound item
	public void compoundItemTest2() throws Exception
	{
		Item[] items= getItems(compoundItemLines2);
		
		assertEquals("itemCount", 3, items.length);
		
		
		testItem(items[0], "31.05.18", "31.05.18", "1'166.18", null, "88'445.71");
		testItem(items[1], "31.05.18", "31.05.18", "610.51",   null, "88'445.71");
		testItem(items[2], "31.05.18", "31.05.18", "418.63",   null, "88'445.71");
	}
	
	private Item[] getItems(String lines) throws Exception
	{
		TestItemSink itemSink= new TestItemSink();
		TestLineSource lineSource= new TestLineSource(lines);
		
		new Parser(lineSource, itemSink).parse();
		
		return itemSink.items.toArray(new Item[itemSink.items.size()]);
	}
	
	private void testItem(Item item, String date, String valueDate, String debit, String credit, String balance)
	{
		assertEquals("itemDate", date, item.date);
		assertEquals("valueDate", valueDate, item.valueDate);
		assertEquals("debit", debit, item.debit);
		assertEquals("credit", credit, item.credit);
		assertEquals("balance", balance, item.balance);
	}
	
	String singlePageLines= 
		"        Datum  Text                               Valuta    Belastung  Gutschrift        Saldo\n" + 
		"        15.03.18 Vergütung                        16.03.18     31.80                   82'549.48\n" + 
		"               Elektrizitätswerk der Stadt Zürich\n" + 
		"               Tramstrasse 35\n" + 
		"               8050Zürich\n" + 
		"               000001015837270200011575061\n" +
		"                                                                            948'369'911 Seite 4/5\n";
	
	String multiPageLines=
		"                                             Datum                                       Text                                                                                                                                                                                                               Valuta                                                     Belastung                                                           Gutschrift                                                                                                     Saldo\n" + 
		"                                              e                                           Saldovortrag                                                                                                                                                                                                                                                                                                                                                                                                                                     75'130.28\n" + 
		"                                              01.03.18 Dauerauftrag                                                                                                                                                                                                                                          01.03.18                                                             1'210.00                                                                                                                                                 73'920.28\n" + 
		"                                                                                          Personalvorsorgestiftung graphische\n" + 
		"                                                                                          Industrie (PVGI) 3005 Bern\n" + 
		"                                                                                          Schwamendingenstr 75-79\n" + 
		"                                                                                          8050Zürich\n" + 
		"                                                                                          947569000000001101077011315\n" +
		"                                                                                                                                                                                                                                                                                                                                                                                                                                                                        948'369'911                                                                Seite 1/5\n" +
		"        Datum Text                             Valuta    Belastung Gutschrift        Saldo\n" + 
		"        14.03.18 Vergütung                     14.03.18               620.00      81'361.28\n" + 
		"              SRIVALLI GODUGUNURI\n" + 
		"              8052ZURICH\n" + 
		"              TAVISHHAPPYNESTFEES\n" + 
		"                                                                        948'369'911Seite 3/5\n" + // note no whitespace before "Seite" - intentional and important part of test
		"        Datum  Text                               Valuta    Belastung  Gutschrift        Saldo\n" + 
		"        15.03.18 Vergütung                        15.03.18     31.80                   82'549.48\n" + 
		"               Elektrizitätswerk der Stadt Zürich\n" + 
		"               Tramstrasse 35\n" + 
		"               8050Zürich\n" + 
		"               000001015837270200011575061\n" +
		"                                                                            948'369'911 Seite 4/5\n";
	
	String compoundItemLines1 = 
			"        Datum  Text                               Valuta    Belastung   Gutschrift        Saldo\n" + 
			"        09.04.18 Postgiro                         09.04.18                552.00       79'832.94\n" + 
			"               Favre M. et Carlen M. B.     276.00\n" + 
			"               Herbstweg 35\n" + 
			"               8050Zurich\n" + 
			"               DonovanCarlenApril 2018\n" + 
			"               Gudel, Mario                 276.00\n" + 
			"               Neudorfstrasse 4\n" + 
			"               8050Zurich\n" +
			"                                                                            948'369'911 Seite 4/5\n";
	
	String compoundItemLines2 =
			"        Datum  Text                               Valuta    Belastung   Gutschrift        Saldo\n" +
	        "        31.05.18 Vergütung                        31.05.18    2'195.32                 88'445.71\n" + 
	        "               Heloise Clifford           1'166.18\n" + 
	        "               Bachstrasse 15\n" + 
	        "               5303Würenlingen\n" + 
	        "               Salary Happy Nest May 2018\n" + 
	        "               Kimberley Middleton          610.51\n" + 
	        "               Grüzenstr. 15\n" + 
	        "               8600Dübendorf\n" + 
	        "               Salary Happy Nest May 2018\n" + 
	        "               Sandra GomesdeOliveira da Silva418.63\n" + 
	        "               8952Schlieren\n" + 
	        "               Schönenwerdstr 1\n" + 
	        "               Salary Happy Nest April 2017\n" + 
	        "               Saldo per 31.05.2018                           4'007.16   7'285.00      88'445.71\n" + 
	        "                                                                            955'831'073  Seite 4/5\n";

			
}
