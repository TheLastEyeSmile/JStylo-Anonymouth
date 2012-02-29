package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.SyllableCounterEventDriver;
import edu.drexel.psal.jstylo.eventDrivers.WordCounterEventDriver;

/**
 * Calculator for AVERAGE_SYLLABLES_IN_WORD present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_AVERAGE_SYLLABLES_IN_WORD extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		WordCounterEventDriver wced = new WordCounterEventDriver();
		SyllableCounterEventDriver sced = new SyllableCounterEventDriver();
		double numWords = wced.getValue(doc);
		double numSylls = sced.getValue(doc);
		presentValue = numSylls/numWords;
		
	}

}
