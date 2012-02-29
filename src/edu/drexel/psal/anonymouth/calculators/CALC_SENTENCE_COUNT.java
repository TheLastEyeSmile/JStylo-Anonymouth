package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.SentenceCounterEventDriver;

/**
 * Calculator for SENTENCE_COUNT present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_SENTENCE_COUNT extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		SentenceCounterEventDriver sced = new SentenceCounterEventDriver();
		int numSentences = (int) sced.getValue(doc);
		presentValue = numSentences;
		
		
	}

}
