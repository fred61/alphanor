package org.swissmail.fred.wordleSolver.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class LoadWordFile extends AbstractCommand {
	
	private final Solver solver;

	public LoadWordFile(Solver solver) {
		super("load");
		this.solver= solver;
	}

	@Override
	protected void doExecute(String[] args) {
		File theFile= new File(args[1]);
		
		if (!theFile.canRead()) {
			addError("cannot read " + args[1]);
		} else {
			int wordLength= solver.getWordLength();
			final Pattern wordTest= Pattern.compile(String.format("[A-Z]{%d}", wordLength));
			
			String line;
			int linesAdded= 0;
			List<String> words= new ArrayList<>();			//eventually could preallocate this  
			try (FileReader fIn= new FileReader(theFile); BufferedReader in= new BufferedReader(fIn)) {
				line= in.readLine();
				while (line != null) {
					line= line.toUpperCase();
					if (line.length() == wordLength) {
						if (!wordTest.matcher(line).matches()) {
							addInfo("not a good word: " + line + ", ignoring");
						} else {
							words.add(line);
							linesAdded+= 1;
						}
					}
					line= in.readLine();
				}
				addInfo(String.format("read %d words", linesAdded));
				solver.setWords(words);
			} catch(IOException e) {
				addError(String.format("exception reading %s: %s", args[1], e.getMessage()));
			}
		}
		
	}

}
