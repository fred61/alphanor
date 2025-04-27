package org.swissmail.fred.wordleSolver.commands;

import java.util.regex.Pattern;

import org.swissmail.fred.commandProcessor.AbstractCommand;
import org.swissmail.fred.wordleSolver.Solver;

public class Test extends AbstractCommand {
	
	private final Solver solver;
	private final Pattern wordTest;
	private final Pattern resultTest;
	
	public Test(Solver solver) {
		super("test");
		this.solver= solver;
		
		int wordLength= solver.getWordLength();
		wordTest= Pattern.compile(String.format("[A-Z]{%d}", wordLength));
		resultTest= Pattern.compile(String.format("[012]{%d}", wordLength));
	}

	@Override
	protected void doExecute(String[] args) {
		if (args.length < 3) {
			addError("expect 2 parameters");
		}
		String word= args[1].toUpperCase();
		String result= args[2];
		
		if (!wordTest.matcher(word).matches()) {
			addError("parameter 1 must be a word of length " + solver.getWordLength());
		}
		if (!resultTest.matcher(result).matches()) {
			addError("parameter must be a sequence of 0, 1, 2 of of length " + solver.getWordLength());
		}
		
		if (!hasError()) {
			for(int i= 0; i < solver.getWordLength(); i++) {
				if (result.charAt(i) == '2') {
					solver.require(word.charAt(i), i);
				} else if (result.charAt(i) == '1') {
					solver.requireExcept(word.charAt(i), i);
				} else if (result.charAt(i) == '0') {
					char c= word.charAt(i);
					if (!isDuplicate(c, word)) {
					 solver.requireNot(word.charAt(i));
					}
				} else {
					throw new IllegalStateException("result param regex test failed");
				}
			}
			addInfo("active: " + solver.countActive());
		}
	}
	
	private boolean isDuplicate(char c, String word) {
		int count= 0;
		for(int i= 0; count < 2 && i < word.length(); i++) {
			if (word.charAt(i) == c) {
				count+= 1;
			}
		}
		return count > 1;
	}

}
