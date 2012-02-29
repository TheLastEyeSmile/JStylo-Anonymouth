package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.CharCounterEventDriver;
import edu.drexel.psal.jstylo.eventDrivers.WordCounterEventDriver;

/**
 * Calculator for AVERAGE_CHARACTERS_PER_WORD
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_AVERAGE_CHARACTERS_PER_WORD extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document theDoc = super.getDocument();
		CharCounterEventDriver cced = new CharCounterEventDriver();
		WordCounterEventDriver wced = new WordCounterEventDriver();
		double numWords = wced.getValue(theDoc);
		double charCount = cced.getValue(theDoc);
		presentValue = charCount/numWords;
	}

}
