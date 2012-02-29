package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

/**
 * Calculator for WORD_BIGRAMS
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_WORD_BIGRAMS extends Computer {

	@Override
	protected void compute() {
		isAvailable = true;
		String theWords = stringInBraces;
		int startWord = theWords.indexOf("(")+1;
		int endWord = theWords.indexOf(")"); 
		String wordOne = theWords.substring(startWord,endWord);
		String wordTwo = theWords.substring(theWords.indexOf("(",startWord)+1,theWords.indexOf(")",endWord+1));
		
		if(wordOne.contains("."))
			wordOne = wordOne.replace(".","\\.");
		if(wordOne.contains("?"))
			wordOne = wordOne.replace("?","\\?");
		if(wordTwo.contains("."))
			wordTwo = wordTwo.replace(".","\\.");
		if(wordTwo.contains("?"))
			wordTwo = wordTwo.replace("?","\\?");
		
		Document theDoc = super.getDocument();
		RegexpCounterEventDriver rced = new RegexpCounterEventDriver();
		rced.setParameter("regexp", "((\\s|\\b)("+wordOne+"\\s*"+wordTwo+")(\\s|\\b)){1}+");
		presentValue = rced.getValue(theDoc);
	}
		
	

}
