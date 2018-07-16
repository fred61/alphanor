package org.swissmail.fred.asParser;

public interface ILineSource {
	
	void open() throws Exception;
	
	String nextLine();

	void close();
}
