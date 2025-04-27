package org.swissmail.fred.commandProcessor;

import java.util.Collection;

public interface ICommand {

	public String getName();
	
	public Collection<Diagnostic> execute(String [] args);
	
	public boolean hasError();
	
	
}
