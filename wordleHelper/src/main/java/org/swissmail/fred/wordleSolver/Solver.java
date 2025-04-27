package org.swissmail.fred.wordleSolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.swissmail.fred.commandProcessor.CommandProcessor;
import org.swissmail.fred.wordleSolver.commands.AddWord;
import org.swissmail.fred.wordleSolver.commands.ListWords;
import org.swissmail.fred.wordleSolver.commands.LoadWordFile;
import org.swissmail.fred.wordleSolver.commands.Reset;
import org.swissmail.fred.wordleSolver.commands.SaveWordFile;
import org.swissmail.fred.wordleSolver.commands.Test;

public class Solver {
	

	public static void main(String[] args) {
		Solver theSolver= new Solver();
		CommandProcessor processor= new CommandProcessor();
		
		processor.register(new LoadWordFile(theSolver));
		processor.register(new ListWords(theSolver));
		processor.register(new Test(theSolver));
		processor.register(new Reset(theSolver));
		processor.register(new SaveWordFile(theSolver));
		processor.register(new AddWord(theSolver));
		
		processor.run();
	}
	
	private Solver() {
		this(5);
	}
	
	Solver(int wordLength) {
		this.wordLength= wordLength;
		createIndex();
	}
	
	private final int wordLength;
	private BitmapIndex[][] index; 
	
	private List<String> words;
	private BitmapIndex active;
	
	public int getWordLength() {
		return wordLength;
	}
	
	public void setWords(List<String> words) {
		this.words= words;
		buildIndex();
	}
	
	public Collection<String> getWords() {
		return Collections.unmodifiableCollection(words);
	}
	
	public Collection<String> listActive() {
		Collection<String> result= new ArrayList<String>();
		
		for(int i= 0; i < words.size(); i++) {
			if (active.isSet(i)) {
				result.add(words.get(i));
			}
		}
		
		return result;
	}
	
	public int countActive() {
		return active.countSet();
	}
	
	private void createIndex() {
		index= new BitmapIndex[('Z' - 'A') + 1][wordLength];		
	}
	
	private void buildIndex() {
		active= BitmapIndex.allSet(words.size());
		initialiseIndex();
		for(int i= 0; i < words.size(); i++) {
			String word= words.get(i);
			for(int j= 0; j < wordLength; j++) {
				getIndex(word.charAt(j), j).set(i);
			}
		}
	}
	
	private void initialiseIndex() {
		int wordsCount= words.size();
		for(int i= 0; i < index.length; i++) {
			for(int j= 0; j < wordLength; j++) {
				index[i][j]= new BitmapIndex(wordsCount);
			}
		}
	}
	
	BitmapIndex getIndex(char c, int wordPos) {
		return index[c - 'A'][wordPos];
	}
	
	public void require(char c, int wordPos) {
		active.intersectWith(getIndex(c, wordPos));
	}
	
	public void requireExcept(char c, int wordPos) {
		// word must have c but not at wordPos
		BitmapIndex b= new BitmapIndex(words.size());
		for (int i= 0; i < wordLength; i++) {
			if (i != wordPos) {
				b.uniteWith(getIndex(c, i));
			}
		}
		
		active.intersectWith(b);		
	}
	
	public void requireNot(char c) {
		// word must not have c in any position
		BitmapIndex b= new BitmapIndex(words.size());
		for (int i= 0; i < wordLength; i++) {
			b.uniteWith(getIndex(c, i));
		}
		
		active.intersectWith(b.invert());
		
	}
	
	public void reset() {
		active= BitmapIndex.allSet(words.size());
	}
	
}
