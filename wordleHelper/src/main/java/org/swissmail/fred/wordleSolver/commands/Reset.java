package org.swissmail.fred.wordleSolver.commands;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class Reset extends AbstractCommand {

	private final Solver solver;
	
	public Reset(Solver solver) {
		super("reset");
		this.solver= solver;
	}
	
	@Override
	protected void doExecute(String[] args) {
		solver.reset();
	}

}
