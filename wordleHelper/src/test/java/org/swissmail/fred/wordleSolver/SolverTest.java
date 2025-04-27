package org.swissmail.fred.wordleSolver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolverTest {


	@Test
	void testIndexes() {
		Solver theSolver= new Solver(3);
		
		List<String> words= List.of("FOO", "BAR");
		theSolver.setWords(words);
		
		BitmapIndex index= theSolver.getIndex('A', 1);
		assertEquals(index, new BitmapIndex(new long[] {2}));
		
		//etc
	}
	
	@Test
	void testRequire() {
		Solver theSolver= new Solver(3);
		
		List<String> words= List.of("FOO", "BAR", "BAZ", "BOO");
		theSolver.setWords(words);
		
		theSolver.require('A', 1);
		
		List<String> result= new ArrayList<>();
		result.addAll(theSolver.listActive());
		
		assertEquals(result, List.of("BAR", "BAZ"));
	}

	@Test
	void testRequireNot() {
		Solver theSolver= new Solver(3);
		
		List<String> words= List.of("FOO", "BAR", "BAZ", "BOO");
		theSolver.setWords(words);
		
		theSolver.requireNot('A');
		
		List<String> result= new ArrayList<>();
		result.addAll(theSolver.listActive());
		
		assertEquals(result, List.of("FOO", "BOO"));
	}
	
	@Test
	void testRequireExcept() {
		Solver theSolver= new Solver(3);
		
		List<String> words= List.of("FOO", "BAR", "ZAB", "ABC");
		theSolver.setWords(words);
		
		theSolver.requireExcept('B', 1);
		
		List<String> result= new ArrayList<>();
		result.addAll(theSolver.listActive());
		
		assertEquals(result, List.of("BAR", "ZAB"));
		
	}
}
