package org.swissmail.fred.wordleSolver.commands;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class ListWords extends AbstractCommand {

	private final Solver solver;
	
	public ListWords(Solver solver) {
		super("list");
		this.solver= solver;
	}

	@Override
	protected void doExecute(String[] args) {
		Integer from= getNumberParameter(1, args, 0);
		Integer to= getNumberParameter(2, args, Integer.MAX_VALUE);
		
		solver.listActive().stream().skip(from).limit(to).forEach(word -> {
			System.out.println(word);
		});
	}

}
