package org.swissmail.fred;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PointSource {
	
	DataInputStream inputStream;
	Point next;
	
	public void open(File pointFile) throws IOException
	{
		inputStream= new DataInputStream(new BufferedInputStream(new FileInputStream(pointFile), 16384));
		readNext();
	}
	
	public Point next() throws IOException {
		Point result= next;
		readNext();
		return result;
	}
	
	public boolean haveNext()
	{
		return next != null;
	}
	
	public void close() throws IOException {
		inputStream.close();
	}
	
	private void readNext() throws IOException
	{
		try {
			next= new Point(inputStream.readShort(), inputStream.readShort());
		} catch (EOFException x) {
			next= null;
			inputStream.close();
		}
	}

}
