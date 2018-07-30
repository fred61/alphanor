package org.swissmail.fred.asParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Token {
	static private Pattern amountPattern= Pattern.compile("(?:[^0-9.]|^)(\\d+[0-9']*\\.\\d\\d)(?:[^0-9.]|$)");
	// NaN or start of string; followed by amount; followed by NaN or end of string.
	// PM: (?:) is non-capturing parenthesis
	static private Pattern datePattern= Pattern.compile("(?:[^0-9.]|^)(\\d{1,2}\\.\\d{1,2}\\.\\d{2,4})(?:[^0-9.]|$)");
	
	static private Pattern[] explodePatterns= new Pattern[] {
			amountPattern
		   ,datePattern
		   ,Pattern.compile("(Seite)")
		   ,Pattern.compile("(Datum)")
		   ,Pattern.compile("(Text)")
		   ,Pattern.compile("(Valuta)")
		   ,Pattern.compile("(Belastung)")
		   ,Pattern.compile("(Gutschrift)")
		   ,Pattern.compile("(Saldo)")
	};
	
	final int startAt;
	final String content;
	
	static Collection<Token> makeTokens(int startAt, String content)
	{
		Collection<Token> sourceCollection= Collections.singletonList(new Token(startAt, content));
		Collection<Token> targetCollection= Collections.emptyList();
		
		for(Pattern p : explodePatterns) {
			targetCollection= explode(sourceCollection, p);
			sourceCollection= targetCollection;
		}
		
		return targetCollection;
	}
	
	static private Collection<Token> explode(Collection<Token> tokens, Pattern pattern)
	{
		Collection<Token> result= new ArrayList<>();
		
		for(Token t : tokens) {
			Matcher m= pattern.matcher(t.content);
			
			int breakAt= 0;
			
			while (m.find()) {
				if (m.start(1) > breakAt) {
					result.add(new Token(t.startAt + breakAt, t.content.substring(breakAt, m.start(1))));
				}
				result.add(new Token(t.startAt + m.start(1), t.content.substring(m.start(1),m.end(1))));
				breakAt= m.end();
			}
			if (breakAt < t.content.length()) {
				result.add(new Token(t.startAt + breakAt, t.content.substring(breakAt, t.content.length())));
			}
		}
		return result;
		
	}
	
	private Token(int startAt, String content) {
		this.startAt= startAt;
		this.content= content;
	}
	
	private TokenClass tokenClass= null;
	
	enum TokenClass {text, date, amount};
	
	TokenClass getTokenClass() {
		if (tokenClass == null) {
			if (amountPattern.matcher(content).matches()) {
				tokenClass= TokenClass.amount;
			} else if (datePattern.matcher(content).matches()) {
				tokenClass= TokenClass.date;
			} else {
				tokenClass= TokenClass.text;
			}
		}
		return tokenClass;
	}
	
	@Override
	public String toString() {
		return "Token [startAt=" + startAt + ", content=" + content + "]";
	}
}