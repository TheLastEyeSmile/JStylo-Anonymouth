package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.CharCounterEventDriver;

/**
 * Calculator for CHARACTER_COUNT
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_CHARACTER_COUNT extends Computer{

	@Override
	protected void compute() {
		isAvailable = true;
		CharCounterEventDriver cced = new CharCounterEventDriver();
		Document theDoc = getDocument();
		presentValue = cced.getValue(theDoc);
	}

}
