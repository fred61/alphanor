package org.swissmail.fred.asParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//TODO if the input file doesn't end with a "new page" line, parser misses last item

/**
 * The main class. Parses each line in a text file given as argument on the command line. Gathers a list of Item's. 
 * This list is then written to an Excel worksheet named "output.xls" in the same directory as the
 * input text file.
 *
 */

public class Parser {
	private static Logger logger= Logger.getLogger(Parser.class.getCanonicalName());
	
	
	
	public static void main(String[] args) throws Exception {
		logger.info("parsing " + args[0]);
		
		File inFile= new File(args[0]);
		
		if (!inFile.exists()) {
			System.err.println("input file does not exist: " + args[0]);
		} else {
			File parentDir= inFile.getParentFile();
			new Parser(new FileLineSource(inFile), new XLSheetItemSink(parentDir)).parse();
		}
	}
	
	private final ILineSource source;
	private final IItemSink sink;
	
	Parser(ILineSource source, IItemSink sink) {
		this.source= source;
		this.sink= sink;
	}
	
	List<Item> items= new ArrayList<>();		
	
	void parse() throws Exception
	{
		source.open(); sink.open();
		
		Page currentPage= new Page();
		
		try {
			for(String line= source.nextLine(); line != null; line= source.nextLine()) {
				Line aLine= new Line(line);
				
				if (aLine.isEndOfPage()) {
					logger.info("new page: " + aLine);
					currentPage.end();
					currentPage= new Page();
				} else {
					currentPage.process(aLine);
				}
			}
			
			currentPage.end();
			
			for (Item i : items) {
				sink.writeItem(i);
			}
		} finally {
			source.close();
			sink.close();
			
		}
	}
	
	class Page {
		// I need this because header lines can be different from one page to next - that is a Ghostscript artifact, can't change it

		Line headerLine= null;
		Item currentItem= null;
		
		void process(Line line) {
			if (headerLine == null) {
				if (line.isHeaderLine()) {
					headerLine= line;
					logger.info("new header: " + line);
				}
				// else skip
			} else {
				if (!line.isItemHeader()) {
					if (currentItem != null) {
						currentItem.process(line);
					}
				} else {
					if (currentItem != null) {
						items.add(currentItem);
						logger.info("finished item: " + currentItem);
					}
					currentItem= new Item(line, headerLine);
				}
			}
		}
		
		void end() {
			if (currentItem != null) {
				items.add(currentItem);
			}
		}
		
	}
}
