package org.swissmail.fred.asParser;

public interface IItemSink {
	
	void open() throws Exception;
	
	void writeItem(Item i);
	
	void close();

}
