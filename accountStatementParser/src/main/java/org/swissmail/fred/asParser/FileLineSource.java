package org.swissmail.fred.asParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLineSource implements ILineSource {
	private static Logger logger= Logger.getLogger(FileLineSource.class.getCanonicalName());
	
	final File inputFile;
	BufferedReader input;
	
	FileLineSource(File inputFile) {
		this.inputFile= inputFile;
	}

	@Override
	public void open() throws Exception {
		input= new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), Charset.forName("UTF-8")));
		
	}

	@Override
	public String nextLine() {
		try {
			return input.readLine();
		} catch (IOException x) {
			logger.log(Level.SEVERE, "Exception reading line", x);
			return null;
		}
	}

	@Override
	public void close() {
		try {
			input.close();
		} catch (IOException x) {
			logger.log(Level.SEVERE, "Exception closing", x);
		}
	}

	
}
