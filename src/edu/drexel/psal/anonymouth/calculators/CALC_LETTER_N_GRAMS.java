package edu.drexel.psal.anonymouth.calculators;


/**
 * Calculator for LETTER_N_GRAMS present value (for the specific string in the Attribute's 'stringInBraces' field)
 * @author Andrew W.E. McDonald
 *
 */
public class CALC_LETTER_N_GRAMS extends Computer {

	@Override
	protected void compute() {
		isAvailable = true;
		char[] theText = super.getDocument().getProcessedText();
		int gramLength = stringInBraces.length();
		int textLength = theText.length;
		String thisGram;
		int i;
		int numOfTheseGrams = 0;
		for(i=0;i<=(textLength-gramLength);i++){
			thisGram = new String(theText,i,gramLength);
			if(thisGram.matches(stringInBraces))
				numOfTheseGrams++;
		}
		presentValue = numOfTheseGrams;
			
	}

}
