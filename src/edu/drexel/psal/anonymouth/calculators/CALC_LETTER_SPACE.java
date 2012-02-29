package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;
import com.jgaap.generics.EventGenerationException;

import edu.drexel.psal.jstylo.eventDrivers.LetterCounterEventDriver;

/**
 * Calculator for LETTER_SPACE present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_LETTER_SPACE extends Computer{

	@Override
	protected void compute() throws EventGenerationException {
		isAvailable = true;
		Document doc = super.getDocument();
		LetterCounterEventDriver lced = new LetterCounterEventDriver();
		presentValue = lced.getValue(doc);
		 
	}

}
