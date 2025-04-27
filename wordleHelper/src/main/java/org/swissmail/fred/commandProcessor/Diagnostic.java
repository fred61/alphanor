package org.swissmail.fred.commandProcessor;

public class Diagnostic {

	private enum Severity {
		ERROR
	   ,INFO
	}
	
	private final String message;
	private final Severity severity;
	
	private Diagnostic(Severity severity, String message) {
		this.severity = severity;
		this.message= message;
	}
	
	static public Diagnostic error(String message) {
		return new Diagnostic(Severity.ERROR, message);
	}
	
	static public Diagnostic info(String message) {
		return new Diagnostic(Severity.INFO, message);
	}
	
	public boolean isError() {
		return severity == Severity.ERROR;
	}
	
	public boolean isInfo() {
		return severity == Severity.INFO;
	}
	
	public String getMessage() {
		return message;
	}
}
