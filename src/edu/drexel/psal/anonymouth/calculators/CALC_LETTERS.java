package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

/**
 * Calculator for LETTERS feature
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_LETTERS extends Computer{

	@Override
	protected void compute() {
		isAvailable = true;
		Document theDoc = super.getDocument();
		
		RegexpCounterEventDriver rced = new RegexpCounterEventDriver();
		rced.setParameter("regexp", stringInBraces);
		presentValue = rced.getValue(theDoc);
	}

}
