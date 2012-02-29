package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.SentenceCounterEventDriver;
import edu.drexel.psal.jstylo.eventDrivers.WordCounterEventDriver;

/**
 * Calcuator for AVERAGE_SENTENCE_LENGTH present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_AVERAGE_SENTENCE_LENGTH extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		WordCounterEventDriver wced = new WordCounterEventDriver();
		SentenceCounterEventDriver sced = new SentenceCounterEventDriver();
		double numWords = wced.getValue(doc);
		double numSentences = sced.getValue(doc);
		presentValue = numWords/numSentences;
		
	}

}
