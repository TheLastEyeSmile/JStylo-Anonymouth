package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

/**
 * Calculator for DIGITS feature
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_DIGITS extends Computer {

	@Override
	protected void compute() {
		isAvailable = true;
		Document theDoc = super.getDocument();
		RegexpCounterEventDriver reced = new RegexpCounterEventDriver();
		reced.setParameter("regexp", super.stringInBraces);
		presentValue = reced.getValue(theDoc);
	}

}
