package edu.drexel.psal.anonymouth.calculators;

/**
 * Calculator for CHARACTER_SPACE present value
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_CHARACTER_SPACE extends Computer{
	

	@Override
	protected void compute() {
		isAvailable = true;
		presentValue = super.getDocument().getProcessedText().length;
		
	}

}
