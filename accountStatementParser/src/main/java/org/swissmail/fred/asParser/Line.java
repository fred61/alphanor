package org.swissmail.fred.asParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class Line {
	static Pattern pageEndPattern = Pattern.compile("\\d+/\\d+");
	Token[] tokens;

	Line(String line) {
		//note: line.split doesn't cut it, need to know where in line a Token starts
		List<Token> tokenList = new ArrayList<>();

		char[] lineArr = line.toCharArray();

		int currentTokenStart = -1;

		for (int i = 0; i < lineArr.length; i++) {
			if (currentTokenStart < 0) {
				if (lineArr[i] != ' ') {
					currentTokenStart = i;
				}
			} else {
				if (lineArr[i] == ' ') {
					String content = new String(lineArr, currentTokenStart, i - currentTokenStart);
					tokenList.addAll(Token.makeTokens(currentTokenStart, content));
					currentTokenStart= -1;
				}
			}
		}

		if (currentTokenStart > 0) {
			String content = new String(lineArr, currentTokenStart, lineArr.length - currentTokenStart);
			tokenList.addAll(Token.makeTokens(currentTokenStart, content));
		}

		tokens = tokenList.toArray(new Token[tokenList.size()]);
	}

	boolean isEndOfPage() {
		boolean result = false;
		int i = 0;

		while (!result && i < tokens.length - 1) {
			result = ("Seite".equals(tokens[i].content)) && (pageEndPattern.matcher(tokens[i + 1].content).matches());
			i += 1;
		}

		return result;
	}

	boolean isHeaderLine() {
		return equalsAll(new String[] { "Datum", "Text", "Valuta", "Belastung", "Gutschrift" });
	}

	boolean isItemHeader() {
		return tokens[0].getTokenClass() == Token.TokenClass.date
				&& tokens[tokens.length - 1].getTokenClass() == Token.TokenClass.amount;

	}

	boolean isCompoundItemStart() {
		return tokens[0].getTokenClass() == Token.TokenClass.text
				&& tokens[tokens.length - 1].getTokenClass() == Token.TokenClass.amount;
	}

	boolean equalsAll(String[] strings) {
		if (tokens.length < strings.length) {
			return false;
		} else {
			for (int i = 0; i < strings.length; i++) {
				if (!strings[i].equals(tokens[i].content)) {
					return false;
				}
			}
			return true;
		}
	}

	Token closestToken(Token aToken) {
		int i = 0;
		int distance = Math.abs(aToken.startAt - tokens[i].startAt);
		do {
			int newDistance = Math.abs(aToken.startAt - tokens[i + 1].startAt);
			if (newDistance > distance) {
				return tokens[i];
			} else {
				distance = newDistance;
				i += 1;
			}

		} while (i < tokens.length);
		return tokens[i];
	}

	@Override
	public String toString() {
		return "Line [tokens=" + Arrays.toString(tokens) + "]";
	}
}