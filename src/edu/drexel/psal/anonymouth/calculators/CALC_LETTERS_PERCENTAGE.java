package edu.drexel.psal.anonymouth.calculators;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.eventDrivers.CharCounterEventDriver;
import edu.drexel.psal.jstylo.eventDrivers.RegexpCounterEventDriver;

public class CALC_LETTERS_PERCENTAGE extends Computer{

	@Override
	protected void compute() {
		isAvailable = true;
		Document theDoc = super.getDocument();
		CharCounterEventDriver lced = new CharCounterEventDriver();
		double numLetters = lced.getValue(theDoc);
		RegexpCounterEventDriver rced = new RegexpCounterEventDriver();
		rced.setParameter("regexp", "[A-Za-z]");
		super.presentValue = (rced.getValue(theDoc)/numLetters)*100;
	
	}
	

}
