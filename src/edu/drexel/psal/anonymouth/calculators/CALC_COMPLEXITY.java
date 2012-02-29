package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.UniqueWordsCounterEventDriver;
import edu.drexel.psal.jstylo.eventDrivers.WordCounterEventDriver;

/**
 * Calculator for COMPLEXITY present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_COMPLEXITY extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		UniqueWordsCounterEventDriver uwced = new UniqueWordsCounterEventDriver();
		WordCounterEventDriver wced = new WordCounterEventDriver();
		double numComplex = uwced.getValue(doc);
		double numWords = wced.getValue(doc);
		presentValue = numComplex/numWords;
	}

}
