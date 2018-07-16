package org.swissmail.fred.asParser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


// note: this class operates on Strings and Labels only, conversions to Date or Number are done in Excel if necessary

public class XLSheetItemSink implements IItemSink {
	private static Logger logger= Logger.getLogger(XLSheetItemSink.class.getCanonicalName());
	
	final File targetDir;
	WritableWorkbook workbook= null;
	WritableSheet sheet= null;
	int lineNumber;

	XLSheetItemSink(File targetDir) {
		this.targetDir= targetDir;
	}

	@Override
	public void open() throws Exception {
		workbook= Workbook.createWorkbook(new File(targetDir, "output.xls"));
		sheet= workbook.createSheet("Sheet1", 0);
		lineNumber= 0;
	}

	@Override
	public void writeItem(Item i) {
		if (i.subItems != null) {
			for(Item j : i.subItems) {
				writeItem(j);
			}
		} else {
			try {
				Label dateLabel= new Label(0, lineNumber, i.date);
				sheet.addCell(dateLabel);
				Label textLabel= new Label(1, lineNumber, "");
				textLabel.setString(i.text.toString());
				sheet.addCell(textLabel);
				if (i.credit != null) {
					Label creditLabel= new Label(2, lineNumber, i.credit);
					sheet.addCell(creditLabel);
				}
				if (i.debit != null) {
					Label debitLabel= new Label(3, lineNumber, i.debit);
					sheet.addCell(debitLabel);
				}
				
				lineNumber= lineNumber + 1;
			} catch (Exception x) {
				throw new RuntimeException("exception writing item to sheet", x);
			}
		}
		
	}

	@Override
	public void close() {
		if (workbook != null) {
			try {
				workbook.write();
				workbook.close();
			} catch (IOException x) {
				logger.log(Level.SEVERE, "IOException closing workbook", x);
			} catch (WriteException x) {
				logger.log(Level.SEVERE, "WriteException closing workbook", x);
			}
		}
	}
	
	
}
