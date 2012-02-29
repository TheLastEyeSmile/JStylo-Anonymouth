package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

/**
 * Calculator for DIGITS_PERCENTAGE
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_DIGITS_PERCENTAGE extends Computer {

	@Override
	protected void compute() {
		isAvailable = true;
		Document theDoc = super.getDocument();
		double NumChars = (double)theDoc.getProcessedText().length;
		RegexpCounterEventDriver reced = new RegexpCounterEventDriver();
		reced.setParameter("regexp", "\\d");
		presentValue = (reced.getValue(theDoc)/NumChars)*100;
	}
	
	
}
