package org.swissmail.fred.commandProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CommandProcessor {

	private final HashMap<String, ICommand> commands= new HashMap<>();
	private final Pattern splitter= Pattern.compile("\\s+");
	private String prompt= "";
	
	public CommandProcessor() {
		register(new AbstractCommand("quit") {
			@Override
			protected void doExecute(String[] args) {
				//do nothing
			}
		});
	}
	
	public void run() {
		try (InputStreamReader r= new InputStreamReader(System.in); 
				BufferedReader b= new BufferedReader(r)) {
			String line;
			do {
				prompt();
				line= b.readLine();
				String[] tokens= splitter.split(line);
				if (tokens.length > 0) {
					ICommand cmd= commands.get(tokens[0]);
					if (cmd == null) {
						unknownCommand(line);
					} else {
						long elapsed= System.nanoTime();
						Collection<Diagnostic> diagnostics= cmd.execute(tokens);
						if (cmd.hasError()) {
							reportErrors(diagnostics);
						} else if (diagnostics.size() > 0) {
							reportInfo(diagnostics);
						}
						elapsed= System.nanoTime() - elapsed;
						reportExecutionTime(elapsed);
					}
					}
			} while (!isQuitCommand(line));
			
		} catch (IOException e) {
			
		}
	}
	
	private boolean isQuitCommand(String line) {
		return line == null || line.isEmpty() || line.isBlank() || line.startsWith("quit");
	}
	
	private void prompt() {
		System.out.print(prompt);
		System.out.print('>');
	}
	
	private void unknownCommand(String line) {
		System.out.print("unknown command: ");
		System.out.println(line);
	}
	
	private void reportErrors(Collection<Diagnostic> diagnostics) {
		System.err.println("command failed");
		diagnostics.stream().filter(d -> d.isError()).forEach(d -> {System.err.println(d.getMessage());});
	}
	
	private void reportInfo(Collection<Diagnostic> diagnostics) {
		diagnostics.stream().filter(d -> d.isInfo()).forEach(d -> {System.out.println(d.getMessage());});
	}
	
	private void reportExecutionTime(long timeNanos) {
		System.out.println(String.format("Command executed in %d us", timeNanos / 1000));
	}

	public void register(ICommand command) {
		commands.put(command.getName(), command);
	}
}
