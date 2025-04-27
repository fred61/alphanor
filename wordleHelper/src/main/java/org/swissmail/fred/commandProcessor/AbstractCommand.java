package org.swissmail.fred.commandProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

abstract public class AbstractCommand implements ICommand {

	private final String name;
	private Collection<Diagnostic> diagnostics= null;
	
	public AbstractCommand(String name) {
		this.name= name;
	}
	
	@Override
	final public String getName() {
		return name;
	}

	@Override
	final public Collection<Diagnostic> execute(String[] args) {
		diagnostics= null;
		doExecute(args);
		return diagnostics == null ? Collections.emptyList() : diagnostics;
	}
	
	final public boolean hasError() {
		return diagnostics != null && diagnostics.stream().anyMatch(d -> d.isError());
	}
	
	
	final protected void addError(String message) {
		if (diagnostics == null) {
			diagnostics= new ArrayList<Diagnostic>();
		}
		diagnostics.add(Diagnostic.error(message));
	}
	
	final protected void addInfo(String message) {
		if (diagnostics == null) {
			diagnostics= new ArrayList<Diagnostic>();
		}
		diagnostics.add(Diagnostic.info(message));
	}
	
	final protected Integer getNumberParameter(int parameterIndex, String[] args) {
		if (parameterIndex < 0 || parameterIndex > args.length - 1)  {
			return null;
		}
		String parameterString= args[parameterIndex];
		try {
			return Integer.parseInt(parameterString);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	final protected Integer getNumberParameter(int parameterIndex, String[] args, int defaultValue) {
		Integer result= getNumberParameter(parameterIndex, args);
		if (result == null) {
			result= defaultValue;
		}
		return result;
	}
	

	abstract protected void doExecute(String[] args);
}
