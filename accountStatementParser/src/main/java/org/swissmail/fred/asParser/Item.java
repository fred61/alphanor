package org.swissmail.fred.asParser;

import java.util.ArrayList;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableSheet;

/**
 * An item in the statement.
 *
 */
class Item {
	String date;
	String valueDate;
	String balance;
	String credit;
	String debit;
	StringBuilder text= null;
	StringBuilder textTarget= null;
	List<Item> subItems= null;
	
	Item(Line itemLine, Line headerLine) {
		date= itemLine.tokens[0].content;
		
		text= new StringBuilder(itemLine.tokens[1].content);
		text.append(' ');
		for (int i = 2; i < itemLine.tokens.length - 3; i++) {
			text.append(itemLine.tokens[i].content);
			text.append(' ');
		}
		text.setLength(text.length() - 1);
		textTarget= text;
		
		valueDate= itemLine.tokens[itemLine.tokens.length - 3].content;
		
		balance= itemLine.tokens[itemLine.tokens.length - 1].content;
		
		if ("Belastung".equals(headerLine.closestToken(itemLine.tokens[itemLine.tokens.length - 2]).content)) {
			debit= itemLine.tokens[itemLine.tokens.length - 2].content;
		} else {
			credit= itemLine.tokens[itemLine.tokens.length - 2].content;
		}
	}
	
	Item(Item superItem, Line line) {
		this.date= superItem.date;
		this.valueDate= superItem.valueDate;
		this.balance= superItem.balance;
		this.text= new StringBuilder();
		
		if (superItem.debit != null) {
			this.debit= line.tokens[line.tokens.length - 1].content;
		}
		if (superItem.credit != null) {
			this.credit= line.tokens[line.tokens.length - 1].content;
		}
		for(int i= 0; i < line.tokens.length - 1; i++) {
			this.text.append(line.tokens[i].content);
			this.text.append(' ');
		}
		this.text.setLength(this.text.length() - 1);
	}
	
	void process(Line line) {
		if (!possibleCompoundItem()) {
			appendLineToText(line);
		} else {
			if (line.isCompoundItemStart()) {
				// new sub item
				if (subItems == null) {
					subItems= new ArrayList<>();
				}
				Item subItem= new Item(this, line);
				subItems.add(subItem);
				textTarget= subItem.text;
			} else {
				// append line to current text buffer
				appendLineToText(line);
			}
		}
	}
	
	void appendLineToText(Line line) {
		textTarget.append(" // ");
		for (Token t : line.tokens) {
			textTarget.append(t.content);
			textTarget.append(' ');
		}
		textTarget.setLength(textTarget.length() - 1);
	}
	
	boolean possibleCompoundItem() {
		return text.toString().equals("Vergütung") && debit != null && credit == null;
	}
	
	int writeToSheet(int lineNumber, WritableSheet sheet)  {
		if (subItems != null) {
			int l= lineNumber;
			for(Item i : subItems) {
				l= i.writeToSheet(l, sheet);
			}
			return l;
		} else {
			try {
				Label dateLabel= new Label(0, lineNumber, date);
				sheet.addCell(dateLabel);
				Label textLabel= new Label(1, lineNumber, "");
				textLabel.setString(text.toString());
				sheet.addCell(textLabel);
				if (credit != null) {
					Label creditLabel= new Label(2, lineNumber, credit);
					sheet.addCell(creditLabel);
				}
				if (debit != null) {
					Label debitLabel= new Label(3, lineNumber, debit);
					sheet.addCell(debitLabel);
				}
				
				return lineNumber + 1;
			} catch (Exception x) {
				throw new RuntimeException("exception writing item to sheet", x);
			}
		}
	}

	@Override
	public String toString() {
		return "Item [date=" + date + ", text=" + text + ", valueDate=" + valueDate + ", credit=" + credit + ", debit="
				+ debit + ", balance=" + balance + "]";
	}

}