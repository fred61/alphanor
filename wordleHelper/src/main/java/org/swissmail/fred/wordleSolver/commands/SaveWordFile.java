package org.swissmail.fred.wordleSolver.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class SaveWordFile extends AbstractCommand {

	private final Solver solver;

	public SaveWordFile(Solver solver) {
		super("save");
		this.solver= solver;
	}

	protected void doExecute(String[] args) {
		File theFile= new File(args[1]);

		if (!theFile.exists()) {
			try {
				theFile.createNewFile();
			} catch (IOException e) {
				addError(args[1] + " does not exist and can't be created: " + e.getMessage());
			}
		}
		
		if (!hasError()) {
			if (!theFile.canWrite()) {
				addError("cannot write " + args[1]);
			} else {
				try (FileWriter fOut = new FileWriter(theFile); BufferedWriter out= new BufferedWriter(fOut)) {
					for (String word : solver.getWords()) {
						out.append(word);
						out.newLine();
					}
				} catch(IOException e) {
					addError(String.format("exception writing %s: %s", args[1], e.getMessage()));
				} 
			}
		}
	}
	
}
