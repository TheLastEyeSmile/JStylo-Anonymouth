package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

/**
 * Calculator for WORDS
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_WORDS extends Computer {

	@Override
	protected void compute() {
		isAvailable = true;
		Document theDoc = super.getDocument();
		RegexpCounterEventDriver rced = new RegexpCounterEventDriver();
		rced.setParameter("regexp", "((\\s|\\b)("+stringInBraces+")(\\s|\\b)){1}+");
		presentValue = rced.getValue(theDoc);
	}

}
