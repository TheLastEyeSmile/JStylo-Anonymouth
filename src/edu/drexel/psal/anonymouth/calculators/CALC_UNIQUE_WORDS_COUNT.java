package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.UniqueWordsCounterEventDriver;

/**
 * Calculator for UNIQUE_WORDS_COUNT present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_UNIQUE_WORDS_COUNT extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		UniqueWordsCounterEventDriver uwced = new UniqueWordsCounterEventDriver();
		int numUnique = (int) uwced.getValue(doc);
		presentValue = numUnique;
	}

}
