package org.swissmail.fred.wordleSolver.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class AddWord extends AbstractCommand {

	private final Solver solver;

	public AddWord(Solver solver) {
		super("add");
		this.solver= solver;
	}

	protected void doExecute(String[] args) {
		Collection<String> oldWords= solver.getWords();
		
		List<String> newWords= new ArrayList<>(oldWords.size() + 1);
		newWords.addAll(oldWords);
		newWords.add(args[1].toUpperCase());
		
		solver.setWords(newWords.stream().sorted().collect(Collectors.toList()));
	}
	
}
